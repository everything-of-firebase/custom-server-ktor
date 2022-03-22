package app.pwdr.model

import io.ktor.server.auth.*

data class User(
    val _id: String,
    val username: String
) : Principal