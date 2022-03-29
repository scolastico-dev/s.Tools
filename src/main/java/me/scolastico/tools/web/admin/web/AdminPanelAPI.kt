package me.scolastico.tools.web.admin.web

import io.ktor.application.*
import io.ktor.features.*
import io.ktor.http.*
import io.ktor.http.cio.websocket.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.websocket.*
import me.scolastico.tools.console.ConsoleManager
import me.scolastico.tools.web.WebserverRegistration
import me.scolastico.tools.web.admin.AdminPanelInstaller
import me.scolastico.tools.web.admin.etc.AdminPanelLoginData
import me.scolastico.tools.web.admin.etc.AdminPanelSendCommandData
import org.apache.commons.lang3.RandomStringUtils
import java.time.Instant

@Suppress("TooGenericExceptionCaught", "ComplexCondition", "ComplexMethod", "ComplexMethod", "LongMethod")
class AdminPanelAPI {

    companion object {
        private const val LOGIN_TOKEN_LENGTH = 128

        val connections = HashMap<String, DefaultWebSocketServerSession>()

        /**
         * Internal function to get the user from a web call.
         * @param call The web call
         * @return The user if found, null otherwise.
         */
        fun getUser(call: ApplicationCall): String? {
            val token = call.request.cookies["s-admin-auth-token"]?: ""
            var user:String? = null
            if (AdminPanelInstaller.currentConfig.staticTokens.containsValue(token)) {
                for ((u,t) in AdminPanelInstaller.currentConfig.staticTokens) {
                    if (t == token) {
                        user = u
                    }
                }
            }
            if (AdminPanelInstaller.tokens.containsValue(token)) {
                for ((u,t) in AdminPanelInstaller.tokens) {
                    if (t == token) {
                        user = u
                    }
                }
            }
            return user
        }

        fun executeCommand(command: String, user: String): Boolean {
            val cmd = command.split(" ")[0]
            var hasPermission = false
            for (permission in AdminPanelInstaller.currentConfig.permissions[user]?: ArrayList()) {
                val allowAll = permission == "*"
                val isExactCommand = permission == cmd
                val startsWith = permission.startsWith("*") &&
                        cmd.endsWith(permission.replaceFirst("*", ""))
                val endsWith = permission.endsWith("*") &&
                        cmd.startsWith(permission.substring(0, permission.length - 1))
                if (allowAll || isExactCommand || startsWith || endsWith) {
                    hasPermission = true
                    break
                }
            }
            if (hasPermission) {
                println(AdminPanelInstaller.prefix().fgYellow()
                    .a("User '$user' executed command '$command'.")
                    .fgDefault())
                try {
                    ConsoleManager.runCommand(command)
                } catch (ignored: Throwable) {
                    println(AdminPanelInstaller.prefix().fgRed()
                        .a("Something went wrong while executing command '$command'...")
                        .fgDefault())
                }
                return true
            } else {
                println(AdminPanelInstaller.prefix().fgYellow()
                    .a("User '$user' try'd to executed command '$command' " +
                            "but hasn't enough permissions to do so.")
                    .fgDefault())
            }
            return false
        }
    }

    /**
     * WebserverManager compatible module for installing the API of s.Admin.
     */
    @WebserverRegistration
    fun Application.moduleAdminPanelAPI() {
        routing {
            post("/.admin/api/auth/login") {
                val data = call.receive<AdminPanelLoginData>()
                if (AdminPanelInstaller.currentConfig.user.containsKey(data.username.lowercase())) {
                    if (AdminPanelInstaller.verifier
                            .hash(AdminPanelInstaller.currentConfig.user[data.username.lowercase()])
                            .password(data.password.toByteArray())
                            .verifyEncoded()
                    ) {
                        println(AdminPanelInstaller.prefix().fgYellow()
                            .a("User '${data.username.lowercase()}' logged in " +
                                    "from '${call.request.origin.remoteHost}'.")
                            .fgDefault())
                        lateinit var token: String
                        do {
                            token = RandomStringUtils.randomAlphanumeric(LOGIN_TOKEN_LENGTH)
                        } while (
                            AdminPanelInstaller.currentConfig.staticTokens.containsValue(token) ||
                                    AdminPanelInstaller.tokens.containsValue(token)
                        )
                        AdminPanelInstaller.tokens[data.username.lowercase()] = token
                        AdminPanelInstaller.tokenDate[token] = Instant.now()
                        call.response.cookies.append(
                            "s-admin-auth-token",
                            token,
                            path = "/.admin",
                            secure = AdminPanelInstaller.currentConfig.secureCookie
                        )
                        call.respond(HttpStatusCode.OK)
                    } else call.respond(HttpStatusCode.Forbidden)
                } else call.respond(HttpStatusCode.Forbidden)
            }
            get("/.admin/api/auth/logout") {
                val user = getUser(call)
                if (user != null) {
                    val token = call.request.cookies["s-admin-auth-token"]
                    if (!AdminPanelInstaller.currentConfig.staticTokens.containsValue(token)) {
                        AdminPanelInstaller.tokenDate.remove(token)
                        AdminPanelInstaller.tokens.remove(user)
                        if (connections.containsKey(user)) {
                            try {
                                val con = connections[user]!!
                                connections.remove(user)
                                con.close(CloseReason(CloseReason.Codes.NORMAL, "logout"))
                            } catch (ignored: Throwable) {}
                        }
                        call.response.cookies.appendExpired("s-admin-auth-token")
                        println(AdminPanelInstaller.prefix().fgYellow()
                            .a("User '$user' logged out.")
                            .fgDefault())
                        call.respond(HttpStatusCode.OK)
                    } else call.respond(HttpStatusCode.BadRequest)
                } else call.respond(HttpStatusCode.Unauthorized)
            }
            get("/.admin/api/status") {
                val user = getUser(call)
                if (user != null) {
                    call.respond(HttpStatusCode.OK)
                } else call.respond(HttpStatusCode.Unauthorized)
            }
            post("/.admin/api/console/send") {
                val data = call.receive<AdminPanelSendCommandData>()
                val user = getUser(call)
                if (user != null) {
                    if (executeCommand(data.command, user)) {
                        call.respond(HttpStatusCode.OK)
                    } else {
                        call.respond(HttpStatusCode.Forbidden)
                    }
                } else call.respond(HttpStatusCode.Unauthorized)
            }
            get("/.admin/api/console/history") {
                val user = getUser(call)
                if (user != null) {
                    call.respond(ConsoleManager.getLastLogLines())
                } else call.respond(HttpStatusCode.Unauthorized)
            }
            webSocket("/.admin/api/console/live") {
                val user = getUser(call)
                if (user == null) {
                    send("unauthorized")
                    close(CloseReason(CloseReason.Codes.NORMAL, "unauthorized"))
                    return@webSocket
                }
                connections[user] = this
                for (frame in incoming) {
                    frame as? Frame.Text ?: continue
                    executeCommand(frame.readText(), user)
                }
            }
            intercept(ApplicationCallPipeline.Features) {
                if (call.request.path() == "/.admin/api/console/live") {
                    val user = getUser(call)
                    if (user == null) {
                        call.respond(HttpStatusCode.Unauthorized, "unauthorized")
                        finish()
                        return@intercept
                    }
                    if (connections.containsKey(user)) {
                        try {
                            val con = connections[user]!!
                            connections.remove(user)
                            con.send("logged in from else where")
                            con.close(CloseReason(
                                CloseReason.Codes.NORMAL,
                                "logged in from else where"
                            ))
                        } catch (ignored: Throwable) {}
                    }
                }
            }
        }
    }

}
