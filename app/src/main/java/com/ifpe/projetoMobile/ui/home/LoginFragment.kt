package com.ifpe.projetomobile.deolhonaconsulta.ui.auth

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.ifpe.projetomobile.deolhonaconsulta.MainActivity
import com.ifpe.projetomobile.deolhonaconsulta.R
import com.ifpe.projetomobile.deolhonaconsulta.data.Api.HttpClientFactory
import com.ifpe.projetomobile.deolhonaconsulta.data.repository.AuthRepository
import com.ifpe.projetomobile.deolhonaconsulta.databinding.FragmentLoginBinding
import kotlinx.coroutines.launch

class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    // INSTANCIAÇÃO IGUAL AO SEU AVALIACAO FRAGMENT
    private val repository = AuthRepository(HttpClientFactory.client)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonLogin.setOnClickListener {
            val email = binding.editTextEmail.text.toString()
            val senha = binding.editTextSenha.text.toString()

            if (email.isNotEmpty() && senha.isNotEmpty()) {

                // CHAMADA ASSÍNCRONA IGUAL AO SEU PADRÃO
                lifecycleScope.launch {
                    try {
                        val response = repository.fazerLogin(email, senha)

                        if (!response.erro) {
                            Toast.makeText(requireContext(), "Bem-vindo, ${response.usuario?.nome}", Toast.LENGTH_SHORT).show()

                            // Vai para a MainActivity
                            val intent = Intent(requireContext(), MainActivity::class.java)
                            startActivity(intent)
                            requireActivity().finish()
                        } else {
                            Toast.makeText(requireContext(), response.mensagem, Toast.LENGTH_SHORT).show()
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                        Toast.makeText(requireContext(), "Erro de conexão: ${e.message}", Toast.LENGTH_SHORT).show()
                    }
                }

            } else {
                Toast.makeText(requireContext(), "Preencha os campos", Toast.LENGTH_SHORT).show()
            }
        }

        binding.textIrParaRegistro.setOnClickListener {
            findNavController().navigate(R.id.action_login_to_register)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}