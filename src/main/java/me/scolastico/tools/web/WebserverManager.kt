package me.scolastico.tools.web

import com.typesafe.config.ConfigFactory
import com.typesafe.config.ConfigValueFactory
import io.ktor.server.config.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import org.slf4j.LoggerFactory
import java.util.Timer
import kotlin.collections.HashMap
import kotlin.concurrent.timerTask
import kotlin.reflect.full.declaredFunctions
import kotlin.reflect.full.hasAnnotation

class WebserverManager(private val listeningPort: Int = 8080, private val listeningHost: String = "127.0.0.1") {

    private val routes = ArrayList<String>()
    private val ktorConfigObjects = HashMap<String, Any>()
    private val applicationConfigObjects = HashMap<String, Any>()
    private var server: NettyApplicationEngine? = null

    fun start():WebserverManager {
        if (server == null) {
            applicationConfigObjects["modules"] = ConfigValueFactory.fromIterable(routes.toList())
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
                routes.add("${module.javaClass.name}.${function.name}")
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

}

fun main() {
    val web = WebserverManager().registerModule(FilePageHandler()).start()
    Timer().schedule(timerTask {
        println("stopping")
        web.stop()
        println("stopped")
    }, 10000)
    Timer().schedule(timerTask {
        println("starting")
        web.start()
    }, 20000)
}
