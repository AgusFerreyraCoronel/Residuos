package com.example.residuos.scanResult

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.residuos.MainActivity
import com.example.residuos.R
import com.example.residuos.databinding.ActivityScanResultBinding

public class ScanResultActivity : AppCompatActivity() {
    // Se definen las mismas claves para recuperar los datos
    private val KEY_ID = "KEY ID"
    private val KEY_PUNTOS = "EXTRA_PUNTOS"
    private val KEY_TIPO = "EXTRA_TIPO_RESIDUO"

    private lateinit var binding: ActivityScanResultBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityScanResultBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setContentView(R.layout.activity_scan_result)
        // Asegúrate de que este es el nombre de tu archivo layout XML
        setContentView(R.layout.activity_scan_result)

        // Recuperar el Intent que inició esta Activity
        val intent = intent
        Log.d("QR_SCAN", "Valor del intent: $intent")

        // 1. Recuperar los datos del Intent
        val idResiduo = intent.getStringExtra("KEY_ID")
        // Usamos getIntExtra, el '0' es el valor por defecto si la clave no se encuentra
        val puntos = intent.getIntExtra("KEY_PUNTOS", 0)
        val tipoResiduo = intent.getStringExtra("KEY_TIPO")
        Log.d("QR_SCAN", "Valor del id: $idResiduo")
        Log.d("QR_SCAN", "Valor del puntos: $puntos")
        Log.d("QR_SCAN", "Valor del Residuo: $tipoResiduo")

        // 2. Localizar los TextViews en el Layout
        val idTextView = findViewById<TextView>(R.id.text_id_residuo)
        val puntosTextView = findViewById<TextView>(R.id.text_puntos)
        val tipoTextView = findViewById<TextView>(R.id.text_tipo_residuo)

        // 3. Mostrar los datos en los TextViews
        idTextView.text = "ID del Residuo: ${idResiduo ?: "No encontrado"}"
        puntosTextView.text = "Puntos Obtenidos: $puntos"
        tipoTextView.text = "Tipo de Residuo: ${tipoResiduo ?: "No encontrado"}"

        }

    // Método que maneja el click del botón "Aceptar"
    private fun setupListeners() {

        binding.buttonAccept.setOnClickListener {
            navigateToMainActivity()
        }
    }

    // Método que realiza la navegación, no funca
    private fun navigateToMainActivity() {
        val intent = Intent(this, MainActivity::class.java).apply {
            // Flags para limpiar la pila de actividades (opcional pero recomendado)
            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        startActivity(intent)
        finish() // Cierra la Activity actual
    }

}