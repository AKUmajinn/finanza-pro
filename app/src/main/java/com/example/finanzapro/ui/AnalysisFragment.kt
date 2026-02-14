package com.example.finanzapro.ui

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.finanzapro.MainActivity
import com.example.finanzapro.R
import com.example.finanzapro.adapter.TransactionAdapter
import com.example.finanzapro.adapter.TransactionViewModel
import com.example.finanzapro.databinding.FragmentAnalysisBinding

class AnalysisFragment : Fragment(R.layout.fragment_analysis) {

    private lateinit var binding: FragmentAnalysisBinding
    private lateinit var adapter: TransactionAdapter
    private lateinit var viewModel: TransactionViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentAnalysisBinding.bind(view)
        viewModel = ViewModelProvider(requireActivity())[TransactionViewModel::class.java]

        binding.btnNew.setOnClickListener {
            Log.d("Boton btnNew :: ", "Presionado")
            (requireActivity() as MainActivity).replaceFragment(RegisterFragment())
        }
        getTransaction()
    }

    fun getTransaction() {
        var list = viewModel.getTransactions()
        Log.e("Lista de transacciones :: ", list.toString())
    }
}