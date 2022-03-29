package me.scolastico.tools.web.admin.commands

import me.scolastico.tools.web.admin.AdminPanelInstaller
import org.apache.commons.io.FileUtils
import picocli.CommandLine
import java.io.File
import java.nio.charset.StandardCharsets

@CommandLine.Command(
    name = "ap-adduser",
    aliases = ["adduser"],
    mixinStandardHelpOptions = true,
    version = ["1.0.0"],
    description = ["Add an user to the 's.Admin' admin panel."]
)
class CreateUserCommand: Runnable {

    @CommandLine.Parameters(description = ["The user name of the user which will be created. (Case insensitive)"])
    var user: String = ""

    @CommandLine.Parameters(description = [
        "The password.",
        "If you dont want to enter it in the console because of the console log",
        "write the password into an file and add an file@ before the password string",
        "to load it from an file and to automatically delete that file afterwards."
    ])
    var password: String = ""

    override fun run() {
        if (!AdminPanelInstaller.currentConfig.user.containsKey(user.lowercase())) {
            createUser(user, password)
            println(
                AdminPanelInstaller.prefix()
                    .fgGreen().a("Created the user '$user'.")
                    .fgDefault())
        } else {
            println(
                AdminPanelInstaller.prefix()
                .fgRed().a("Sorry, the user '$user' exists already.")
                .fgDefault())
        }
    }

    companion object {
        /**
         * Create a new user or override the password of an existing user.
         * The password will automatically be encrypted with argon2d
         * @param user The username.
         * @param password The password.
         */
        fun createUser(user: String, password: String) {
            var p = password
            if (password.startsWith("file@")) {
                val file = File(password.substring("file@".length))
                if (!file.exists()) {
                    println(
                        AdminPanelInstaller.prefix()
                            .fgRed().a("Sorry, this password file does not exist!")
                            .fgDefault())
                }
                p = FileUtils.readFileToString(file, StandardCharsets.UTF_8)
            }
            p = AdminPanelInstaller.hasher.password(p.toByteArray()).encodedHash()
            AdminPanelInstaller.currentConfig.user[user.lowercase()] = p
            AdminPanelInstaller.saveConfig()
        }
    }

}
