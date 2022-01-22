package rpc

import kotlinx.browser.window
import kotlinx.coroutines.await
import kotlinx.coroutines.withContext
import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.KSerializer
import kotlinx.serialization.json.Json
import org.w3c.fetch.RequestCredentials
import org.w3c.fetch.RequestInit
import kotlin.coroutines.CoroutineContext
import kotlin.js.json

@JsName("encodeURIComponent")
external fun urlEncode(value: String): String

@JsName("decodeURIComponent")
external fun urlDecode(encoded: String): String

class Transport(private val coroutineContext: CoroutineContext) {
    internal suspend fun <T> get(
        url: String,
        deserializationStrategy: KSerializer<T>,
        vararg args: Pair<String, Any>
    ): T {
        return parse(deserializationStrategy, fetch("GET", url, *args))
    }

    private suspend fun fetch(
        method: String,
        shortUrl: String,
        vararg args: Pair<String, Any?>,
    ): String {
        var url = "/api/$shortUrl"
        if (method == "GET" && args.isNotEmpty()) {
            url += "?"
            url += args.joinToString("&", transform = { "${it.first}=${urlEncode(it.second.toString())}" })
        }

        return withContext(coroutineContext) {
            val response = window.fetch(
                url, RequestInit(
                    method = method,
                    headers = json(
                        "Accept" to "application/json; charset=UTF-8",
                        "Content-Type" to "application/json; charset=UTF-8"
                    ),
                    credentials = "same-origin".unsafeCast<RequestCredentials>(),
                    body = if (method == "POST") JSON.stringify(json(*args)).also { console.log(it) } else undefined
                )
            ).await()

            response.text().await()
        }
    }

}

@Suppress("UNCHECKED_CAST")
fun <T> parse(serializationStrategy: DeserializationStrategy<T>, string: String): T {
    return try {
        Json.decodeFromString(serializationStrategy, string)
    } catch (e: Throwable) {
        console.log("meh")
        e.printStackTrace()
        throw TransportException(e.message ?: "")
    }
}

class TransportException(message: String) : Exception(message)