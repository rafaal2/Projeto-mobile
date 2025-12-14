package com.projetomobile.deolhonaconsulta.data.Api.response

import kotlinx.serialization.Serializable

@Serializable
data class ConfirmacaoRequest(
    val COD_SOLICITACAO: String?,
    val TELEFONE: String?,
    val TIPO_NUMERO: String?,
    val CONFIRMADO: String?,
    val TOKEN_FIREBASE: String?,
    val TOKEN_APLICACAO: String?
)

@Serializable
data class ConfirmacaoResponse(
    val message: String? = null,
    val error: String? = null
)
