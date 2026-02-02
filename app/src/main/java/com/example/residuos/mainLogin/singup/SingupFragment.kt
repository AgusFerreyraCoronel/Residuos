package com.example.residuos.mainLogin.singup

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.residuos.R
import com.example.residuos.databinding.FragmentSignupBinding
import com.example.residuos.network.RetrofitClient
import kotlinx.coroutines.launch

class SingupFragment  : Fragment() {

    private lateinit var binding: FragmentSignupBinding
    private val viewModel: SignupViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_signup, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel

        binding.btnCreate.setOnClickListener {
            doSignup()
        }

        viewModel.uiState.observe(viewLifecycleOwner) { state ->
            when (state) {
                is SignupUiState.Loading -> {
                    binding.progress.visibility = View.VISIBLE
                }

                is SignupUiState.Success -> {
                    binding.progress.visibility = View.GONE
                    Toast.makeText(requireContext(), "Cuenta creada", Toast.LENGTH_LONG).show()
                }

                is SignupUiState.Error -> {
                    binding.progress.visibility = View.GONE
                    Toast.makeText(requireContext(), state.message, Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun doSignup() {
        val api = RetrofitClient.getApiService(requireContext())

        viewLifecycleOwner.lifecycleScope.launch {
            val body = viewModel.signup(
                api = api,
                username = binding.etUsername.text.toString(),
                password = binding.etPassword.text.toString(),
                email = binding.etEmail.text.toString().ifEmpty { null }
            )

            requireContext()
                .getSharedPreferences("auth", Context.MODE_PRIVATE)
                .edit()
                .putString("access", body.access)
                .putString("refresh", body.refresh)
                .apply()
        }
    }
}