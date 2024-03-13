package cafe.nes.dao

import cafe.nes.dao.DatabaseSingleton.dbQuery
import cafe.nes.models.Avatars
import cafe.nes.models.User
import cafe.nes.models.Users
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.statements.api.ExposedBlob
import java.io.File
import java.sql.Blob
import javax.sql.rowset.serial.SerialBlob

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
        adminResume = row[Users.adminResume],
        birthday = row[Users.birthday],
        sex = row[Users.sex],
        avatar = row[Users.avatar]
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
            it[sex] = user.sex
            it[birthday] = user.birthday
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
                it[sex] = user.sex
                it[birthday] = user.birthday
            }
            updatedRowCount > 0
        } else false
    }

    override suspend fun uploadAvatar(id: Int, file: File): String {
        // check if user exists
        getUser(id) ?: return "User not found"

        // load file
        val bytes = file.readBytes()
        val updated = dbQuery {
            val avatar = Avatars.insert {
                it[fileName] = file.name
                it[bin] = bytes
            }
            Users.update({ Users.id eq id }) {
                it[Users.avatar] = avatar[Avatars.id].toString()
            }
        }

        // return
        if (updated > 0) {
            return "Avatar uploaded"
        }
        return "Avatar not uploaded"
    }

    override suspend fun getAvatar(id: Int): Blob? = dbQuery {
        val avatarId = Users.select { Users.id eq id }.singleOrNull()?.get(Users.avatar) ?: return@dbQuery null
        val blob =
            Avatars.select { Avatars.id eq avatarId.toInt() }.singleOrNull()?.get(Avatars.bin) ?: return@dbQuery null
        SerialBlob(blob)
    }
}

val userSvc: UserDAO = UserDAOImpl()
