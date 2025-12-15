package com.ifpe.projetomobile.deolhonaconsulta.data.repository

import com.ifpe.projetomobile.deolhonaconsulta.data.Api.response.UsuarioResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.headers
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.contentType

class UsuarioRepository(private val httpClient: HttpClient) {

    // Você pode buscar pelo ID (recomendado) ou pelo Email
    suspend fun getPerfil(idUsuario: Int): UsuarioResponse {
        return httpClient.post("http://10.0.2.2/ApiOlho/api/usuario.php?acao=perfil") {
            contentType(ContentType.Application.Json)
            headers {
                append(HttpHeaders.Accept, ContentType.Application.Json.toString())
            }
            setBody(mapOf("id" to idUsuario))
        }.body()
    }
}