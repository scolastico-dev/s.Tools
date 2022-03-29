@file:Suppress("WildcardImport")
package me.scolastico.tools.web.admin

import com.kosprov.jargon2.api.Jargon2
import io.ktor.http.cio.websocket.*
import kotlinx.coroutines.runBlocking
import me.scolastico.tools.console.ConsoleManager
import me.scolastico.tools.handler.ConfigHandler
import me.scolastico.tools.web.WebserverManager
import me.scolastico.tools.web.admin.commands.*
import me.scolastico.tools.web.admin.etc.AdminPanelConfig
import me.scolastico.tools.web.admin.etc.KtorGsonInstaller
import me.scolastico.tools.web.admin.etc.KtorWebsocketInstaller
import me.scolastico.tools.web.admin.etc.NewLogLineEventHandler
import me.scolastico.tools.web.admin.web.AdminPanelAPI
import me.scolastico.tools.web.admin.web.AdminPanelFrontend
import org.fusesource.jansi.Ansi
import java.time.Instant
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.concurrent.timerTask


class AdminPanelInstaller private constructor() {
    companion object {

        private const val CLEANUP_SCHEDULER_SECONDS: Long = 30;

        var currentConfig: AdminPanelConfig = AdminPanelConfig();
        var configHandler: ConfigHandler<AdminPanelConfig> = ConfigHandler(currentConfig, "admin-config.json")
        val tokens: HashMap<String, String> = HashMap()
        val tokenDate: HashMap<String, Instant> = HashMap()
        val verifier: Jargon2.Verifier = Jargon2.jargon2Verifier()
        private var enabled = false

        val hasher: Jargon2.Hasher = Jargon2.jargon2Hasher()
            .type(Jargon2.Type.ARGON2d)
            .memoryCost(65536)
            .timeCost(3)
            .parallelism(4)
            .saltLength(16)
            .hashLength(16)

        /**
         * Install the s.Admin module in the ConsoleManager nad a WebserverManager.
         * @param webserver The webserver where to install the module.
         * @param installWebsocketExtension Should the ktor websocket extension be installed automatically?
         */
        fun install(
            webserver: WebserverManager,
            installWebsocketExtension: Boolean = true,
            installGSONExtension: Boolean = true
        ) {
            if (enabled) return
            enabled = true
            loadConfig()
            if (installWebsocketExtension) webserver.registerModule(KtorWebsocketInstaller())
            if (installGSONExtension) webserver.registerModule(KtorGsonInstaller())
            webserver.registerModule(AdminPanelAPI())
            webserver.registerModule(AdminPanelFrontend())
            ConsoleManager.registerCommand(AddPermissionCommand())
            ConsoleManager.registerCommand(ChangePasswordCommand())
            ConsoleManager.registerCommand(CreateUserCommand())
            ConsoleManager.registerCommand(DeletePermissionCommand())
            ConsoleManager.registerCommand(DeleteUserCommand())
            ConsoleManager.registerCommand(ListPermissionsCommand())
            ConsoleManager.registerCommand(ListUserCommand())
            ConsoleManager.registerNewLogLineRoutine(NewLogLineEventHandler())
            startTimer()
        }

        /**
         * Load the s.Admin config file. (admin-config.json)
         */
        fun loadConfig() {
            if (!configHandler.checkIfExists()) configHandler.saveDefaultConfig()
            currentConfig = configHandler.loadConfig();
        }

        /**
         * Save the s.Admin config file. (admin-config.json)
         */
        fun saveConfig(config: AdminPanelConfig = currentConfig) {
            currentConfig = config
            configHandler.storeConfig(config)
        }

        /**
         * Get the default console prefix from s.Admin as an ansi object
         */
        fun prefix(): Ansi{
            return Ansi.ansi()
                .fgBright(Ansi.Color.BLACK).a("[")
                .fgBrightMagenta().a("s.Admin")
                .fgBright(Ansi.Color.BLACK).a("] ")
        }

        /**
         * Start the cleanup scheduler.
         * This function is useful when starting s.Admin manually.
         */
        fun startTimer() {
            Timer("s-admin-panel-token-invalidator").scheduleAtFixedRate(
                timerTask {
                    val toDeleteTokens = ArrayList<String>()
                    val toDeleteUser = ArrayList<String>()
                    for ((token,date) in tokenDate) {
                        if (date.plusMillis(TimeUnit.HOURS.toMillis(1)).isBefore(Instant.now())) {
                            toDeleteTokens.add(token)
                        }
                    }
                    for (token in toDeleteTokens) {
                        tokenDate.remove(token)
                        for ((user,t) in tokens) {
                            if (token == t) toDeleteUser.add(user)
                        }
                    }
                    for (user in toDeleteUser) {
                        tokens.remove(user)
                        println(prefix().fgYellow()
                            .a("The session of the user '$user' timed out.")
                            .fgDefault())
                        if (AdminPanelAPI.connections.containsKey(user)) {
                            try {
                                val con = AdminPanelAPI.connections[user]!!
                                AdminPanelAPI.connections.remove(user)
                                Thread(Runnable {
                                    runBlocking {
                                        try {
                                            con.close(CloseReason(CloseReason.Codes.NORMAL, "timeout"))
                                        } catch (ignored: Throwable) {}
                                    }
                                }).start()
                            } catch (ignored: Throwable) {}
                        }
                    }
                }, TimeUnit.SECONDS.toMillis(CLEANUP_SCHEDULER_SECONDS),
                TimeUnit.SECONDS.toMillis(CLEANUP_SCHEDULER_SECONDS))
        }
    }
}
