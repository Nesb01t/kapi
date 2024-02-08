package cafe.nes.dao

import cafe.nes.models.User

interface UserDAO {
    suspend fun getAllUsers(): List<User>
    suspend fun getUser(id: Int): User?
    suspend fun getUserByNo(no: Int): User?
    suspend fun addNewUser(user: User): User?
    suspend fun deleteUser(id: Int): Boolean
    suspend fun editUser(user: User): Boolean
}