package me.scolastico.tools.web.admin.commands

import me.scolastico.tools.web.admin.AdminPanelInstaller
import picocli.CommandLine

@CommandLine.Command(
    name = "ap-delete-permission",
    aliases = ["delperm"],
    mixinStandardHelpOptions = true,
    version = ["1.0.0"],
    description = ["Delete an permission of an 's.Admin' admin panel user."]
)
class DeletePermissionCommand: Runnable {

    @CommandLine.Parameters(description = ["The user where the permission will be added. (Case insensitive)"])
    var user: String = ""

    @CommandLine.Parameters(description = [
        "The 'permission' or better known the command you want to allow to a user.",
        "You can use a '*' at the start/end to allow all command beginning/ending with the 'permission'."
    ])
    var permission: String = ""

    override fun run() {
        val permissions = AdminPanelInstaller.currentConfig.permissions[user.lowercase()]?: ArrayList()
        if (permissions.contains(permission.lowercase())) {
            permissions.remove(permission.lowercase())
            AdminPanelInstaller.currentConfig.permissions[user.lowercase()] = permissions
            AdminPanelInstaller.saveConfig()
            print(AdminPanelInstaller.prefix()
                .fgGreen().a("Removed permission '$permissions' from the user '$user'.")
                .fgDefault())
        } else {
            print(AdminPanelInstaller.prefix()
                .fgRed().a("This user and/or permission does not exist!")
                .fgDefault())
        }
    }

}
