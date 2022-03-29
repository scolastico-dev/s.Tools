package me.scolastico.tools.web

import io.ktor.application.*
import io.ktor.http.*
import io.ktor.response.*
import io.ktor.routing.*
import me.scolastico.tools.console.ConsoleManager
import me.scolastico.tools.web.admin.AdminPanelInstaller

class AdminPanelTest {

    companion object {

        @JvmStatic
        fun main(args: Array<String>) {
            val web = WebserverManager(
                8080,
                "localhost"
            )
            AdminPanelInstaller.install(web)
            web.registerModule(AdminPanelTest())
            web.start()
            ConsoleManager.enable()
        }

    }

    @WebserverRegistration
    fun Application.moduleTest() {
        routing {
            get("/") {
                call.respond(HttpStatusCode.OK, "Nice")
            }
        }
    }

}
