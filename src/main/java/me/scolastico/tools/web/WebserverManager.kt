package me.scolastico.tools.web

import com.typesafe.config.ConfigFactory
import com.typesafe.config.ConfigValueFactory
import io.ktor.serialization.gson.*
import io.ktor.server.application.*
import io.ktor.server.config.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.*
import me.scolastico.tools.console.ConsoleManager
import me.scolastico.tools.web.admin.AdminPanelInstaller
import org.slf4j.LoggerFactory
import kotlin.reflect.full.declaredFunctions
import kotlin.reflect.full.hasAnnotation

class WebserverManager(private val listeningPort: Int = 8080, private val listeningHost: String = "127.0.0.1") {

    private val modules = ArrayList<String>()
    private val ktorConfigObjects = HashMap<String, Any>()
    private val applicationConfigObjects = HashMap<String, Any>()
    private var server: NettyApplicationEngine? = null
    private val defaultModule = "me.scolastico.tools.web.WebserverManager.moduleDefault"

    fun start():WebserverManager {
        if (server == null) {
            if (!modules.contains(defaultModule)) modules.add(defaultModule)
            applicationConfigObjects["modules"] = ConfigValueFactory.fromIterable(modules.toList())
            ktorConfigObjects["application"] = ConfigValueFactory.fromMap(applicationConfigObjects)
            val c = ConfigFactory.load().withValue("ktor", ConfigValueFactory.fromMap(ktorConfigObjects))
            server = embeddedServer(Netty, environment = applicationEngineEnvironment {
                log = LoggerFactory.getLogger("ktor.application")
                config = HoconApplicationConfig(c)
                connector {
                    port = listeningPort
                    host = listeningHost
                }
            }).start(wait = false)
        }
        return this
    }

    fun stop(gracePeriodMillis: Long = 1000, timeoutMillis: Long = 5000):WebserverManager {
        if (server != null) {
            server!!.stop(gracePeriodMillis, timeoutMillis)
            server = null
        }
        return this
    }

    fun registerModule(module: Any):WebserverManager {
        for (function in module::class.declaredFunctions) {
            if (function.hasAnnotation<WebserverRegistration>()) {
                modules.add("${module.javaClass.name}.${function.name}")
            }
        }
        return this
    }

    fun addKtorConfigObject(key: String, value: Any):WebserverManager {
        ktorConfigObjects[key] = value
        return this
    }

    fun addApplicationConfigObject(key: String, value: Any):WebserverManager {
        applicationConfigObjects[key] = value
        return this
    }

    fun Application.moduleDefault() {
        install(ContentNegotiation) {
            gson {
                serializeNulls()
                disableHtmlEscaping()
            }
        }
    }

}

fun main() {
    val web = WebserverManager()
    AdminPanelInstaller.install(web)
    ConsoleManager.enable()
    web.start()
}
