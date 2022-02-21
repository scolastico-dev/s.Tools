package me.scolastico.tools.web.admin.web

import io.ktor.server.application.*
import io.ktor.server.http.content.*
import io.ktor.server.routing.*
import me.scolastico.tools.web.WebserverRegistration

class AdminPanelFrontend {

    /**
     * WebserverManager compatible module for installing the frontend.
     */
    @WebserverRegistration
    fun Application.moduleAdminPanelFrontend() {
        routing {
            static("/.admin/") {
                staticBasePackage = "s-admin-web-files"
                defaultResource("200.html")
                resources(".")
            }
        }
    }

}
