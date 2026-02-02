package com.example.residuos.rankings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.residuos.R

class RankingsFragment : Fragment() {

    private lateinit var adapter: RankingAdapter
    private val viewModel: RankingsViewModel by viewModels {
        RankingsViewModelFactory(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View =
        inflater.inflate(R.layout.fragment_rankings, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        adapter = RankingAdapter()

        val rv = view.findViewById<RecyclerView>(R.id.rvRanking)
        val progress = view.findViewById<ProgressBar>(R.id.progress)

        rv.layoutManager = LinearLayoutManager(requireContext())
        rv.adapter = adapter

        viewModel.ranking.observe(viewLifecycleOwner) {
            adapter.submit(it)
        }

        viewModel.loading.observe(viewLifecycleOwner) {
            progress.visibility = if (it) View.VISIBLE else View.GONE
        }

        viewModel.loadRanking()
    }
}