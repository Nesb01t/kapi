package cafe.nes.dao

import cafe.nes.models.RegisterDTO
import cafe.nes.models.User

class AuthDAOImpl : AuthDAO {
    override suspend fun registerNewUser(dto: RegisterDTO): User? {
        val user = User(
            no = dto.no, name = dto.name, password = dto.password
        )
        val res = userSvc.addNewUser(user)
        return res;
    }
}

val authSvc = AuthDAOImpl()