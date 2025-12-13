package com.recifemecatron.deolhonaconsulta.data.Api.response

import kotlinx.serialization.Serializable

@Serializable
data class ConsultaResponse(
    val STATUS: String? = null,
    val TOKEN_FIREBASE: String? = null,
    val TOKEN_APLICACAO: String? = null,
    val UNIDADE_SOLICITANTE: String? = null
)