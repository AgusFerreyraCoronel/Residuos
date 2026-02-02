package com.example.residuos.mainLogin.singup

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.residuos.R
import com.example.residuos.databinding.FragmentSignupBinding
import com.example.residuos.network.RetrofitClient

class SingupFragment : Fragment() {

    private lateinit var binding: FragmentSignupBinding
    private val viewModel: SignupViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_signup,
            container,
            false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel

        binding.btnCreate.setOnClickListener {
            viewModel.signup(
                api = RetrofitClient.getApiService(requireContext()),
                username = binding.etUsername.text.toString().trim(),
                password = binding.etPassword.text.toString(),
                email = binding.etEmail.text.toString().ifEmpty { null }
            )
        }

        viewModel.uiState.observe(viewLifecycleOwner) { state ->
            when (state) {
                SignupUiState.Loading -> {
                    binding.progress.visibility = View.VISIBLE
                }

                SignupUiState.Success -> {
                    binding.progress.visibility = View.GONE
                    Toast.makeText(
                        requireContext(),
                        "Cuenta creada",
                        Toast.LENGTH_LONG
                    ).show()

                    // VOLVER AL LOGIN
                    findNavController().popBackStack()
                }

                is SignupUiState.Error -> {
                    binding.progress.visibility = View.GONE
                    Toast.makeText(
                        requireContext(),
                        state.message,
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }
}