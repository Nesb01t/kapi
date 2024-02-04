package cafe.nes.dao

import cafe.nes.dao.DatabaseSingleton.dbQuery
import cafe.nes.models.Message
import cafe.nes.models.Messages
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq

class MessageDAOImpl : MessageDAO {
    private fun resultRowToMessage(row: ResultRow) = Message(
        id = row[Messages.id],
        content = row[Messages.content],
        userId = row[Messages.userId]
    )

    override suspend fun getMessages(): List<Message> = dbQuery {
        Messages.selectAll().map { resultRowToMessage(it) }
    }

    override suspend fun boardMessage(msg: Message): Message? = dbQuery {
        val insert = Messages.insert {
            it[content] = msg.content
            it[userId] = msg.userId
        }
        insert.resultedValues?.singleOrNull()?.let { resultRowToMessage(it) }
    }

    override suspend fun deleteMessage(id: Int): Boolean = dbQuery {
        Messages.deleteWhere { Messages.id eq id } > 0
    }
}

val messageSvc: MessageDAO = MessageDAOImpl()