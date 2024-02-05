package cafe.nes.routes

import cafe.nes.dao.messageSvc
import cafe.nes.models.Message
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.messageRouting() {
    route("/message") {

        get("/") {
            call.respond(messageSvc.getMessages())
        }

        post("/") {
            val received = call.receive<Message>()

            val msg = messageSvc.boardMessage(msg = received)
            if (msg == null) {
                call.respond(HttpStatusCode.BadRequest, "Message is required")
                return@post
            }

            call.respond(msg)
        }

        delete("/{id}") {
            val id = call.parameters["id"]?.toInt()
            if (id == null) {
                call.respond(HttpStatusCode.BadRequest, "id is required")
                return@delete
            }

            val res = messageSvc.deleteMessage(id)
            call.respond(res)
        }
    }
}
