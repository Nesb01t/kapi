package cafe.nes.plugins

import cafe.nes.dao.dao
import cafe.nes.models.User
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureRouting() {
    routing {
        get("/") {
            call.respondText(dao.getAllUsers().toString())
        }

        post("/") {
            val received = call.receive<User>()
            val user = dao.addNewUser(user = received)
            call.respondText(user.toString())
        }
    }
}
