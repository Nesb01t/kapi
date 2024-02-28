package cafe.nes.models

import io.ktor.server.auth.*
import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.*
import java.io.File

@Serializable
data class User(
    val id: Int? = null,
    val no: Int,
    val name: String,

    // auth
    val password: String,
    val isAdmin: Boolean = false,

    // 联系方式
    val email: String? = null,
    val phone: String? = null,
    val qq: String? = null,
    val wechat: String? = null,

    // 个人信息
    val sex: String? = null,
    val birthday: String? = null,
    val avatar: ByteArray? = null,

    // 寝室班级号
    val className: String? = null,
    val roomName: String? = null,

    // 地址
    val address: String? = null,
    val birthPlace: String? = null,

    // 自我和管理员留言
    val selfResume: String? = null,
    val adminResume: String? = null,
) : Principal {
    fun withoutPassword(): User {
        return this.copy(password = "")
    }
}

object Users : Table() {
    val id = integer("id").autoIncrement()
    val no = integer("no")
    val name = varchar("name", 50)

    // auth
    val password = varchar("password", 255)
    val isAdmin = bool("isAdmin")

    // 个人信息
    val sex = varchar("sex", 10).nullable()
    val birthday = varchar("birthday", 50).nullable()
    val avatar = binary("avatar").nullable()
    
    // 联系方式
    val email = varchar("email", 50).nullable()
    val phone = varchar("phone", 50).nullable()
    val qq = varchar("qq", 50).nullable()
    val wechat = varchar("wechat", 50).nullable()

    // 寝室班级号
    val className = varchar("className", 50).nullable()
    val roomName = varchar("roomName", 50).nullable()

    // 地址
    val address = varchar("address", 255).nullable()
    val birthPlace = varchar("birthPlace", 255).nullable()

    // 自我和管理员留言
    val selfResume = varchar("selfResume", 255).nullable()
    val adminResume = varchar("adminResume", 255).nullable()

    override val primaryKey = PrimaryKey(id)
}