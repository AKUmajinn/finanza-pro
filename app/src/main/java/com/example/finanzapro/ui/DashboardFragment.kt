package com.example.finanzapro.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.finanzapro.LoginActivity
import com.example.finanzapro.MainActivity
import com.example.finanzapro.R
import com.example.finanzapro.adapter.TransactionAdapter
import com.example.finanzapro.adapter.TransactionViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.example.finanzapro.databinding.FragmentDashboardBinding
import com.example.finanzapro.model.Transaction

class DashboardFragment : Fragment(R.layout.fragment_dashboard) {

    private lateinit var btnRegisterSpent: FloatingActionButton
    private var _binding: FragmentDashboardBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: TransactionAdapter
    private lateinit var viewModel: TransactionViewModel
    private lateinit var trans: List<Transaction>

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(requireActivity())[TransactionViewModel::class.java]
        val userId = getUserIdOrToast()
        if (userId.isEmpty()) {
            showToast("No hay usuario autenticado - Dashboard")
            val intent = Intent(requireContext(), LoginActivity::class.java)
            startActivity(intent)
        }
        adapter = TransactionAdapter(emptyList())
        binding.customHeader.tvHeaderTitle.text = "Inicio"
        binding.customHeader.btnSearch.visibility = View.VISIBLE

        binding.rvDashboard.layoutManager = LinearLayoutManager(requireContext())
        binding.rvDashboard.adapter = adapter
        btnRegisterSpent = view.findViewById(R.id.register_spent)
        btnRegisterSpent.setOnClickListener {
            (requireActivity() as MainActivity).replaceFragment(RegisterFragment())
        }

        loadTransactions(userId, "transactions")
    }

    fun loadTransactions(userId: String, table: String) {
        viewModel.getTransactions(userId, table)
        viewModel.listTransactions.observe(viewLifecycleOwner) { transactions ->
            adapter.getList(transactions)
            trans = transactions
            val totalSpent = transactions.sumOf { it.amount }
            binding.tvTotalSpent.text = totalSpent.toString()
            val remainingBudget = 1000.0 - totalSpent
            binding.tvRemainingBudget.text = remainingBudget.toString()
        }
    }

    fun getUserIdOrToast(): String {
        val prefs = requireContext().getSharedPreferences("personal_data", Context.MODE_PRIVATE)
        val userId = prefs.getString("userId", null)

        if (userId.isNullOrEmpty()) {
            showToast("No hay usuario autenticado")
            return ""
        }
        return userId
    }

    private fun Fragment.showToast(message: String, duration: Int = Toast.LENGTH_SHORT) {
        Toast.makeText(requireContext(), message, duration).show()
    }
}