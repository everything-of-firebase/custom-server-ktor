package app.pwdr.plugins

import app.pwdr.model.User
import io.ktor.server.routing.*
import io.ktor.http.*
import io.ktor.server.http.content.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.response.*

/*fun Route.staticContent() {
    static {
        resource("/", "index.html")
        resource("*", "index.html")
        static("static") {
            resources("static")
        }
    }
}*/

fun Application.configureRouting() {
    install(IgnoreTrailingSlash)

    /*intercept(ApplicationCallPipeline.Setup) {
        if (call.request.path() == "/admin/book") {
            call.respondText {
                "intercept book"
            }
            // Truncate the route response. If there is no finish() function,
            // the route /book will still respond to the processing, and the pipeline will be unwritable.
            return@intercept finish()
        }
    }*/

    /*routing {
        get("/") {
            call.respondText("Hello World!")
        }
        // Static plugin. Try to access `/static/index.html`
        static("/static") {
            resources("static")
        }
    }*/

    routing {
        get("/") {
            call.respond(HttpStatusCode.OK, "I'm working just fine, thanks!")
        }

        static("static") {
            resources("static")
            defaultResource("index.html", "static")
        }

        authenticate {
            get("/authenticated") {
                call.respond(HttpStatusCode.OK, "My name is ${call.principal<User>()?.username}, and I'm authenticated!")
            }
        }
    }
}