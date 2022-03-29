package me.scolastico.tools.web.admin.etc

import io.ktor.http.cio.websocket.*
import kotlinx.coroutines.runBlocking
import me.scolastico.tools.routine.Routine
import me.scolastico.tools.routine.RoutineAnswer
import me.scolastico.tools.web.admin.web.AdminPanelAPI

class NewLogLineEventHandler: Routine {

    override fun execute(objectMap: HashMap<String, Any>?): RoutineAnswer {
        for (con in AdminPanelAPI.connections.values) {
            Thread {
                runBlocking {
                    try {
                        con.send((objectMap!!["line"] ?: "") as String)
                    } catch (ignored: Exception) {
                    }
                }
            }.start()
        }
        return RoutineAnswer(objectMap)
    }

}
