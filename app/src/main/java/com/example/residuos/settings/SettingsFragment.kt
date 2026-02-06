package com.example.residuos.settings

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.residuos.R
import com.example.residuos.databinding.FragmentSettingsBinding

class SettingsFragment : Fragment() {

    private lateinit var binding: FragmentSettingsBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnLogout.setOnClickListener {
            logout()
        }
    }

    private fun logout() {
        // Borramos todas las preferencias para que no quede nada en memoria

        // Auth prefs
        requireContext()
            .getSharedPreferences("auth", Context.MODE_PRIVATE)
            .edit()
            .clear()
            .apply()

        // User prefs
        requireContext()
            .getSharedPreferences("user_data", Context.MODE_PRIVATE)
            .edit()
            .clear()
            .apply()

        findNavController().navigate(
            R.id.action_settingsFragment_to_loginFragment
        )
    }
}