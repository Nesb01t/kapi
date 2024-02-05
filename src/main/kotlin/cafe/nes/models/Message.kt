package cafe.nes.models

import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.*
import java.time.LocalDateTime

@Serializable
data class Message(
    val id: Int? = null,
    val content: String,
    val userId: Int? = null,
    val time: String? = LocalDateTime.now().toString(),
)

object Messages : Table() {
    val id = integer("id").autoIncrement()
    val content = varchar("content", 255)
    val userId = integer("userId").references(Users.id).nullable()
    val time = varchar("time", 255).default(LocalDateTime.now().toString())

    override val primaryKey = PrimaryKey(Users.id)
}