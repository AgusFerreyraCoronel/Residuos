package com.example.residuos.mainLogin

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.residuos.network.RetrofitClient
import com.example.residuos.network.SignupRequest
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import com.example.residuos.databinding.ActivitySignupBinding

class SignupActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignupBinding
    private val ioScope = CoroutineScope(Dispatchers.IO)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnCreate.setOnClickListener { doSignup() }
    }

    private fun doSignup() {
        val user = binding.etUsername.text.toString().trim()
        val pass = binding.etPassword.text.toString()
        val email = binding.etEmail.text.toString().trim().ifEmpty { null }

        if (user.isEmpty() || pass.isEmpty()) {
            Toast.makeText(this, "Usuario y contraseña requeridos", Toast.LENGTH_SHORT).show()
            return
        }

        binding.progress.visibility = View.VISIBLE
        ioScope.launch {
            try {
                val api = RetrofitClient.getApiService(applicationContext)
                val resp = api.signup(
                    SignupRequest(
                        username = user,
                        password = pass,
                        email = email
                    )
                )
                withContext(Dispatchers.Main) {
                    binding.progress.visibility = View.GONE
                    if (resp.isSuccessful) {
                        val body = resp.body()
                        if (body != null) {
                            // GUARDAR TOKENS
                            getSharedPreferences("auth", MODE_PRIVATE).edit()
                                .putString("access", body.access)
                                .putString("refresh", body.refresh)
                                .apply()

                            Toast.makeText(this@SignupActivity, "Cuenta creada", Toast.LENGTH_LONG).show()

                            finish() // vuelve al login
                        }
                    } else {
                        Toast.makeText(this@SignupActivity, "Error registrando: ${resp.code()}", Toast.LENGTH_LONG).show()
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    binding.progress.visibility = View.GONE
                    Toast.makeText(this@SignupActivity, "Error de red: ${e.localizedMessage}", Toast.LENGTH_LONG).show()
                }
            }
        }
    }
}