package com.ifpe.projetomobile.deolhonaconsulta.deolhonaconsulta.data.repository

import com.ifpe.projetomobile.deolhonaconsulta.deolhonaconsulta.data.Api.response.ConsultaResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.headers
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.contentType

class ConsultaRepository(private val httpClient: HttpClient) {

    suspend fun consulta(codSolicitacao: String, tokenFirebase: String, tokenAplicacao: String): ConsultaResponse {
        return httpClient.post("http://10.0.2.2/ApiOlho/api/solicitacao.php?acao=status") {
            contentType(ContentType.Application.Json)
            headers {
                append(HttpHeaders.Accept, ContentType.Application.Json.toString())
            }
            setBody(
                mapOf(
                    "COD_SOLICITACAO" to codSolicitacao,
                    "TOKEN_FIREBASE" to tokenFirebase,
                    "TOKEN_APLICACAO" to tokenAplicacao
                )
            )
        }.body()
    }
}
