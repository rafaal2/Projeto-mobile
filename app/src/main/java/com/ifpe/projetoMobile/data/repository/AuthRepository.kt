package com.ifpe.projetomobile.deolhonaconsulta.data.repository

import com.ifpe.projetomobile.deolhonaconsulta.data.Api.response.AuthResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.headers
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.contentType

class AuthRepository(private val httpClient: HttpClient) {

    suspend fun fazerLogin(email: String, senha: String): AuthResponse {
        // Ajuste o IP conforme necessário (10.0.2.2 para emulador)
        return httpClient.post("http://10.0.2.2/ApiOlho/api/login.php") {
            contentType(ContentType.Application.Json)
            headers {
                append(HttpHeaders.Accept, ContentType.Application.Json.toString())
            }
            setBody(mapOf("email" to email, "senha" to senha))
        }.body()
    }

    suspend fun fazerCadastro(nome: String, email: String, senha: String): AuthResponse {
        return httpClient.post("http://10.0.2.2/ApiOlho/api/register.php") {
            contentType(ContentType.Application.Json)
            headers {
                append(HttpHeaders.Accept, ContentType.Application.Json.toString())
            }
            setBody(mapOf("nome" to nome, "email" to email, "senha" to senha))
        }.body()
    }
}