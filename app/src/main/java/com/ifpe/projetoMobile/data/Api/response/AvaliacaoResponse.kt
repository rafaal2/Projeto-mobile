package com.ifpe.projetomobile.deolhonaconsulta.data.Api.response

import kotlinx.serialization.Serializable

@Serializable
data class AvaliacaoRequest(
    val COD_SOLICITACAO: String,
    val RESULTADO: String,
    val INDICADOR: String,
    val COMENTARIO: String,
    val NOME_UNIDADE_AVALIADA: String,
    val NUN_AVALIACAO: Int,
    val TOKEN_FIREBASE: String
)

@Serializable
data class AvaliacaoResponse(
    val message: String? = null,
    val error: String? = null
)