package com.ifpe.projetomobile.deolhonaconsulta.ui.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.firebase.messaging.FirebaseMessaging
import com.ifpe.projetomobile.deolhonaconsulta.R
import com.ifpe.projetomobile.deolhonaconsulta.data.Api.HttpClientFactory
import com.ifpe.projetomobile.deolhonaconsulta.data.Api.response.AvaliacaoRequest
import com.ifpe.projetomobile.deolhonaconsulta.data.repository.AvaliacaoRepository
import kotlinx.coroutines.launch
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class AvaliacaoFragment : Fragment() {

    private lateinit var scrollView: ScrollView
    private lateinit var radioGroupResultado: RadioGroup
    private lateinit var radioGroupIndicador: RadioGroup
    private lateinit var editTextComentario: EditText
    private lateinit var buttonSubmit: Button
    private lateinit var indicatorContainer: LinearLayout
    private lateinit var commentContainer: LinearLayout

    private val avaliacaoRepository = AvaliacaoRepository(HttpClientFactory.client)
    private lateinit var tokenFirebase: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_avaliacao, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val unidade = arguments?.getString("unidade_solicitante")

        FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w("FCM_TOKEN", "Erro ao obter token do Firebase", task.exception)
                return@addOnCompleteListener
            }
            tokenFirebase = task.result
        }

        scrollView = view.findViewById(R.id.scrollView)
        radioGroupResultado = view.findViewById(R.id.radioGroupResultado)
        radioGroupIndicador = view.findViewById(R.id.radioGroupIndicador)
        editTextComentario = view.findViewById(R.id.editTextComentario)
        buttonSubmit = view.findViewById(R.id.buttonSubmit)
        indicatorContainer = view.findViewById(R.id.indicatorContainer)
        commentContainer = view.findViewById(R.id.commentContainer)

        radioGroupResultado.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.radioOtimo -> {
                    indicatorContainer.visibility = View.GONE
                    commentContainer.visibility = View.VISIBLE
                }
                R.id.radioRegular, R.id.radioPessimo -> {
                    indicatorContainer.visibility = View.VISIBLE
                    commentContainer.visibility = View.VISIBLE
                    scrollView.post { scrollView.fullScroll(ScrollView.FOCUS_DOWN) }
                }
                else -> {
                    indicatorContainer.visibility = View.GONE
                    commentContainer.visibility = View.GONE
                }
            }
        }

        buttonSubmit.setOnClickListener {
            val selectedResultadoId = radioGroupResultado.checkedRadioButtonId
            if (selectedResultadoId == -1) {
                Toast.makeText(requireContext(), "Selecione uma avaliação", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val resultadoValue = when (selectedResultadoId) {
                R.id.radioOtimo    -> "5"
                R.id.radioRegular  -> "3"
                R.id.radioPessimo  -> "1"
                else               -> "0"
            }

            val selectedIndicadorId = radioGroupIndicador.checkedRadioButtonId
            val indicadorValue = if (selectedIndicadorId != -1) {
                view.findViewById<RadioButton>(selectedIndicadorId).text.toString()
            } else ""

            val comentarioValue = editTextComentario.text.toString()
            val codSolicitacaoStr = arguments?.getString("codSolicitacao") ?: ""

            // Monta o request e faz log do JSON
            val avaliacaoRequest = AvaliacaoRequest(
                COD_SOLICITACAO       = codSolicitacaoStr,
                RESULTADO            = resultadoValue,
                INDICADOR            = indicadorValue,
                COMENTARIO           = comentarioValue,
                NOME_UNIDADE_AVALIADA= unidade ?: "Desconhecida",
                NUN_AVALIACAO        = 1,
                TOKEN_FIREBASE       = tokenFirebase
            )
            val jsonToSend = Json.encodeToString(avaliacaoRequest)
            Log.d("AVALIACAO_JSON", jsonToSend)

            lifecycleScope.launch {
                try {
                    avaliacaoRepository.createAvaliacao(avaliacaoRequest)
                    Toast.makeText(requireContext(), "Avaliação enviada com sucesso!", Toast.LENGTH_SHORT).show()
                    findNavController().popBackStack()
                } catch (e: Exception) {
                    e.printStackTrace()
                    Toast.makeText(requireContext(), "Erro ao enviar avaliação", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}
