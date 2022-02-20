package me.scolastico.tools.web.tools

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.plugins.*
import io.ktor.server.response.*

class JsonStatusPagesModule {

    fun Application.moduleJsonStatusPages() {
        install(StatusPages) {
            status(HttpStatusCode.NotFound) { call,_ ->
                call.respond(DefaultAnswerDataHolder("error", HttpStatusCode.NotFound.value))
            }
        }
    }

    data class DefaultAnswerDataHolder(
        val status: String = "ok",
        val code: Int = 200,
        val error: String? = null
    )

}