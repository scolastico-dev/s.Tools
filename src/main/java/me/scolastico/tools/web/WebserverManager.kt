package me.scolastico.tools.web

import com.typesafe.config.ConfigFactory
import com.typesafe.config.ConfigValueFactory
import io.ktor.config.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import org.slf4j.LoggerFactory
import java.io.File
import java.security.KeyStore
import kotlin.reflect.full.declaredFunctions
import kotlin.reflect.full.hasAnnotation

class WebserverManager(
    private val listeningPort: Int = 8080,
    private val listeningHost: String = "127.0.0.1",
    private val sslPort: Int? = null,
    private val sslKeyStoreFile: File? = null,
    private val sslKeyStore: KeyStore? = null,
    private val sslKeyAlias: String = "ssl",
    private val sslKeyPassword: String = "password",
    private val sslStorePassword: String = "password",
    var ktorConfigObjects: HashMap<String, Any> = HashMap(),
    var applicationConfigObjects: HashMap<String, Any> = HashMap(),
) {

    private val modules = ArrayList<String>()
    private var server: NettyApplicationEngine? = null

    /**
     * Start the WebserverManager.
     * @return self
     */
    fun start():WebserverManager {
        if (server == null) {
            val sslEnabled = sslKeyStore != null && sslPort != null && sslKeyStoreFile != null
            applicationConfigObjects["modules"] = ConfigValueFactory.fromIterable(modules.toList())
            ktorConfigObjects["application"] = ConfigValueFactory.fromMap(applicationConfigObjects)
            val c = ConfigFactory.load().withValue("ktor", ConfigValueFactory.fromMap(ktorConfigObjects))
            server = embeddedServer(Netty, environment = applicationEngineEnvironment {
                log = LoggerFactory.getLogger("ktor.application")
                config = HoconApplicationConfig(c)
                if (!sslEnabled || sslPort != listeningPort) {
                    connector {
                        port = listeningPort
                        host = listeningHost
                    }
                }
                if (sslEnabled) {
                    sslConnector(
                        keyStore = sslKeyStore!!,
                        keyAlias = sslKeyAlias,
                        keyStorePassword = { sslStorePassword.toCharArray() },
                        privateKeyPassword = { sslKeyPassword.toCharArray() }
                    ) {
                        port = sslPort!!
                        keyStorePath = sslKeyStoreFile
                        host = listeningHost
                    }
                }
            }).start(wait = false)
        }
        return this
    }

    /**
     * Stop the WebserverManager.
     * @return self
     */
    fun stop(gracePeriodMillis: Long = 1000, timeoutMillis: Long = 5000):WebserverManager {
        if (server != null) {
            server!!.stop(gracePeriodMillis, timeoutMillis)
            server = null
        }
        return this
    }

    /**
     * Register and module into the webserver manager.
     * @param module Object containing the module with the WebserverRegistration annotation.
     * @return self
     */
    fun registerModule(module: Any):WebserverManager {
        for (function in module::class.declaredFunctions) {
            if (function.hasAnnotation<WebserverRegistration>()) {
                modules.add("${module.javaClass.name}.${function.name}")
            }
        }
        return this
    }

}
