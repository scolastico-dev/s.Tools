package me.scolastico.tools.web.admin.commands

import me.scolastico.tools.web.admin.AdminPanelInstaller
import picocli.CommandLine

@CommandLine.Command(
    name = "ap-passwd",
    aliases = ["passwd"],
    mixinStandardHelpOptions = true,
    version = ["1.0.0"],
    description = ["Change the password of an 's.Admin' admin panel user."]
)
class ChangePasswordCommand: Runnable {

    @CommandLine.Parameters(description = ["The user where to edit the password. (Case insensitive)"])
    var user: String = ""

    @CommandLine.Parameters(description = [
        "The password.",
        "If you dont want to enter it in the console because of the console log",
        "write the password into an file and add an file@ before the password string",
        "to load it from an file and to automatically delete that file afterwards."
    ])
    var password: String = ""

    override fun run() {
        if (AdminPanelInstaller.currentConfig.user.containsKey(user.lowercase())) {
            CreateUserCommand.createUser(user, password)
            AdminPanelInstaller.tokens.remove(user.lowercase())
            println(AdminPanelInstaller.prefix()
                .fgGreen().a("Updated the password from the user '$user'.")
                .fgDefault())
        } else {
            println(AdminPanelInstaller.prefix()
                .fgRed().a("Sorry, this user could not be found.")
                .fgDefault())
        }
    }

}
