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
                        if (barcodes.isEmpty()) {
                            imageProxy.close()
                            return@addOnSuccessListener
                        }

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
            showError("QR vacío o inválido")
            return
        }

        try {
            val json = JSONObject(rawValue)

            val id = json.getString("ID Residuo")
            val puntos = json.getInt("Puntos")
            val tipo = json.getString("Tipo Residuo")

            qrHandled = true

            val intent = Intent(this, ScanResultActivity::class.java).apply {
                putExtra(KEY_ID, id)
                putExtra(KEY_PUNTOS, puntos)
                putExtra(KEY_TIPO, tipo)
            }

            startActivity(intent)

        } catch (e: JSONException) {
            showError("QR inválido. Formato incorrecto")
            Log.e("QR_SCAN", "Error JSON: ${e.message}")
        }
    }

    private fun showError(msg: String) {
        lifecycleScope.launch {
            Toast.makeText(this@ScannerActivity, msg, Toast.LENGTH_SHORT).show()
        }
    }
}