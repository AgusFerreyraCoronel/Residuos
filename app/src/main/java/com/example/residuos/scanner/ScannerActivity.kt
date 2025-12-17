package com.example.residuos.scanner

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.util.Size
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.annotation.OptIn
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.example.residuos.R
import com.example.residuos.network.RetrofitClient
import com.example.residuos.scanResult.ScanResultActivity
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.barcode.common.Barcode.FORMAT_QR_CODE
import com.google.mlkit.vision.common.InputImage
import kotlinx.coroutines.launch
import org.json.JSONException
import org.json.JSONObject

class ScannerActivity : ComponentActivity() {

    companion object {
        const val KEY_ID = "KEY_ID"
        const val KEY_PUNTOS = "KEY_PUNTOS"
        const val KEY_TIPO = "KEY_TIPO"
    }

    private lateinit var preview: Preview
    private lateinit var imageAnalysis: ImageAnalysis

    private var qrHandled = false
    private var errorShown = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scanner)

        if (allPermissionsGranted()) {
            startCamera()
        } else {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.CAMERA),
                100
            )
        }
    }

    private fun allPermissionsGranted() =
        ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED

    @OptIn(ExperimentalGetImage::class)
    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)

        cameraProviderFuture.addListener({
            val cameraProvider = cameraProviderFuture.get()

            val previewView =
                findViewById<androidx.camera.view.PreviewView>(R.id.previewView)

            preview = Preview.Builder().build()
            preview.setSurfaceProvider(previewView.surfaceProvider)

            imageAnalysis = ImageAnalysis.Builder()
                .setTargetResolution(Size(1280, 720))
                .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                .build()

            val scanner = BarcodeScanning.getClient()

            imageAnalysis.setAnalyzer(ContextCompat.getMainExecutor(this)) { imageProxy ->
                val mediaImage = imageProxy.image

                if (mediaImage == null) {
                    imageProxy.close()
                    return@setAnalyzer
                }

                val image = InputImage.fromMediaImage(
                    mediaImage,
                    imageProxy.imageInfo.rotationDegrees
                )

                scanner.process(image)
                    .addOnSuccessListener { barcodes ->
                        for (barcode in barcodes) {
                            if (barcode.format == FORMAT_QR_CODE) {
                                handleQr(barcode.rawValue)
                                imageProxy.close()
                                return@addOnSuccessListener
                            }
                        }
                        imageProxy.close()
                    }
                    .addOnFailureListener {
                        imageProxy.close()
                    }
            }

            cameraProvider.unbindAll()
            cameraProvider.bindToLifecycle(
                this,
                CameraSelector.DEFAULT_BACK_CAMERA,
                preview,
                imageAnalysis
            )

        }, ContextCompat.getMainExecutor(this))
    }

    private fun handleQr(rawValue: String?) {
        if (qrHandled) return

        if (rawValue.isNullOrBlank()) {
            showErrorOnce("QR inválido")
            return
        }

        qrHandled = true

        lifecycleScope.launch {
            reclamarResiduo(rawValue)
        }
    }

    private suspend fun reclamarResiduo(idResiduo: String) {
        try {
            val api = RetrofitClient.getApiService(this)

            val body = mapOf(
                "id_residuo" to idResiduo
            )

            val response = api.reclamar(body)

            if (response.isSuccessful) {
                // OK → ir a pantalla de resultado
                val intent = Intent(this, ScanResultActivity::class.java).apply {
                    putExtra("KEY_ID", idResiduo)
                }
                startActivity(intent)

            } else {
                showErrorOnce("No se pudo reclamar el residuo")
                qrHandled = false
            }

        } catch (e: Exception) {
            showErrorOnce("Error de conexión con el servidor")
            qrHandled = false
        }
    }


    // Error una sola vez
    private fun showErrorOnce(msg: String) {
        if (errorShown) return
        errorShown = true
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }
}