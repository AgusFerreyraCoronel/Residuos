package com.example.residuos.points

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.residuos.R

class PointsFragment : Fragment(R.layout.fragment_points) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val tvMyPoints = view.findViewById<TextView>(R.id.tvMyPoints)
        val btnRedeem = view.findViewById<Button>(R.id.btnRedeemPoints)

        val userPoints = 1250 // luego lo reemplazás por backend / ViewModel
        tvMyPoints.text = "Mis puntos: $userPoints"

        btnRedeem.setOnClickListener {
            findNavController().navigate(R.id.redeemPoints)
        }
    }
}