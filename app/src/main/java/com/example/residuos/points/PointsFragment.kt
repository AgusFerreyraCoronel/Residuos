package com.example.residuos.points

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.residuos.R

class PointsFragment : Fragment(R.layout.fragment_points) {

    private val viewModel: PointsViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val tvMyPoints = view.findViewById<TextView>(R.id.tvMyPoints)
        val btnRedeem = view.findViewById<Button>(R.id.btnRedeemPoints)
        val btnMyRewards = view.findViewById<Button>(R.id.btnMyRewards)

        btnMyRewards.setOnClickListener {
            findNavController().navigate(R.id.myRewardsFragment)
        }

        viewModel.points.observe(viewLifecycleOwner) { points ->
            tvMyPoints.text = "Mis puntos: $points"
        }

        viewModel.error.observe(viewLifecycleOwner) {
            Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
        }

        btnRedeem.setOnClickListener {
            findNavController().navigate(R.id.redeemPoints)
        }

        viewModel.loadMyPoints()
    }
}