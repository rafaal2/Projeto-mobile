package com.projetomobile.deolhonaconsulta.data.Api
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.DEFAULT
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.logging.Logger           // <-- import correto
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import java.util.concurrent.TimeUnit

object HttpClientFactory {
    val client = HttpClient(OkHttp) {
        engine {
            config {
                readTimeout(60, TimeUnit.SECONDS)
                writeTimeout(60, TimeUnit.SECONDS)
                connectTimeout(30, TimeUnit.SECONDS)
            }
        }
        install(Logging) {
            logger = Logger.DEFAULT               // Logger do Ktor
            level = LogLevel.BODY                 // ou ALL, se quiser tudo
        }
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
                prettyPrint = true
                isLenient = true
            })
        }
    }
}
