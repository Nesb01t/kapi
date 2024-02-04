package cafe.nes.dao

import cafe.nes.models.Message

interface MessageDAO {
    suspend fun getMessages(): List<Message>
    suspend fun boardMessage(msg: Message): Message?
    suspend fun deleteMessage(id: Int): Boolean
}