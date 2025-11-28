package com.leny.yanel.app.api

import io.ktor.client.*
import io.ktor.client.engine.okhttp.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json

object ApiClient {

    val client = HttpClient(OkHttp) {
        install(ContentNegotiation) {
            json(
                Json {
                    ignoreUnknownKeys = true  // si el back manda campos extras, no truena
                    isLenient = true
                }
            )
        }
        install(Logging) {
            level = LogLevel.ALL
        }
    }

    // Cambia esto si tu API usa otro path base
    const val BASE_URL = "https://backlenylannel.onrender.com/"
}
