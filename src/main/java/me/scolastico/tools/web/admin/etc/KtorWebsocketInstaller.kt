package me.scolastico.tools.web.admin.etc

import io.ktor.application.*
import io.ktor.http.cio.websocket.*
import io.ktor.websocket.*
import me.scolastico.tools.web.WebserverRegistration
import java.time.Duration

class KtorWebsocketInstaller {

    companion object {
        var PING_PERIOD: Long = 15
        var TIMEOUT_PERIOD: Long = 15
    }

    /**
     * WebserverManager compatible module for installing websocket support into ktor.
     */
    @WebserverRegistration
    fun Application.moduleWebsocketInstaller() {
        install(WebSockets) {
            pingPeriod = Duration.ofSeconds(PING_PERIOD)
            timeout = Duration.ofSeconds(TIMEOUT_PERIOD)
            maxFrameSize = Long.MAX_VALUE
            masking = false
        }
    }

}
