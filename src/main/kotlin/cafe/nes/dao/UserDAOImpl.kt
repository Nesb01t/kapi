package cafe.nes.dao

import cafe.nes.dao.DatabaseSingleton.dbQuery
import cafe.nes.models.User
import cafe.nes.models.Users
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq

class UserDAOImpl : UserDAO {
    private fun resultRowToUser(row: ResultRow) = User(
        id = row[Users.id],
        no = row[Users.no],
        name = row[Users.name],
        password = row[Users.password],
        isAdmin = row[Users.isAdmin],
        email = row[Users.email],
        phone = row[Users.phone],
        qq = row[Users.qq],
        wechat = row[Users.wechat],
        className = row[Users.className],
        roomName = row[Users.roomName],
        address = row[Users.address],
        birthPlace = row[Users.birthPlace],
        selfResume = row[Users.selfResume],
        adminResume = row[Users.adminResume]
    )

    override suspend fun getAllUsers(): List<User> = dbQuery {
        Users.selectAll().map { resultRowToUser(it).withoutPassword() }
    }

    override suspend fun getUser(id: Int): User? = dbQuery {
        Users.select { Users.id eq id }.map { resultRowToUser(it) }.singleOrNull()
    }

    override suspend fun getUserByNo(no: Int): User? = dbQuery {
        Users.select { Users.no eq no }.map { resultRowToUser(it) }.singleOrNull()
    }

    override suspend fun addNewUser(user: User): User? = dbQuery {
        val insert = Users.insert {
            it[no] = user.no
            it[name] = user.name
            it[password] = user.password
            it[isAdmin] = user.isAdmin
            it[email] = user.email
            it[phone] = user.phone
            it[qq] = user.qq
            it[wechat] = user.wechat
            it[className] = user.className
            it[roomName] = user.roomName
            it[address] = user.address
            it[birthPlace] = user.birthPlace
            it[selfResume] = user.selfResume
            it[adminResume] = user.adminResume
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
                it[isAdmin] = user.isAdmin
                it[phone] = user.phone
                it[qq] = user.qq
                it[wechat] = user.wechat
                it[className] = user.className
                it[roomName] = user.roomName
                it[address] = user.address
                it[birthPlace] = user.birthPlace
                it[selfResume] = user.selfResume
                it[adminResume] = user.adminResume
            }
            updatedRowCount > 0
        } else false
    }
}

val userSvc: UserDAO = UserDAOImpl()