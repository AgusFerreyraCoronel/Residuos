package com.example.residuos

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.residuos.databinding.ActivityMainBinding
import com.example.residuos.scanner.ScannerActivity

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Obtener usuario guardado
        val prefs = getSharedPreferences("user_data", MODE_PRIVATE)
        val username = prefs.getString("username", "Usuario")

        binding.tvUser.text = "Hola, $username"

        binding.btnScan.setOnClickListener {
            // Abrimos la actividad de escaneo
            val intent = Intent(this, ScannerActivity::class.java)
            startActivity(intent)
        }
    }
}