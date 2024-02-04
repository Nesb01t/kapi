package cafe.nes.plugins

import cafe.nes.dao.dao
import cafe.nes.models.User
import cafe.nes.routes.userRouting
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureRouting() {
    routing {
        userRouting()
    }
}
