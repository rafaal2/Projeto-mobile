package com.recifemecatron.deolhonaconsulta.ui.home

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.recifemecatron.deolhonaconsulta.data.Api.HttpClientFactory
import com.recifemecatron.deolhonaconsulta.data.repository.ConsultaDetRepository
import com.recifemecatron.deolhonaconsulta.databinding.FragmentInfoBinding
import kotlinx.coroutines.launch

class InfoFragment : Fragment() {

    private var _binding: FragmentInfoBinding? = null
    private val binding get() = _binding!!

    private val consultaDetRepository = ConsultaDetRepository(HttpClientFactory.client)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentInfoBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val codSolicitacaoStr = arguments?.getString("codSolicitacao") ?: ""
        val codSolicitacao = codSolicitacaoStr.toLongOrNull()

        if (codSolicitacao != null) {
            lifecycleScope.launch {
                try {
                    val response = consultaDetRepository.consultaDet(codSolicitacaoStr)
                    Log.d("API_RESPONSE", "Resposta completa: $response")
                    binding.cod.text = "Código de Solicitação: $codSolicitacaoStr"
                    binding.status.text = ""
                    binding.tvDataDaSolicitacao.text = "Data da Solicitação: ${response.dataDaSolicitacao ?: "N/A"}"
                    binding.tvNomeDoPaciente.text = "Nome do Paciente: ${response.nomeDoPaciente ?: "N/A"}"
                    binding.tvProcedimento.text = "Procedimento: ${response.procedimento ?: "N/A"}"
                    binding.tvUnidadeSolicitante.text = "Unidade Solicitante: ${response.unidadeSolicitante ?: "N/A"}"
                    binding.tvUnidadeExecutante.text = "Unidade Executante: ${response.unidadeExecutante ?: "N/A"}"
                    binding.tvDataDeAtendimento.text = "Data de Atendimento: ${response.dataDeAtendimento ?: "N/A"}"
                    binding.tvObs.text = "Observação: ${response.obs ?: "N/A"}"
                } catch (e: Exception) {
                    Log.e("InfoFragment", "Erro ao buscar detalhes da solicitação", e)
                }
            }
        } else {
            Log.e("InfoFragment", "Código de solicitação inválido")
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
