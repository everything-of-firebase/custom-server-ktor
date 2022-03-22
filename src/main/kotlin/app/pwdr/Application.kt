package app.pwdr

import app.pwdr.config.firebase.FirebaseAdmin
import app.pwdr.plugins.configureCallLogging
import app.pwdr.plugins.configureRouting
import app.pwdr.plugins.configureSecurity
import app.pwdr.plugins.configureSerialization
import com.google.auth.oauth2.GoogleCredentials
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseToken
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.routing.*
import io.ktor.util.logging.*

fun main() {
    val log = KtorSimpleLogger("main");

    FirebaseAdmin.init()

    // var firebaseToken: FirebaseToken = FirebaseAuth.getInstance().verifyIdToken("access_token")

    /*val listUsersPage: ListUsersPage = FirebaseAuth.getInstance().listUsers(null)
    listUsersPage.values.map { userRecord: ExportedUserRecord? ->
        log.info("Email: {}", userRecord!!.email.toString())

        val instant = Instant.ofEpochMilli(userRecord.userMetadata.creationTimestamp)
        val zonedDateTime: ZonedDateTime = ZonedDateTime.ofInstant(instant, ZoneOffset.systemDefault())
        val formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss.SSS")
        val createdDate = formatter.format(zonedDateTime)

        log.info("Created Date: {}", createdDate)
    }*/

    embeddedServer(Netty, port = 8080, host = "localhost") {
        configureSecurity()
        configureRouting()
        configureSerialization()
        configureCallLogging()
    }.start(wait = true)
}