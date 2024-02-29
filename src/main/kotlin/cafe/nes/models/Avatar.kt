package cafe.nes.models

import org.jetbrains.exposed.sql.*
import kotlinx.serialization.Serializable
import java.sql.Blob

@Serializable
data class Avatar(
    val id: Int,
    val fileName: String,
    val bin: Blob
)

object Avatars : Table() {
    val id = integer("id").autoIncrement()
    val fileName = varchar("fileName", 255)
    val bin = binary("content", length = 10 * 1048576)

    override val primaryKey = PrimaryKey(id)
}
