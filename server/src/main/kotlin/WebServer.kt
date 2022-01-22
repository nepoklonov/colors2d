import io.ktor.application.*
import io.ktor.features.*
import io.ktor.html.*
import io.ktor.http.content.*
import io.ktor.routing.*
import kotlinx.coroutines.*
import kotlinx.html.body
import kotlinx.html.div
import kotlinx.html.id
import kotlinx.html.script
import model.step
import rpc.rpc
import services.SquareService
import services.square

fun Application.module() {
    CoroutineScope(Job()).launch {
        while (true) {
            square.step()
        }
    }


    install(XForwardedHeaderSupport)

    routing {
        resource("client.js")
        get("/") {
            call.respondHtml {
                body {
                    div {
                        id = "react-app"
                    }
                    script(src = "/client.js") {}
                }
            }
        }

        static("static") {
            resources("/")
        }

        route("/api") {
            rpc(SquareService::class)
        }
    }
}