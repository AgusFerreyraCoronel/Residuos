package com.example.residuos.rankings

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.residuos.R
import com.google.android.material.card.MaterialCardView

class RankingAdapter :
    RecyclerView.Adapter<RankingAdapter.VH>() {

    private val items = mutableListOf<RankingItem>()

    fun submit(list: List<RankingItem>) {
        items.clear()
        items.addAll(list)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_ranking, parent, false)
        return VH(view)
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: VH, position: Int) {
        val item = items[position]
        val rank = position + 1

        holder.tvPosition.text = rank.toString()
        holder.tvUsername.text = item.username
        holder.tvScore.text = "${item.score} pts"

        when (rank) {
            1 -> holder.root.setBackgroundResource(R.drawable.bg_rank_gold)
            2 -> holder.root.setBackgroundResource(R.drawable.bg_rank_silver)
            else -> holder.root.setBackgroundResource(R.drawable.bg_rank_normal)
        }
    }

    class VH(v: View) : RecyclerView.ViewHolder(v) {
        val root: MaterialCardView = v.findViewById(R.id.rootCard)
        val tvPosition: TextView = v.findViewById(R.id.tvPosition)
        val tvUsername: TextView = v.findViewById(R.id.tvUsername)
        val tvScore: TextView = v.findViewById(R.id.tvScore)
    }
}