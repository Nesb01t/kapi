package cafe.nes

import cafe.nes.dao.DatabaseMigration
import cafe.nes.dao.DatabaseSingleton
import cafe.nes.plugins.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*

fun main() {
    embeddedServer(Netty, port = 8080, host = "0.0.0.0", module = Application::module).start(wait = true)
}

fun Application.module() {
    DatabaseSingleton.init()
    DatabaseMigration.migrate()
    configureRouting()
    configureSerialization()
    configureCORS()
    configureAuthentication()
}
