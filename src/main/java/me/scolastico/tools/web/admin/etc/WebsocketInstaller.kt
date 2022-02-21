package me.scolastico.tools.web.admin.etc

import io.ktor.server.application.*
import io.ktor.websocket.*
import me.scolastico.tools.web.WebserverRegistration
import java.time.Duration

class WebsocketInstaller {

    companion object {
        var PING_PERIOD: Long = 15
        var TIMEOUT_PERIOD: Long = 15
    }

    /**
     * WebserverManager compatible module for installing websocket support into ktor.
     */
    @WebserverRegistration
    fun Application.moduleWebsocketInstaller() {
        install(io.ktor.server.websocket.WebSockets) {
            pingPeriod = Duration.ofSeconds(PING_PERIOD)
            timeout = Duration.ofSeconds(TIMEOUT_PERIOD)
            maxFrameSize = Long.MAX_VALUE
            masking = false
        }
    }

}
