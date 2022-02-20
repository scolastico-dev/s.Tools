package me.scolastico.tools.web.admin.commands

import me.scolastico.tools.web.admin.AdminPanelInstaller
import org.fusesource.jansi.Ansi
import picocli.CommandLine

@CommandLine.Command(
    name = "ap-list-users",
    aliases = ["listuser"],
    mixinStandardHelpOptions = true,
    version = ["1.0.0"],
    description = ["List all users of the 's.Admin' admin panel."]
)
class ListUserCommand: Runnable {

    override fun run() {
        val users = AdminPanelInstaller.currentConfig.user.keys
        if (users.size > 0) {
            print(
                AdminPanelInstaller.prefix()
                .fgGreen().a("Found ${users.size} users: ")
                .fgDefault().a(users.joinToString(
                        Ansi.ansi()
                    .fgYellow().a(", ").fgDefault().toString())))
        } else {
            print(
                AdminPanelInstaller.prefix()
                .fgGreen().a("No users where found!")
                .fgDefault())
        }
    }

}
