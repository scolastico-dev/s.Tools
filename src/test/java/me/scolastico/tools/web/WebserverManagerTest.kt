package me.scolastico.tools.web

import io.ktor.application.*
import io.ktor.http.*
import io.ktor.network.tls.certificates.*
import io.ktor.response.*
import io.ktor.routing.*
import java.io.File

class WebserverManagerTest {

    companion object {

        @JvmStatic
        fun main(args: Array<String>) {
            val keyStoreFile = File("keystore.jks")
            val keystore = generateCertificate(
                file = keyStoreFile,
                keyAlias = "ssl",
                keyPassword = "foobar",
                jksPassword = "foobar"
            )
            val web = WebserverManager(
                8080,
                "localhost",
                8080,
                sslKeyStoreFile = keyStoreFile,
                sslKeyStore = keystore,
                sslKeyPassword = "foobar",
                sslStorePassword = "foobar"
            )
            web.registerModule(WebserverManagerTest())
            web.start()
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