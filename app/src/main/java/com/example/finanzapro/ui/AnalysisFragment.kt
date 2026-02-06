package com.example.finanzapro

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import com.example.finanzapro.databinding.FragmentAnalysisBinding
import com.example.finanzapro.ui.RegisterFragment

class AnalysisFragment : Fragment(R.layout.fragment_analysis) {

    private lateinit var binding: FragmentAnalysisBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentAnalysisBinding.bind(view)

        binding.btnNew.setOnClickListener {
            Log.d("Boton btnNew :: ", "Presionado")
            (requireActivity() as MainActivity).replaceFragment(RegisterFragment())
        }
    }

}