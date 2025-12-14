package com.projetomobile.deolhonaconsulta.data.Api.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ConsultaDetResponse(
    @SerialName("DATA_DA_SOLICITACAO")
    val dataDaSolicitacao: String? = null,
    @SerialName("NOME_DO_PACIENTE")
    val nomeDoPaciente: String? = null,
    @SerialName("PROCEDIMENTO")
    val procedimento: String? = null,
    @SerialName("CARTAO_SUS")
    val cartaoSus: String? = null,
    @SerialName("CHAVE")
    val chave: String? = null,
    @SerialName("DATA_DE_EXECUCAO")
    val dataDeExecucao: String? = null,
    @SerialName("HORA_DE_EXECUCAO")
    val horaDeExecucao: String? = null,
    @SerialName("DATA_DE_ATENDIMENTO")
    val dataDeAtendimento: String? = null,
    @SerialName("OBS")
    val obs: String? = null,
    @SerialName("UNIDADE_SOLICITANTE")
    val unidadeSolicitante: String? = null,
    @SerialName("ENDERECO_SOLICITANTE")
    val enderecoSolicitante: String? = null,
    @SerialName("UNIDADE_EXECUTANTE")
    val unidadeExecutante: String? = null,
    @SerialName("ENDERECO_EXECUTANTE")
    val enderecoExecutante: String? = null
)