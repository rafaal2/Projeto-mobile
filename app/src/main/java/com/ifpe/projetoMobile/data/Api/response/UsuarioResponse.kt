package com.projetomobile.deolhonaconsulta.data.Api.response

import kotlinx.serialization.Serializable

@Serializable
data class UsuarioResponse(
    val id: Int? = null,
    val nome: String? = null,
    val email: String? = null
)