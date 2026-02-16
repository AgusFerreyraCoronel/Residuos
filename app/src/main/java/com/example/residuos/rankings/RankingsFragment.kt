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
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.AdapterView



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

        val spinner = view.findViewById<Spinner>(R.id.spinnerMaterial)

        val materiales = listOf(
            "Todos",
            "Plastico",
            "Vidrio",
            "Carton",
            "Metal",
            "Papel",
            "Basura"
        )

        val spinnerAdapter = ArrayAdapter(
            requireContext(),
            R.layout.item_spinner_selected,
            materiales
        )

        spinnerAdapter.setDropDownViewResource(R.layout.item_spinner_dropdown)
        spinner.adapter = spinnerAdapter

        // spinner listener
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                val selectedMaterial = parent?.getItemAtPosition(position).toString()

                if (selectedMaterial == "Todos") {
                    viewModel.loadRanking(null)
                } else {
                    viewModel.loadRanking(selectedMaterial)
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }
}