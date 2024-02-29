package cafe.nes.routes

import cafe.nes.dao.userSvc
import cafe.nes.models.User
import cafe.nes.plugins.JwtConfig
import io.ktor.http.*
import io.ktor.http.content.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import java.io.File

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

        get("/{id}/avatar") {
            val id = call.parameters["id"]?.toInt()
            if (id == null) {
                call.respond(HttpStatusCode.BadRequest, "id is required")
                return@get
            }

            val img = userSvc.getAvatar(id)
            if (img == null) {
                call.respond(HttpStatusCode.NotFound, "User not found Or Avatar not found")
                return@get
            }

            val bytes = img.getBytes(1, img.length().toInt())
            call.respondBytes(bytes, ContentType.Image.JPEG)
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

        post("/{id}/avatar") {
            val id = call.parameters["id"]?.toInt()
            if (id == null) {
                call.respond(HttpStatusCode.BadRequest, "id is required")
                return@post
            }

            val multipart = call.receiveMultipart()
            println(multipart)
            var file: File? = null

            multipart.forEachPart { part ->
                if (part is PartData.FileItem) {
                    val ext = part.originalFileName?.let { it1 -> File(it1).extension }
                    val tempFile = File.createTempFile("upload_", ".$ext")
                    part.streamProvider().use { its -> tempFile.outputStream().buffered().use { its.copyTo(it) } }
                    file = tempFile
                }
                part.dispose()
            }

            if (file == null) {
                call.respond(HttpStatusCode.BadRequest, "File is required")
                return@post
            }

            val result = userSvc.uploadAvatar(id, file!!)
            call.respondText(result)
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
