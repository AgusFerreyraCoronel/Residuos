package com.example.residuos.scanResult

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.residuos.R
import com.example.residuos.databinding.FragmentScanResultBinding
import com.example.residuos.scanner.ScannerFragment


class ScanResultFragment : Fragment(R.layout.fragment_scan_result) {

    private var _binding: FragmentScanResultBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ScanResultViewModel by viewModels()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding = FragmentScanResultBinding.bind(view)

        val args = requireArguments()
        viewModel.setData(
            args.getString(ScannerFragment.KEY_ID),
            args.getInt(ScannerFragment.KEY_PUNTOS, 0),
            args.getString(ScannerFragment.KEY_TIPO)
        )

        viewModel.idResiduo.observe(viewLifecycleOwner) {
            binding.textIdResiduo.text = it
        }
        viewModel.puntos.observe(viewLifecycleOwner) {
            binding.textPuntos.text = it.toString()
        }
        viewModel.tipoResiduo.observe(viewLifecycleOwner) {
            binding.textTipoResiduo.text = it
        }

        /* Intenta ir a la camara pero no entra y vuelve a cargar el scan result.
        binding.buttonAccept.setOnClickListener {
            requireActivity()
                .supportFragmentManager
                .popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
        }*/

        binding.buttonAccept.setOnClickListener {
            findNavController().navigate(R.id.homeFragment)
        }



    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        fun newInstance(id: String, puntos: Int, tipo: String) =
            ScanResultFragment().apply {
                arguments = Bundle().apply {
                    putString(ScannerFragment.KEY_ID, id)
                    putInt(ScannerFragment.KEY_PUNTOS, puntos)
                    putString(ScannerFragment.KEY_TIPO, tipo)
                }
            }
    }
}