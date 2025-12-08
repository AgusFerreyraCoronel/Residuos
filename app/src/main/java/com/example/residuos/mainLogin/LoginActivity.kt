package com.example.residuos.mainLogin

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.residuos.databinding.ActivityLoginBinding
import com.example.residuos.MainActivity
import com.example.residuos.network.LoginRequest
import com.example.residuos.network.RetrofitClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private val ioScope = CoroutineScope(Dispatchers.IO)

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Botón de registrarse
        binding.btnSignup.setOnClickListener {
            startActivity(Intent(this, SignupActivity::class.java))
        }

        // Botón de login
        binding.btnLogin.setOnClickListener {
            attemptLogin()
        }
    }

    private fun attemptLogin() {
        val user = binding.etUsername.text.toString().trim()
        val pass = binding.etPassword.text.toString()

        if (user.isEmpty() || pass.isEmpty()) {
            Toast.makeText(this, "Usuario y contraseña requeridos", Toast.LENGTH_SHORT).show()
            return
        }

        binding.progress.visibility = View.VISIBLE

        ioScope.launch {
            try {
                val api = RetrofitClient.getApiService(applicationContext)
                val resp = api.login(LoginRequest(username = user, password = pass))

                withContext(Dispatchers.Main) {
                    binding.progress.visibility = View.GONE

                    if (resp.isSuccessful && resp.body() != null) {

                        val body = resp.body()!!

                        // Guardar nombre del usuario para mostrarlo en MainActivity
                        val prefs = getSharedPreferences("user_data", MODE_PRIVATE)
                        prefs.edit()
                            .putString("username", user)
                            .apply()

                        // Guardar tokens para despues
                        val shared = getSharedPreferences("auth", MODE_PRIVATE)
                        shared.edit()
                            .putString("access_token", body.access)
                            .putString("refresh_token", body.refresh)
                            .apply()

                        // Ir a la pantalla principal
                        startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                        Toast.makeText(this@LoginActivity, "Login OK", Toast.LENGTH_SHORT).show()

                    } else {
                        val code = resp.code()
                        Toast.makeText(
                            this@LoginActivity,
                            "Error: $code. Credenciales inválidas.",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }

            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    binding.progress.visibility = View.GONE
                    Toast.makeText(
                        this@LoginActivity,
                        "Error de red: ${e.localizedMessage}",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }
}