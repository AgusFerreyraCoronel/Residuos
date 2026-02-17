package com.example.residuos.myRewards

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.navigation.fragment.findNavController
import com.example.residuos.R

class MyRewardsFragment : Fragment(R.layout.fragment_my_rewards) {

    private val viewModel: MyRewardsViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val container = view.findViewById<LinearLayout>(R.id.containerRewards)

        viewModel.rewards.observe(viewLifecycleOwner) { rewards ->

            container.removeAllViews()

            rewards.forEach { reward ->

                val item = layoutInflater.inflate(
                    R.layout.item_redeem_option,
                    container,
                    false
                )

                item.findViewById<TextView>(R.id.tvOption).text = reward.descripcion
                item.findViewById<TextView>(R.id.tvCost).visibility = View.GONE


                item.setOnClickListener {

                    val bundle = Bundle().apply {
                        putString("qrMessage", reward.descripcion)
                    }

                    findNavController().navigate(
                        R.id.action_myRewardsFragment_to_redeemQrFragment,
                        bundle
                    )
                }

                container.addView(item)
            }
        }

        viewModel.loadMyRewards()
    }
}
