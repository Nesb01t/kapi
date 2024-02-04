package cafe.nes.dao

import cafe.nes.dao.DatabaseSingleton.dbQuery
import cafe.nes.models.User
import cafe.nes.models.Users
import kotlinx.coroutines.runBlocking
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq

class UserDAOImpl : UserDAO {
    private fun resultRowToUser(row: ResultRow) = User(
        id = row[Users.id],
        name = row[Users.name],
        email = row[Users.email]
    )

    override suspend fun getAllUsers(): List<User> = dbQuery {
        Users.selectAll().map { resultRowToUser(it) }
    }

    override suspend fun getUser(id: Int): User? = dbQuery {
        Users.select { Users.id eq id }
            .map { resultRowToUser(it) }
            .singleOrNull()
    }

    override suspend fun addNewUser(user: User): User? = dbQuery {
        val insert = Users.insert {
            it[Users.name] = user.name
            it[Users.email] = user.email
        }
        insert.resultedValues?.singleOrNull()?.let { resultRowToUser(it) }
    }

    override suspend fun deleteUser(id: Int): Boolean = dbQuery {
        Users.deleteWhere { Users.id eq id } > 0
    }

    override suspend fun editUser(user: User): Boolean = dbQuery {
        if (user.id != null) {
            val updatedRowCount = Users.update({ Users.id eq user.id }) {
                it[name] = user.name
                it[email] = user.email
            }
            updatedRowCount > 0
        } else false
    }
}

val dao: UserDAO = UserDAOImpl().apply {
    runBlocking {
        if (getAllUsers().isEmpty()) {
            addNewUser(User(0, "defaultUser", "admin@163.com"))
        }
    }
}