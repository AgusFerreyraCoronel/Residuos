package com.example.residuos.redeemPoints

import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.residuos.R
import kotlin.getValue

class RedeemPointsFragment : Fragment(R.layout.fragment_redeem_points) {

    private val viewModel: RedeemPointsViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.loadMyPoints()

        val container = view.findViewById<LinearLayout>(R.id.optionsContainer)

        val options = listOf(
            RedeemOption("Descuento en el buffet", 100),
            RedeemOption("Descuento en la fotocopiadora", 200)
        )

        options.forEach { option ->
            val item = layoutInflater.inflate(
                R.layout.item_redeem_option,
                container,
                false
            )

            item.findViewById<TextView>(R.id.tvOption).text = option.title
            item.findViewById<TextView>(R.id.tvCost).text = "${option.cost} pts"

            item.setOnClickListener {
                if (viewModel.isItEnough(option.cost)) {
                    val qrMessage = "Cupón de descuento para ${option.title.lowercase()}"

                    val bundle = Bundle().apply {
                        putString("qrMessage", qrMessage)
                    }

                    findNavController().navigate(
                        R.id.action_redeemPointsFragment_to_qrFragment,
                        bundle
                    )
                } else {
                    Toast.makeText(
                        requireContext(),
                        "No posee puntos suficientes para este canje",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            container.addView(item)
        }
    }

    private fun openQrScreen(option: RedeemOption) {
        val bundle = bundleOf(
            "TITLE" to option.title,
            "COST" to option.cost
        )

        findNavController().navigate(
            R.id.action_redeemPointsFragment_to_qrFragment,
            bundle
        )
    }
}