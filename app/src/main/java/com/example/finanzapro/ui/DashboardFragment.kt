package com.example.finanzapro.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.finanzapro.MainActivity //
import com.example.finanzapro.R
import com.example.finanzapro.adapter.DashboardAdapter
import com.example.finanzapro.adapter.TransactionAdapter
import com.example.finanzapro.adapter.TransactionViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.example.finanzapro.databinding.FragmentDashboardBinding
import com.example.finanzapro.model.Transaction


class DashboardFragment : Fragment(R.layout.fragment_dashboard) {


    private lateinit var btnRegisterSpent: FloatingActionButton
    private lateinit var binding: FragmentDashboardBinding

    private lateinit var adapter: DashboardAdapter

    private lateinit var adapterTransaction: TransactionAdapter
    private lateinit var viewModel: TransactionViewModel

    //var transactionEdit = Transaction()



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDashboardBinding.inflate(inflater, container, false)

        viewModel = ViewModelProvider(requireActivity())[TransactionViewModel::class.java]

        viewModel.listTransactions.observe(viewLifecycleOwner) { transactions ->
            setupRecyclerView(transactions)
        }

        return binding.root

    }

    fun  setupRecyclerView(listTransaction: List<Transaction>){
        adapterTransaction = TransactionAdapter(listTransaction, ::deleteTransaction, ::updateTransaction)
        binding.rvDashboard.adapter = adapterTransaction

    }

    fun deleteTransaction(id: String){
        viewModel.deleteTransaction(id)
    }

    fun updateTransaction(transaction: Transaction){
        viewModel.selectTransaction(transaction)

        (requireActivity() as MainActivity).replaceFragment(RegisterFragment())
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        // header, se cambio por el binding
        binding.customHeader.tvHeaderTitle.text = "Inicio"
        binding.customHeader.btnSearch.visibility = View.VISIBLE

        btnRegisterSpent= view.findViewById(R.id.register_spent)
        btnRegisterSpent.setOnClickListener {
            (requireActivity() as MainActivity).replaceFragment(RegisterFragment())
        }
    }
}