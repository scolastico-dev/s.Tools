package me.scolastico.tools.web

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.application.*
import io.ktor.server.plugins.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.springframework.web.servlet.function.RouterFunctions.route

class FilePageHandler() {
    @WebserverRegistration
    fun Application.module() {
        install(StatusPages) {
            status(HttpStatusCode.NotFound) { call,_ ->
                call.respondText("{\"status\":\"error\",\"error:\":\"not found\"}", ContentType.Application.Json, HttpStatusCode.NotFound)
            }
        }
        routing {
            route("/test2") {
                get {
                    call.respondText("test")
                }
            }
        }
    }
}
