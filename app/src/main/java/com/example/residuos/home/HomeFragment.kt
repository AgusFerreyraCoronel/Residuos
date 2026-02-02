package com.example.residuos.home

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.example.residuos.R
import com.example.residuos.databinding.FragmentHomeBinding

class HomeFragment : Fragment(R.layout.fragment_home) {

    private lateinit var binding: FragmentHomeBinding

    // Lista de tips varios que se muestran al usuario, podriamos ver de hacer una clase propia que tenga mas comportamiento quizas.
    private val tips = listOf(
        "Separá los residuos reciclables antes de tirarlos.",
        "Las botellas plásticas deben estar limpias y secas.",
        "El cartón mojado no se puede reciclar.",
        "Reducí el uso de bolsas plásticas.",
        "Reutilizar también es reciclar."
    )

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentHomeBinding.bind(view)

        val prefs = requireContext()
            .getSharedPreferences("auth", Context.MODE_PRIVATE)

        val username = prefs.getString("username", "Usuario")

        binding.tvWelcome.text = "Bienvenido $username"

        binding.tvTip.text = tips.random()
    }
}