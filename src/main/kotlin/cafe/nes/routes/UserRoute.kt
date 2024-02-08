package cafe.nes.routes

import cafe.nes.dao.userSvc
import cafe.nes.models.User
import cafe.nes.plugins.JwtConfig
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.userRouting() {
    route("/user") {

        get("/") {
            call.respond(userSvc.getAllUsers())
        }

        get("/me") {
            val header = call.request.headers["Authorization"]
            if (header != null && header.startsWith("Bearer ")) {
                val token = header.substringAfter("Bearer ")
                val id = JwtConfig.getUserIdFromToken(token)
                val user = userSvc.getUser(id)
                if (user != null) {
                    call.respond(user.withoutPassword())
                } else {
                    call.respond(HttpStatusCode.NotFound, "User not found")
                }
            } else {
                call.respond(HttpStatusCode.Unauthorized, "Invalid token")
            }
        }
        get("/{id}") {
            val id = call.parameters["id"]?.toInt()
            if (id == null) {
                call.respond(HttpStatusCode.BadRequest, "id is required")
                return@get
            }

            val user = userSvc.getUser(id)
            if (user == null) {
                call.respond(HttpStatusCode.NotFound, "User not found")
                return@get
            }

            call.respond(user.withoutPassword())
        }

        post("/") {
            val received = call.receive<User>()

            val user = userSvc.addNewUser(user = received)
            if (user == null) {
                call.respond(HttpStatusCode.BadRequest, "User already exists")
                return@post
            }

            call.respond(user.withoutPassword())
        }

        patch("/") {
            val received = call.receive<User>()

            val user = userSvc.editUser(user = received)

            call.respond(user)
        }

        delete("/{id}") {
            val id = call.parameters["id"]?.toInt()
            if (id == null) {
                call.respond(HttpStatusCode.BadRequest, "id is required")
                return@delete
            }

            val flag = userSvc.deleteUser(id)
            call.respond(flag)
        }
    }
}
