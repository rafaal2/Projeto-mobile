package com.ifpe.projetomobile.deolhonaconsulta.ui.gallery

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.ifpe.projetomobile.deolhonaconsulta.LoginActivity
import com.ifpe.projetomobile.deolhonaconsulta.databinding.FragmentGalleryBinding

class GalleryFragment : Fragment() {

    private var _binding: FragmentGalleryBinding? = null
    // Essa propriedade só é válida entre onCreateView e onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGalleryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Aqui futuramente chamaremos a API para pegar os dados reais
        carregarDadosDoUsuario()

        // Configuração do botão de sair (exemplo)
        binding.btnSair.setOnClickListener {
            fazerLogout()
        }
    }

    private fun carregarDadosDoUsuario() {
        // TODO: Substituir por chamada da API PHP depois
        // Por enquanto, dados fictícios para visualização
        val nomeMock = "Usuário Testee"
        val emailMock = "usuario@teste.com"

        binding.tvNomeUsuario.text = nomeMock
        binding.tvEmailUsuario.text = emailMock

        // Se quiser mudar o avatar dinamicamente depois:
        // binding.imageAvatar.setImageResource(...)
    }

    private fun fazerLogout() {
        // Lógica simples de logout
        // 1. Limpar SharedPreferences (se houver)
        // 2. Voltar para tela de login

        Toast.makeText(requireContext(), "Saindo...", Toast.LENGTH_SHORT).show()

        val intent = Intent(requireContext(), LoginActivity::class.java)
        // Limpa a pilha de atividades para o usuário não voltar com o botão "voltar"
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}