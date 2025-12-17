package com.example.residuos.scanResult

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.residuos.MainActivity
import com.example.residuos.databinding.ActivityScanResultBinding
import com.example.residuos.scanner.ScannerActivity

class ScanResultActivity : AppCompatActivity() {

    private lateinit var binding: ActivityScanResultBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityScanResultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val idResiduo =
            intent.getStringExtra(ScannerActivity.KEY_ID)
        val puntos =
            intent.getIntExtra(ScannerActivity.KEY_PUNTOS, 0)
        val tipoResiduo =
            intent.getStringExtra(ScannerActivity.KEY_TIPO)

        binding.textIdResiduo.text = idResiduo ?: "No encontrado"
        binding.textPuntos.text = puntos.toString()
        binding.textTipoResiduo.text = tipoResiduo ?: "No encontrado"

        setupListeners()
    }

    private fun setupListeners() {
        binding.buttonAccept.setOnClickListener {
            navigateToMain()
        }
    }

    private fun navigateToMain() {
        val intent = Intent(this, MainActivity::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        startActivity(intent)
        finish()
    }
}