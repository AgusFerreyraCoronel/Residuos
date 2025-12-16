package com.example.residuos.scanner

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Size
import androidx.activity.ComponentActivity
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.residuos.R
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.barcode.common.Barcode.FORMAT_QR_CODE
import com.google.mlkit.vision.common.InputImage
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST
import android.app.AlertDialog
import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.annotation.OptIn
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import org.json.JSONObject
import com.example.residuos.scanResult.ScanResultActivity
import org.json.JSONException

class ScannerActivity : ComponentActivity() {

    private lateinit var preview: Preview
    private lateinit var imageAnalysis: ImageAnalysis

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

            val previewView = findViewById<androidx.camera.view.PreviewView>(R.id.previewView)

            preview = Preview.Builder().build()
            preview.setSurfaceProvider(previewView.surfaceProvider)

            imageAnalysis = ImageAnalysis.Builder()
                .setTargetResolution(Size(1280, 720))
                .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                .build()

            val scanner = BarcodeScanning.getClient()

            imageAnalysis.setAnalyzer(ContextCompat.getMainExecutor(this)) { imageProxy ->

                val mediaImage = imageProxy.image
                if (mediaImage != null) {
                    val image = InputImage.fromMediaImage(mediaImage, imageProxy.imageInfo.rotationDegrees)

                    scanner.process(image)
                        .addOnSuccessListener { barcodes ->
                            for (barcode in barcodes) {
                                if (barcode.format == FORMAT_QR_CODE) {
                                    val qrValue = barcode.rawValue ?: ""
                                    onQrDetected(qrValue)

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
            }

            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

            cameraProvider.unbindAll()
            cameraProvider.bindToLifecycle(
                this,
                cameraSelector,
                preview,
                imageAnalysis
            )

        }, ContextCompat.getMainExecutor(this))
    }

    private var qrHandled = false

    private fun onQrDetected(qrValue: String) {
        if (qrHandled) return
        qrHandled = true
        //Ejemplo de valor de qr {"ID Residuo": "124:Yywc-F-N188FkQlGn2NCvpFEyt6jd379a0kaG_089Yk", "Puntos": 15, "Tipo Residuo": "Plastico"}
        Log.d("QR_SCAN", "Valor del QR: $qrValue")

        lifecycleScope.launch {
            //sendResiduoToServer(qrValue)
            showResiduoInfo(qrValue)
        }
    }

    // ---- Retrofit API ----

    interface ApiService {
        @POST("/api/residuo/reclamar/")
        suspend fun reclamarResiduo(
            @Body body: Map<String, String>,
            @Header("Authorization") token: String
        ): retrofit2.Response<ReclamoResponse>
    }

    data class ReclamoResponse(
        val message: String,
        val tipo: String?,
        val puntos: Int?
    )

    private suspend fun sendResiduoToServer(id: String) {
        val retrofit = Retrofit.Builder()
            //.baseUrl("http://10.0.2.2:8000/")
            .baseUrl("http://192.168.0.101:8000/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val api = retrofit.create(ApiService::class.java)

        val token = getSharedPreferences("auth", MODE_PRIVATE)
            .getString("access_token", "") ?: ""

        val body = mapOf("residuo_id" to id)

        val response = api.reclamarResiduo(body, "Bearer $token")

        if (!response.isSuccessful) {
            runOnUiThread {
                Toast.makeText(this, "Error: ${response.code()}", Toast.LENGTH_LONG).show()
            }
            return
        }

        val data = response.body() ?: return

        runOnUiThread {
            AlertDialog.Builder(this)
                .setTitle("Resultado")
                .setMessage(
                    """
                    ${data.message}
                    
                    Tipo: ${data.tipo ?: "N/A"}
                    Puntos: ${data.puntos ?: 0}
                    """.trimIndent()
                )
                .setPositiveButton("OK") { _, _ -> finish() }
                .show()
        }
    }
    private fun showResiduoInfo(qrValue: String) {
        try {
            // 1. Parsear el String JSON (Esto se ejecuta en el dispatcher de la coroutine)
            val jsonObject = JSONObject(qrValue)
            val idResiduo = jsonObject.getString("ID Residuo")
            val puntos = jsonObject.getInt("Puntos")
            val tipoResiduo = jsonObject.getString("Tipo Residuo")
            Log.d("QR_SCAN", "Valor del JSON generado: $jsonObject")

            // 2. Crear el Intent y pasar los datos
            // NOTA: 'this' en un Fragment/Activity dentro de un método de extensión
            // o coroutine puede requerir ser explícito (ej. requireContext() o nombreClase.this)
            val intent = Intent(this, ScanResultActivity::class.java).apply {
                putExtra("KEY_ID", idResiduo)
                putExtra("KEY_PUNTOS", puntos)
                putExtra("KEY_TIPO", tipoResiduo)
            }

            // 3. Iniciar la nueva Activity (Debe ejecutarse en el Main thread)
            // Dado que estamos usando 'lifecycleScope.launch', ya estamos en el Main thread
            // a menos que se haya cambiado el dispatcher explícitamente.
            startActivity(intent)

        } catch (e: JSONException) {
            Log.e("QR_SCAN_ERROR", "Error al parsear el JSON del QR: ${e.message}")
            // Puedes considerar restablecer qrHandled si el error es fatal para reintentar.
            // qrHandled = false
        }
    }
}