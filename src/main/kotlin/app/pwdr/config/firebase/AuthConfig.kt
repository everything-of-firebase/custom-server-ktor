package app.pwdr.config.firebase

import app.pwdr.auth.FirebaseAuthenticationProvider
import app.pwdr.model.User
import kotlinx.coroutines.runBlocking

/**
 * Configuration for [FirebaseAuthenticationProvider].
 */
object AuthConfig {
    fun FirebaseAuthenticationProvider.Configuration.configure() {
        principal = { firebaseToken ->
            User(firebaseToken.uid, firebaseToken.name ?: "anonymous")
            // this is where you'd make a db call to fetch your User profile
            // runBlocking { }
        }
    }
}