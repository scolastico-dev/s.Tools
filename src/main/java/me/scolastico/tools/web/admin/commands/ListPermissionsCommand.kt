package me.scolastico.tools.web.admin.commands

import me.scolastico.tools.etc.TableGenerator
import me.scolastico.tools.etc.TableGeneratorThemes
import me.scolastico.tools.web.admin.AdminPanelInstaller
import org.fusesource.jansi.Ansi
import picocli.CommandLine
import picocli.CommandLine.Option

@CommandLine.Command(
    name = "ap-list-permissions",
    aliases = ["listperm"],
    mixinStandardHelpOptions = true,
    version = ["1.0.0"],
    description = ["List the permissions of 's.Admin' (an) admin panel user(s)."]
)
class ListPermissionsCommand: Runnable {

    @Option(
        names = ["-u", "--user"],
        description = ["Set an username to only show the permissions of an user."]
    )
    var user: String? = null

    override fun run() {
        if (user != null && user!!.isNotEmpty()) {
            if (AdminPanelInstaller.currentConfig.user.containsKey(user!!.lowercase())) {
                val permissions = AdminPanelInstaller.currentConfig.permissions[user]?: ArrayList()
                if (permissions.size > 0) {
                    println(AdminPanelInstaller.prefix()
                        .fgGreen().a("Found ${permissions.size} permissions for the user '$user': ")
                        .fgDefault().a(permissions.joinToString(Ansi.ansi()
                            .fgYellow().a(", ").fgDefault().toString())))
                } else {
                    println(AdminPanelInstaller.prefix()
                        .fgGreen().a("The user '$user' has no permissions.")
                        .fgDefault())
                }
            } else {
                println(AdminPanelInstaller.prefix()
                    .fgRed().a("This user does not exist!")
                    .fgDefault())
            }
        } else {
            val generator = TableGeneratorThemes.FANCY_BOARDER()
                .setBoarderColorPrefix(Ansi.ansi().fgBright(Ansi.Color.BLACK).toString())
                .setBoarderColorSuffix(Ansi.ansi().fgDefault().toString())
                .setAutoLineBreak(100)
                .addContent(
                    Ansi.ansi().fgYellow().a("User").reset().toString(),
                    Ansi.ansi().a(TableGenerator.CENTER_ALIGNMENT).fgYellow().a("Permission")
                        .reset().toString()
                )
                .addTableLineSeparator()
            var firstLine = true
            for (user in AdminPanelInstaller.currentConfig.permissions.keys) {
                if (!firstLine) generator.addTableLineSeparator()
                if (firstLine) firstLine = false
                generator.addContent(user, AdminPanelInstaller.currentConfig.permissions[user]!!.joinToString(", "))
            }
            println(generator)
        }
    }

}
