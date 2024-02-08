package cafe.nes.plugins

import cafe.nes.models.User
import com.auth0.jwt.JWT
import com.auth0.jwt.JWTVerifier
import com.auth0.jwt.algorithms.Algorithm
import io.ktor.server.auth.*
import io.ktor.server.application.*
import io.ktor.server.auth.jwt.*
import java.io.File
import java.util.*

fun Application.configureAuthentication() {
    install(Authentication) {
        jwt {
            realm = "ktor.io"
            verifier(JwtConfig.verifier)
        }
    }
}

object JwtConfig {
    private val secret: String = run {
        val properties = Properties()
        properties.load(File("./secret.properties").inputStream())
        properties.getProperty("jwt.secret")
    }

    private val issuer = "ktor.io"
    private val validityInMs = 36_000_00 * 7 * 24 // 1 week
    private val algorithm = Algorithm.HMAC512(secret)

    val verifier: JWTVerifier = JWT.require(algorithm).withIssuer(issuer).build()

    fun makeToken(user: User): String =
        JWT.create().withSubject("Authentication").withIssuer(issuer).withClaim("name", user.name)
            .withClaim("id", user.id).withClaim("isAdmin", user.isAdmin)
            .withExpiresAt(Date(System.currentTimeMillis() + validityInMs)).sign(algorithm)

    fun getUserIdFromToken(token: String): Int {
        val decodedToken = JWT.decode(token)
        return decodedToken.getClaim("id").asInt()
    }
}