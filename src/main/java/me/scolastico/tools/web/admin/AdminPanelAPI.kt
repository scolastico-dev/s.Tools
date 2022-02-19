package me.scolastico.tools.web.admin

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.routing.*
import io.ktor.server.websocket.*
import io.ktor.websocket.*
import me.scolastico.tools.web.WebserverRegistration
import java.time.Duration

class AdminPanelAPI {

    @WebserverRegistration
    fun Application.moduleAdminPanel() {
        routing {
            post("/.admin/api/auth/login") {

            }
            get("/.admin/api/auth/logout") {

            }
            get("/.admin/api/status") {

            }
            post("/.admin/api/console/send") {

            }
            get("/.admin/api/console/history") {

            }
            webSocket("/.admin/api/console/live") {
                
            }
        }
    }

}