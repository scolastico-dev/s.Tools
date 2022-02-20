package me.scolastico.tools.web.admin

import com.kosprov.jargon2.api.Jargon2
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


class AdminPanelInstaller private constructor() {
    companion object {

        var currentConfig: AdminPanelConfig = AdminPanelConfig();
        var configHandler: ConfigHandler<AdminPanelConfig> = ConfigHandler(currentConfig, "admin-config.json")
        val tokens: HashMap<String, String> = HashMap()
        val verifier: Jargon2.Verifier = Jargon2.jargon2Verifier()

        val hasher: Jargon2.Hasher = Jargon2.jargon2Hasher()
            .type(Jargon2.Type.ARGON2d)
            .memoryCost(65536)
            .timeCost(3)
            .parallelism(4)
            .saltLength(16)
            .hashLength(16)

        fun install(webserver: WebserverManager, installWebsocketExtension: Boolean = true) {
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
    }
}