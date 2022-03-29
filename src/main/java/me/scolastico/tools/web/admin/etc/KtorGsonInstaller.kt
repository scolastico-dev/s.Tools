package me.scolastico.tools.web.admin.etc

import io.ktor.application.*
import io.ktor.features.*
import io.ktor.gson.*
import me.scolastico.tools.web.WebserverRegistration

class KtorGsonInstaller {

    /**
     * WebserverManager compatible module for installing gson (object <-> json) support into ktor.
     */
    @WebserverRegistration
    fun Application.moduleGsonInstaller() {
        install(ContentNegotiation) {
            gson {
                disableHtmlEscaping()
            }
        }
    }

}
