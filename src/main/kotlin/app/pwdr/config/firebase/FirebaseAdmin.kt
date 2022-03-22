package app.pwdr.config.firebase

import com.google.auth.oauth2.GoogleCredentials
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import java.io.InputStream

/**
 * Initialization for Firebase application.
 */
object FirebaseAdmin {
    val options: FirebaseOptions = FirebaseOptions.builder()
        .setCredentials(GoogleCredentials.getApplicationDefault())
        .build()

    fun init(): FirebaseApp = FirebaseApp.initializeApp(options)
}