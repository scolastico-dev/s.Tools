package me.scolastico.tools.web.admin.commands

import me.scolastico.tools.web.admin.AdminPanelInstaller
import picocli.CommandLine

@CommandLine.Command(
    name = "ap-delete-user",
    aliases = ["deluser"],
    mixinStandardHelpOptions = true,
    version = ["1.0.0"],
    description = ["Delete an permission of an 's.Admin' admin panel user."]
)
class DeleteUserCommand: Runnable {

    @CommandLine.Parameters(description = ["The user name of the user which will be created. (Case insensitive)"])
    var user: String = ""

    override fun run() {
        if (AdminPanelInstaller.currentConfig.user.containsKey(user.lowercase())) {
            AdminPanelInstaller.currentConfig.user.remove(user.lowercase())
            AdminPanelInstaller.currentConfig.permissions.remove(user.lowercase())
            AdminPanelInstaller.tokens.remove(user.lowercase())
            AdminPanelInstaller.saveConfig()
            println(AdminPanelInstaller.prefix()
                .fgGreen().a("Removed the user '$user'.")
                .fgDefault())
        } else {
            println(AdminPanelInstaller.prefix()
                .fgRed().a("This user does not exist!")
                .fgDefault())
        }
    }

}
