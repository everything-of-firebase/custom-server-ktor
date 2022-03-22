package app.pwdr

import app.pwdr.plugins.*
import com.google.auth.oauth2.GoogleCredentials
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import com.google.firebase.auth.ExportedUserRecord
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ListUsersPage
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.util.logging.*
import java.time.Instant
import java.time.ZoneOffset
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

fun main() {
    val log = KtorSimpleLogger("main");

    val options: FirebaseOptions = FirebaseOptions.builder()
        .setCredentials(GoogleCredentials.getApplicationDefault())
        .build()
    FirebaseApp.initializeApp(options)

    val listUsersPage: ListUsersPage = FirebaseAuth.getInstance().listUsers(null)
    listUsersPage.values.map { userRecord: ExportedUserRecord? ->
        log.info("Email: {}", userRecord!!.email.toString())

        val instant = Instant.ofEpochMilli(userRecord.userMetadata.creationTimestamp)
        val zonedDateTime: ZonedDateTime = ZonedDateTime.ofInstant(instant, ZoneOffset.systemDefault())
        val formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss.SSS")
        val createdDate = formatter.format(zonedDateTime)

        log.info("Created Date: {}", createdDate)
    }

    embeddedServer(Netty, port = 8080, host = "0.0.0.0") {
        configureRouting()
        configureSecurity()
        configureSerialization()
    }.start(wait = true)
}
