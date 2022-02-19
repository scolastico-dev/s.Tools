package me.scolastico.tools.web.admin

import io.ktor.server.application.*
import io.ktor.websocket.*
import me.scolastico.tools.web.WebserverRegistration
import java.time.Duration

class WebsocketInstaller {

    @WebserverRegistration
    fun Application.moduleWebsocketInstaller() {
        install(io.ktor.server.websocket.WebSockets) {
            pingPeriod = Duration.ofSeconds(15)
            timeout = Duration.ofSeconds(15)
            maxFrameSize = Long.MAX_VALUE
            masking = false
        }
    }

}