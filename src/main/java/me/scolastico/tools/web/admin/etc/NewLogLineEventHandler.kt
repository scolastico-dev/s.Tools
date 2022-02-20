package me.scolastico.tools.web.admin.etc

import io.ktor.websocket.*
import kotlinx.coroutines.runBlocking
import me.scolastico.tools.routine.Routine
import me.scolastico.tools.routine.RoutineAnswer
import me.scolastico.tools.web.admin.web.AdminPanelAPI

class NewLogLineEventHandler: Routine {

    override fun execute(objectMap: HashMap<String, Any>?): RoutineAnswer {
        for ((key,con) in AdminPanelAPI.connections) {
            if (AdminPanelAPI.authConnections.contains(key)) {
                Thread(Runnable {
                    try {
                        runBlocking {
                            con.send((objectMap!!["line"] ?: "") as String)
                        }
                    } catch (ignored: Exception) {}
                }).start()
            }
        }
        return RoutineAnswer(objectMap)
    }

}