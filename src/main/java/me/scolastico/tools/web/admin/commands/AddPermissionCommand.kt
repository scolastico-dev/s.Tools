package me.scolastico.tools.web.admin.commands

import me.scolastico.tools.web.admin.AdminPanelInstaller
import picocli.CommandLine
import picocli.CommandLine.Parameters

@CommandLine.Command(
    name = "ap-add-permission",
    aliases = ["addperm"],
    mixinStandardHelpOptions = true,
    version = ["1.0.0"],
    description = ["Add permissions to and 's.Admin' admin panel user."]
)
class AddPermissionCommand: Runnable {

    @Parameters(description = ["The user where the permission will be added. (Case insensitive)"])
    var user: String = ""

    @Parameters(description = [
        "The 'permission' or better known the command you want to allow to a user.",
        "You can use a '*' at the start/end to allow all command beginning/ending with the 'permission'."
    ])
    var permission: String = ""

    override fun run() {
        if (AdminPanelInstaller.currentConfig.user.containsKey(user.lowercase())) {
            val permissions = AdminPanelInstaller.currentConfig.permissions[user.lowercase()]?: ArrayList()
            if (!permissions.contains(permission)) {
                permissions.add(permission)
                AdminPanelInstaller.currentConfig.permissions[user.lowercase()] = permissions
                AdminPanelInstaller.saveConfig()
                println(AdminPanelInstaller.prefix()
                    .fgGreen().a("Added permission '$permission' to the user '$user'.")
                    .fgDefault())
            } else {
                println(AdminPanelInstaller.prefix()
                    .fgRed().a("Sorry, the user '$user' has already the permission '$permission'.")
                    .fgDefault())
            }
        } else {
            println(AdminPanelInstaller.prefix()
                .fgRed().a("Sorry, this user could not be found.")
                .fgDefault())
        }
    }
}