package me.scolastico.tools.web.admin.web

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.plugins.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.websocket.*
import io.ktor.websocket.*
import me.scolastico.tools.console.ConsoleManager
import me.scolastico.tools.web.WebserverRegistration
import me.scolastico.tools.web.admin.AdminPanelInstaller
import me.scolastico.tools.web.admin.etc.AdminPanelLoginData
import me.scolastico.tools.web.admin.etc.AdminPanelSendCommandData
import org.apache.commons.lang3.RandomStringUtils
import java.time.Instant

class AdminPanelAPI {

    companion object {
        val connections = HashMap<String, DefaultWebSocketServerSession>()
        val users = HashMap<String, String>()
        val timeouts = HashMap<String, Instant>()
    }

    @WebserverRegistration
    fun Application.moduleAdminPanel() {
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
                            .a("User '${data.username.lowercase()}' logged in from '${call.request.origin.remoteHost}'.")
                            .fgDefault())
                        var token = ""
                        do {
                            token = RandomStringUtils.randomAlphanumeric(128)
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
                        if (users.containsKey(user)) {
                            try {
                                val id = users[user]!!
                                val con = connections[id]!!
                                users.remove(user)
                                connections.remove(id)
                                timeouts.remove(id)
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
                val cmd = data.command.split(" ")[0]
                val user = getUser(call)
                if (user != null) {
                    var hasPermission = false
                    for (permission in AdminPanelInstaller.currentConfig.permissions[user]?: ArrayList()) {
                        if (permission == "*") {
                            hasPermission = true
                            break
                        } else if (permission == cmd) {
                            hasPermission = true
                            break
                        } else if (permission.startsWith("*")) {
                            if (cmd.endsWith(permission.replaceFirst("*", ""))) {
                                hasPermission = true
                                break
                            }
                        } else if (permission.endsWith("*")) {
                            if (cmd.startsWith(permission.substring(0, permission.length - 1))) {
                                hasPermission = true
                                break
                            }
                        }
                    }
                    if (hasPermission) {
                        println(AdminPanelInstaller.prefix().fgYellow()
                            .a("User '$user' executed command '${data.command}'.")
                            .fgDefault())
                        try {
                            ConsoleManager.runCommand(data.command)
                        } catch (e: Throwable) {
                            println(AdminPanelInstaller.prefix().fgRed()
                                .a("Something went wrong while executing command '${data.command}'...")
                                .fgDefault())
                        }
                        call.respond(HttpStatusCode.OK)
                    } else {
                        println(AdminPanelInstaller.prefix().fgYellow()
                            .a("User '$user' try'd to executed command '${data.command}' but hasn't enough permissions to do so.")
                            .fgDefault())
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
            get("/.admin/api/auth/live/{id}") {
                val user = getUser(call)
                if (user != null) {
                    if (connections.containsKey(call.parameters["id"])) {
                        users[user] = call.parameters["id"]!!
                        timeouts.remove(call.parameters["id"]!!)
                        connections[call.parameters["id"]!!]!!.send("authenticated with user $user")
                        call.respond(HttpStatusCode.OK)
                    } else call.respond(HttpStatusCode.NotFound)
                } else call.respond(HttpStatusCode.Unauthorized)
            }
            webSocket("/.admin/api/console/live") {
                var id = ""
                do {
                    id = RandomStringUtils.randomAlphanumeric(16)
                } while (connections.containsKey(id))
                connections[id] = this
                timeouts[id] = Instant.now()
                send(id)
                for (frame in incoming) {
                    send(frame)
                }
            }
        }
    }

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

}
