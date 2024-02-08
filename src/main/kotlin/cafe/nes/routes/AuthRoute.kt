package cafe.nes.routes

import cafe.nes.dao.authSvc
import cafe.nes.dao.userSvc
import cafe.nes.models.LoginDTO
import cafe.nes.models.RegisterDTO
import cafe.nes.models.User
import cafe.nes.plugins.JwtConfig
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*


fun Route.authRouting() {
    route("/auth") {
        post("/register") {
            val received = call.receive<RegisterDTO>()
            val user = userSvc.getUserByNo(received.no)

            if (user != null) {
                call.respond(HttpStatusCode.BadRequest, "User already exists")
                return@post
            }

            val newUser = authSvc.registerNewUser(received)
            if (newUser == null) {
                call.respond(HttpStatusCode.BadGateway, "User create failed")
                return@post
            }

            call.respond(newUser.withoutPassword())
        }


        post("/login") {
            val credential = call.receive<LoginDTO>()
            val user = userSvc.getUserByNo(credential.no)

            if (user == null) {
                call.respond(HttpStatusCode.NotFound, "User not found")
                return@post
            }

            if (credential.password != user.password) {
                call.respond(HttpStatusCode.Unauthorized, "Password is incorrect")
                return@post
            }

            val token = JwtConfig.makeToken(user)
            call.respond(mapOf("token" to token))
        }
    }
}
