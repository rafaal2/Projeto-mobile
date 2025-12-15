package com.ifpe.projetomobile.deolhonaconsulta.ui.home

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.os.Bundle
import android.provider.Settings.Global.putString
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.RadioGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.ifpe.projetomobile.deolhonaconsulta.R
import com.ifpe.projetomobile.deolhonaconsulta.data.Api.HttpClientFactory
import com.ifpe.projetomobile.deolhonaconsulta.data.Api.response.ConfirmacaoRequest
import com.ifpe.projetomobile.deolhonaconsulta.data.repository.ConfirmacaoRepository
import com.ifpe.projetomobile.deolhonaconsulta.databinding.FragmentResultBinding
import kotlinx.coroutines.launch

class ResultFragment : Fragment() {
    private val confirmacaoRepository = ConfirmacaoRepository(HttpClientFactory.client)

    private var _binding: FragmentResultBinding? = null
    private val binding get() = _binding!!

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentResultBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val codSolicitacao = arguments?.getString("codSolicitacao") ?: ""
        val status = arguments?.getString("status") ?: ""
        val unidade = arguments?.getString("unidade_solicitante")

        binding.cod.text = "Código de Solicitação: $codSolicitacao"
        binding.status.text = "Status: $status"

        val desc = when (status) {
            "Autorizada" -> "Sua solicitação foi autorizada, favor comparecer na unidade $unidade com urgência ou clique na opção 'Imprimir sua marcação' para obter a chave de autorização para realização de sua consulta ou exame."
            "Expirada" -> "Seu agendamento expirou, caso não tenha sido atendido, favor procurar a unidade de saúde para verificar o motivo. Caso já tenha sido atendido, pedimos que avalie seu grau de satisfação com a consulta na unidade de saúde$unidade"
            "Cancelada" -> "Sua solicitação foi cancelada, favor procurar a unidade de saúde para verificar o motivo."
            "Devolvida" -> "Sua solicitação foi devolvida, favor procurar a unidade de saúde para verificar o motivo."
            "Negada" -> "Sua solicitação não foi autorizada, favor procurar a unidade de saúde para verificar o motivo."
            "Pendente" -> "Sua solicitação está pendente."
            "Nao encontrado" -> "Código de solicitação não existente, digitado incorretamente ou regulado pelo estado. Em caso de agendamento recente, favor aguardar o próximo dia útil e realizar nova busca."
            else -> "n/a"
        }
        binding.desc.text = desc

        when (status.lowercase()) {
            "pendente" -> {
                binding.buttonRate.visibility = View.VISIBLE
                binding.buttonRate.text = "Avaliar atendimento"
                binding.buttonConfirm.visibility = View.GONE
                binding.buttonPlusInfo.visibility = View.VISIBLE
            }
            "devolvida", "cancelada", "negada" -> {
                binding.buttonRate.visibility = View.GONE
                binding.buttonConfirm.visibility = View.GONE
                binding.buttonPlusInfo.visibility = View.VISIBLE
            }
            "nao encontrado" ->{
                binding.buttonRate.visibility = View.GONE
                binding.buttonConfirm.visibility = View.GONE
                binding.buttonPlusInfo.visibility = View.GONE
            }
            "autorizada" -> {
                binding.buttonRate.visibility = View.GONE
                binding.buttonImprimirAuto.visibility = View.VISIBLE
                binding.buttonConfirm.visibility = View.VISIBLE
                binding.buttonPlusInfo.visibility = View.VISIBLE
                binding.buttonConfirm.text = "Ciente de agendamento"
            }
            "expirada" -> {
                binding.buttonRate.visibility = View.VISIBLE
                binding.buttonRate.text = "Avaliar atendimento"
                binding.buttonConfirm.visibility = View.GONE
                binding.buttonPlusInfo.visibility = View.VISIBLE
            }
            else -> {
                binding.buttonRate.visibility = View.GONE
                binding.buttonConfirm.visibility = View.GONE
                binding.buttonPlusInfo.visibility = View.VISIBLE
            }
        }

        binding.buttonPlusInfo.setOnClickListener {
            val bundle = Bundle().apply {
                putString("codSolicitacao", codSolicitacao)
            }
            findNavController().navigate(R.id.action_resultFragment_to_infoFragment, bundle)
        }

        binding.buttonImprimirAuto.setOnClickListener {
            val bundle = Bundle().apply {
                putString("codSolicitacao", codSolicitacao)
            }
            findNavController().navigate(R.id.action_resultFragment_to_keyFragment, bundle)
        }

        binding.buttonRate.setOnClickListener {
            val bundle = Bundle().apply {
                putString("codSolicitacao", codSolicitacao)
                putString("status", status)
            }
            if (status.equals("Autorizada", ignoreCase = true)) {
                findNavController().navigate(R.id.action_resultFragment_to_avaliacaoFragment, bundle)
            } else {
                val bundle = Bundle().apply {
                putString("codSolicitacao", codSolicitacao)
                putString("unidade_solicitante", unidade)}
                findNavController().navigate(R.id.action_resultFragment_to_avaliacaoFragment, bundle)
            }
        }

        binding.buttonConfirm.setOnClickListener {
            mostrarDialogConfirmacao(codSolicitacao)
        }
    }

    private fun mostrarDialogConfirmacao(codSolicitacao: String) {
        val dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_confirmacao, null)

        val telefoneLayout = dialogView.findViewById<LinearLayout>(R.id.telefoneLayout)
        val editTelefone = dialogView.findViewById<EditText>(R.id.editTelefone)
        val radioGroupTipo = dialogView.findViewById<RadioGroup>(R.id.radioGroupTipo)

        telefoneLayout.visibility = View.GONE

        val dialog = AlertDialog.Builder(requireContext())
            .setTitle("Confirmar Atendimento")
            .setView(dialogView)
            .setPositiveButton("Sim", null)
            .setNegativeButton("Não") { dlg, _ ->
                dlg.dismiss()
                exibirDialogCancelamento(codSolicitacao)
            }
            .create()

        // Depois de criar o diálogo, podemos customizar o clique do botão “SIM”
        dialog.setOnShowListener {
            val positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE)
            positiveButton.setOnClickListener {
                // Se o layout do telefone estiver oculto, mostra e troca o texto do botão
                if (telefoneLayout.visibility == View.GONE) {
                    telefoneLayout.visibility = View.VISIBLE
                    positiveButton.text = "Enviar Confirmação"
                } else {
                    // Pegar dados do telefone e tipo de número
                    val telefone = editTelefone.text.toString().trim()
                    val tipoSelecionado = when (radioGroupTipo.checkedRadioButtonId) {
                        R.id.radioTelefone  -> "TELEFONE"
                        R.id.radioWhatsApp  -> "WHATSAPP"
                        R.id.radioAmbos     -> "AMBOS"
                        else                -> ""
                    }

                    if (telefone.isEmpty() || tipoSelecionado.isEmpty()) {
                        Toast.makeText(context, "Preencha todos os campos corretamente.", Toast.LENGTH_SHORT).show()
                    } else {
                        // Envia “S” + telefone + tipo
                        enviarConfirmacaoApi(codSolicitacao, telefone, tipoSelecionado, "S")
                        dialog.dismiss()
                    }
                }
            }
        }

        dialog.show()
    }

    private fun exibirDialogCancelamento(codSolicitacao: String) {
        AlertDialog.Builder(requireContext()).apply {
            setMessage("Sua consulta será cancelada. Deseja confirmar?")
            setPositiveButton("Confirmar") { dialog, _ ->
                enviarConfirmacaoApi(codSolicitacao, null, null, "N")
                Toast.makeText(context, "Obrigado por avisar", Toast.LENGTH_SHORT).show()
                dialog.dismiss()
            }
            setNegativeButton("Voltar") { dialog, _ ->
                dialog.dismiss()
            }
        }.show()
    }

    private fun enviarConfirmacaoApi(
        codSolicitacao: String,
        telefone: String?,
        tipoNumero: String?,
        confirmado: String
    ) {
        lifecycleScope.launch {
            try {
                val request = ConfirmacaoRequest(
                    COD_SOLICITACAO = codSolicitacao,
                    TELEFONE = telefone,
                    TIPO_NUMERO = tipoNumero,
                    CONFIRMADO = confirmado,
                    TOKEN_FIREBASE = "token_firebase_usuario",
                    TOKEN_APLICACAO = "app_A"
                )

                val response = confirmacaoRepository.createConfirmacao(request)

                if (response.message != null) {
                    Toast.makeText(context, response.message, Toast.LENGTH_LONG).show()
                } else {
                    Toast.makeText(context, response.error ?: "Erro desconhecido", Toast.LENGTH_LONG).show()
                }
            } catch (e: Exception) {
                Toast.makeText(context, "Erro ao conectar com servidor.", Toast.LENGTH_SHORT).show()
                Log.e("CONFIRMACAO_API", "Erro", e)
            }
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
