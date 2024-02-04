package cafe.nes.routes

import cafe.nes.dao.userSvc
import cafe.nes.models.User
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.userRouting() {
    route("/user") {

        get("/") {
            call.respondText(userSvc.getAllUsers().toString())
        }

        get("/{id}") {
            val id = call.parameters["id"]?.toInt()
            if (id == null) {
                call.respond(HttpStatusCode.BadRequest, "id is required")
                return@get
            }

            val user = userSvc.getUser(id)
            call.respond(user.toString())
        }

        post("/") {
            val received = call.receive<User>()
            val user = userSvc.addNewUser(user = received)
            call.respondText(user.toString())
        }

        patch("/") {
            val received = call.receive<User>()
            val user = userSvc.editUser(user = received)
            call.respondText(user.toString())
        }

        delete("/{id}") {
            val id = call.parameters["id"]?.toInt()
            if (id == null) {
                call.respond(HttpStatusCode.BadRequest, "id is required")
                return@delete
            }
            val user = userSvc.deleteUser(id)
            call.respondText(user.toString())
        }
    }
}
