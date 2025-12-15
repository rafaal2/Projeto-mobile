package com.recifemecatron.deolhonaconsulta

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.recifemecatron.deolhonaconsulta.databinding.ActivityLoginBinding
import com.recifemecatron.deolhonaconsulta.databinding.FragmentLoginBinding

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        // 1. Instala a Splash Screen (pois essa é a tela inicial definida no Manifest)
        installSplashScreen()

        super.onCreate(savedInstanceState)

        // 2. Apenas infla o layout. O resto é automático via XML e NavGraph.
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}