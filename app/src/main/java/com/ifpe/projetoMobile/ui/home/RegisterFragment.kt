package com.projetomobile.deolhonaconsulta.ui.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.projetomobile.deolhonaconsulta.data.Api.HttpClientFactory
import com.projetomobile.deolhonaconsulta.data.repository.AuthRepository
import com.projetomobile.deolhonaconsulta.databinding.FragmentRegisterBinding
import kotlinx.coroutines.launch

class RegisterFragment : Fragment() {

    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!

    // INSTANCIAÇÃO IGUAL AO SEU AVALIACAO FRAGMENT
    private val repository = AuthRepository(HttpClientFactory.client)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonCadastrar.setOnClickListener {
            val nome = binding.editTextNome.text.toString()
            val email = binding.editTextEmailCadastro.text.toString()
            val senha = binding.editTextSenhaCadastro.text.toString()

            if (nome.isNotEmpty() && email.isNotEmpty() && senha.isNotEmpty()) {

                lifecycleScope.launch {
                    try {
                        val response = repository.fazerCadastro(nome, email, senha)

                        if (!response.erro) {
                            Toast.makeText(requireContext(), "Cadastro realizado!", Toast.LENGTH_SHORT).show()
                            // Volta para o login (popBackStack)
                            findNavController().popBackStack()
                        } else {
                            Toast.makeText(requireContext(), response.mensagem, Toast.LENGTH_SHORT).show()
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                        Toast.makeText(requireContext(), "Erro ao cadastrar: ${e.message}", Toast.LENGTH_SHORT).show()
                    }
                }

            } else {
                Toast.makeText(requireContext(), "Preencha todos os campos", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}