package app.pwdr.auth

import com.google.firebase.ErrorCode
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.FirebaseToken
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.util.logging.*

private val log = KtorSimpleLogger("FirebaseAuthenticationProvider");

class FirebaseAuthenticationProvider internal constructor(config: Configuration) : AuthenticationProvider(config) {
    internal val token: (ApplicationCall) -> String? = config.token
    internal val principle: ((firebaseToken: FirebaseToken) -> Principal?)? = config.principal

    class Configuration internal constructor(name: String?) : AuthenticationProvider.Configuration(name) {
        internal var token: (ApplicationCall) -> String? = { call -> call.request.parseAuthorizationToken() }
        internal var principal: ((firebaseToken: FirebaseToken) -> Principal?)? = null
        internal fun build() = FirebaseAuthenticationProvider(this)
    }
}

fun Authentication.Configuration.firebase(
    name: String? = null,
    configure: FirebaseAuthenticationProvider.Configuration.() -> Unit
) {
    val provider = FirebaseAuthenticationProvider.Configuration(name).apply(configure).build()
    provider.pipeline.intercept(AuthenticationPipeline.RequestAuthentication) { context ->
        try {
            val token = provider.token(call) ?: throw FirebaseAuthException(
                FirebaseException(ErrorCode.UNAUTHENTICATED, "No token could be found", null)
            )

            val firebaseToken: FirebaseToken = FirebaseAuth.getInstance().verifyIdToken(token)

            // NOTE: JWT Claim에 포함된 사용할 수 있는 값
            //       UID = sub
            //       Tenant = tenant
            //       Issuer = iss
            //       Name = name
            //       Picture = picture
            //       Email = email
            //       Email verified = email_verified

            // NOTE: Context의 Principal을 주입하기 위해 콜백 함수를 호출한다.
            provider.principle?.let {
                it.invoke(firebaseToken)?.let {
                        principle -> context.principal(principle)
                }
            }
        } catch (cause: Throwable) {
            val message = if (cause is FirebaseAuthException) {
                "Authentication failed: ${cause.message ?: cause.javaClass.simpleName}"
            } else {
                cause.message ?: cause.javaClass.simpleName
            }
            log.trace(message)
            call.respond(HttpStatusCode.Unauthorized, message)
            context.challenge.complete()
            finish()
        }
    }
    register(provider)
}

fun ApplicationRequest.parseAuthorizationToken(): String? = authorization()?.let {
    it.split(" ")[1]
}