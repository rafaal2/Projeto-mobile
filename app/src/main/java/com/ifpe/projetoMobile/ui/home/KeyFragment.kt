package com.ifpe.projetomobile.deolhonaconsulta.ui.home

import android.annotation.SuppressLint
import android.content.ActivityNotFoundException
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.ifpe.projetomobile.deolhonaconsulta.data.Api.HttpClientFactory
import com.ifpe.projetomobile.deolhonaconsulta.data.repository.ConsultaDetRepository
import com.ifpe.projetomobile.deolhonaconsulta.databinding.FragmentKeyBinding
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream

class KeyFragment : Fragment() {
    private val consultaDetRepository = ConsultaDetRepository(HttpClientFactory.client)

    private var _binding: FragmentKeyBinding? = null
    private val binding get() = _binding!!
    private var enderecoExecutante: String? = null



    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentKeyBinding.inflate(inflater, container, false)
        return binding.root}

        @SuppressLint("SetTextI18n")
        override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
            super.onViewCreated(view, savedInstanceState)

            lifecycleScope.launch {
                val codSolicitacaoStr = arguments?.getString("codSolicitacao") ?: ""
                val response = consultaDetRepository.consultaDet(codSolicitacaoStr)
                enderecoExecutante = response.enderecoExecutante
                try {
                    binding.tvChave.text = "chave: ${response.chave ?: "N/A"}"
                    binding.tvProcedimento.text = "Procedimento: ${response.procedimento ?: "N/A"}"
                    binding.tvCod.text = "Código de Solicitação: $codSolicitacaoStr"
                    binding.tvNomeDoPaciente.text = "Nome do Paciente: ${response.nomeDoPaciente ?: "N/A"}"
                    binding.tvDiaHora.text = "Data: ${response.dataDaSolicitacao ?: "N/A"}" + " horario: ${response.horaDeExecucao ?: "N/A"}"
                    binding.tvCartaoSUS.text = "Cartão SUS: ${response.cartaoSus ?: "N/A"}"
                    binding.tvUnidadeSolicitante.text = "Unidade Solicitante: ${response.unidadeSolicitante ?: "N/A"}"
                    binding.tvUnidadeExecutante.text = "Unidade Executante: ${response.unidadeExecutante ?: "N/A"}"
                    binding.tvendereco.text = "Endereço: ${response.enderecoExecutante ?: "N/A"}"
                } catch (e: Exception) {
                    Log.e("InfoFragment", "Erro ao buscar detalhes da solicitação", e)
                }
            }

            binding.buttonImprimir.setOnClickListener {
                // Cria bitmap da view inteira
                val rootView = binding.root
                val bitmap = Bitmap.createBitmap(
                    rootView.width,
                    rootView.height,
                    Bitmap.Config.ARGB_8888
                )
                val canvas = Canvas(bitmap)
                rootView.draw(canvas)

                // Salva em arquivo no cache
                val cachePath = File(requireContext().cacheDir, "images")
                cachePath.mkdirs()
                val file = File(cachePath, "screenshot.png")
                FileOutputStream(file).use { out ->
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, out)
                }

                // Gera URI via FileProvider
                val uri: Uri = FileProvider.getUriForFile(
                    requireContext(),
                    "${requireContext().packageName}.provider",
                    file
                )

                // Intent de compartilhamento
                val shareIntent = Intent(Intent.ACTION_SEND).apply {
                    type = "image/png"
                    putExtra(Intent.EXTRA_STREAM, uri)
                    addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                }
                startActivity(Intent.createChooser(shareIntent, "Compartilhar captura"))
            }

            // Botão Maps mantém seu código original
            binding.buttonMaps.setOnClickListener {
                enderecoExecutante?.let {
                    abrirRotaNoGoogleMaps(it)
                } ?: Toast.makeText(
                    requireContext(),
                    "Endereço não disponível",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

    private fun abrirRotaNoGoogleMaps(endereco: String) {
        val gmmIntentUri = Uri.parse("geo:0,0?q=${Uri.encode(endereco)}")
        val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
        mapIntent.setPackage("com.google.android.apps.maps")

        try {
            startActivity(mapIntent)
        } catch (e: ActivityNotFoundException) {
            val browserUri = Uri.parse("https://www.google.com/maps/dir/?api=1&destination=${Uri.encode(endereco)}")
            val browserIntent = Intent(Intent.ACTION_VIEW, browserUri)

            try {
                startActivity(browserIntent)
            } catch (e: ActivityNotFoundException) {
                Toast.makeText(requireContext(), "Nenhum aplicativo de navegação encontrado", Toast.LENGTH_SHORT).show()
            }
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

