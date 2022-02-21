package me.scolastico.tools.web.admin

import com.kosprov.jargon2.api.Jargon2
import io.ktor.websocket.*
import kotlinx.coroutines.runBlocking
import me.scolastico.tools.console.ConsoleManager
import me.scolastico.tools.handler.ConfigHandler
import me.scolastico.tools.web.WebserverManager
import me.scolastico.tools.web.admin.commands.*
import me.scolastico.tools.web.admin.etc.AdminPanelConfig
import me.scolastico.tools.web.admin.etc.NewLogLineEventHandler
import me.scolastico.tools.web.admin.etc.WebsocketInstaller
import me.scolastico.tools.web.admin.web.AdminPanelAPI
import me.scolastico.tools.web.admin.web.AdminPanelFrontend
import org.fusesource.jansi.Ansi
import java.time.Instant
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.concurrent.timerTask


class AdminPanelInstaller private constructor() {
    companion object {

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

        fun install(webserver: WebserverManager, installWebsocketExtension: Boolean = true) {
            if (enabled) return
            enabled = true
            loadConfig()
            if (installWebsocketExtension) webserver.registerModule(WebsocketInstaller())
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

        fun loadConfig() {
            if (!configHandler.checkIfExists()) configHandler.saveDefaultConfig()
            currentConfig = configHandler.loadConfig();
        }

        fun saveConfig(config: AdminPanelConfig = currentConfig) {
            currentConfig = config
            configHandler.storeConfig(config)
        }

        fun prefix(): Ansi{
            return Ansi.ansi()
                .fgBright(Ansi.Color.BLACK).a("[")
                .fgBrightMagenta().a("s.Admin")
                .fgBright(Ansi.Color.BLACK).a("] ")
        }

        fun startTimer() {
            Timer("s-admin-panel-token-invalidator").scheduleAtFixedRate(
                timerTask {
                    val toDeleteTokens = ArrayList<String>()
                    val toDeleteUser = ArrayList<String>()
                    val toDeleteCon = ArrayList<String>()
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
                        if (AdminPanelAPI.users.containsKey(user)) {
                            try {
                                val id = AdminPanelAPI.users[user]!!
                                val con = AdminPanelAPI.connections[id]!!
                                AdminPanelAPI.users.remove(user)
                                AdminPanelAPI.connections.remove(id)
                                AdminPanelAPI.timeouts.remove(id)
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
                    for ((id,date) in AdminPanelAPI.timeouts) {
                        if (date.plusMillis(TimeUnit.MINUTES.toMillis(2)).isBefore(Instant.now())) {
                            toDeleteCon.add(id)
                        }
                    }
                    for (id in toDeleteCon) {
                        val con = AdminPanelAPI.connections[id]!!
                        AdminPanelAPI.connections.remove(id)
                        AdminPanelAPI.timeouts.remove(id)
                        Thread(Runnable {
                            runBlocking {
                                try {
                                    con.close(CloseReason(CloseReason.Codes.NORMAL, "timeout"))
                                } catch (ignored: Throwable) {}
                            }
                        }).start()
                    }
                },
                TimeUnit.SECONDS.toMillis(30),
                TimeUnit.SECONDS.toMillis(30)
            )
        }
    }
}