package me.scolastico.tools.web.admin.web

import io.ktor.application.*
import io.ktor.http.content.*
import io.ktor.routing.*
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
