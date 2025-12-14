package com.projetomobile.deolhonaconsulta.data.Api.response

import kotlinx.serialization.Serializable

@Serializable
data class AuthResponse(
    val erro: Boolean,
    val mensagem: String,
    val usuario: Usuario? = null // É null no cadastro, mas vem preenchido no login
)

@Serializable
data class Usuario(
    val id: Int,
    val nome: String,
    val email: String
)