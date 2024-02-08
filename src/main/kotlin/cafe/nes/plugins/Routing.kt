package cafe.nes.plugins

import cafe.nes.routes.authRouting
import cafe.nes.routes.messageRouting
import cafe.nes.routes.userRouting
import io.ktor.server.application.*
import io.ktor.server.routing.*

fun Application.configureRouting() {
    routing {
        userRouting()
        messageRouting()
        authRouting()
    }
}
