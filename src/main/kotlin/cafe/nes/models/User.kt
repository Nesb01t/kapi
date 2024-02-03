package cafe.nes.models

import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.*

@Serializable
data class User(val id: Int? = null, val name: String, val email: String)

object Users : Table() {
    val id = integer("id").autoIncrement()
    val name = varchar("name", 50)
    val email = varchar("email", 50)
    override val primaryKey = PrimaryKey(id)
}

val userStorage = mutableListOf<User>()