package cafe.nes.dao

import cafe.nes.models.RegisterDTO
import cafe.nes.models.User

interface AuthDAO {
    suspend fun registerNewUser(dto: RegisterDTO): User?
}