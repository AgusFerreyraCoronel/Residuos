package com.example.residuos.redeemQr

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.example.residuos.R
import com.example.residuos.utils.generateQr

class RedeemQrFragment : Fragment(R.layout.fragment_redeem_qr) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val message = arguments?.getString("qrMessage")
            ?: return

        val qrBitmap = generateQr(message)

        val imageView = view.findViewById<ImageView>(R.id.ivQr)
        imageView.setImageBitmap(qrBitmap)
    }
}
