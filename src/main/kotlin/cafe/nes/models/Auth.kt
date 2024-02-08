package cafe.nes.models

import kotlinx.serialization.Serializable

@Serializable
data class RegisterDTO(val no: Int, val name: String, val password: String)

@Serializable
data class LoginDTO(val no: Int, val password: String)