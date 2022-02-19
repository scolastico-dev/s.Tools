package me.scolastico.tools.web.admin

import me.scolastico.tools.web.WebserverManager

fun install(webserver: WebserverManager, installWebsocketExtension: Boolean = true) {
    if (installWebsocketExtension) webserver.registerModule(WebsocketInstaller())
    webserver.registerModule(AdminPanelAPI())
    webserver.registerModule(AdminPanelFrontend())
}
