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
import com.example.finanzapro.adapter.ConfigViewModel
import com.example.finanzapro.adapter.TransactionAdapter
import com.example.finanzapro.adapter.TransactionViewModel
import com.example.finanzapro.databinding.FragmentDashboardBinding
import java.util.Calendar
import java.util.Locale

class DashboardFragment : Fragment(R.layout.fragment_dashboard) {

    private var _binding: FragmentDashboardBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: TransactionAdapter
    private lateinit var transactionViewModel: TransactionViewModel
    private lateinit var configViewModel: ConfigViewModel

    private var currentBudget: Double = 0.0
    private var currentSpent: Double = 0.0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        transactionViewModel = ViewModelProvider(requireActivity())[TransactionViewModel::class.java]
        configViewModel = ViewModelProvider(requireActivity())[ConfigViewModel::class.java]

        val userId = getUserIdOrToast()
        if (userId.isEmpty()) {
            showToast("No hay usuario autenticado - Dashboard")
            startActivity(Intent(requireContext(), LoginActivity::class.java))
            return
        }

        // 2. Configuración estética
        binding.customHeader.tvHeaderTitle.text = "Inicio"
        binding.customHeader.btnSearch.visibility = View.VISIBLE

        adapter = TransactionAdapter(emptyList())
        binding.rvDashboard.layoutManager = LinearLayoutManager(requireContext())
        binding.rvDashboard.adapter = adapter

        binding.registerSpent.setOnClickListener {
            (requireActivity() as MainActivity).replaceFragment(RegisterFragment())
        }

        loadData(userId)
    }

    private fun loadData(userId: String) {
        configViewModel.fetchUserData(userId)
        transactionViewModel.getTransactions(userId, "transactions")

        configViewModel.userProfile.observe(viewLifecycleOwner) { profile ->
            if (profile != null) {
                currentBudget = profile.monthly_budget
                updateBudgetUI()
            }
        }

        transactionViewModel.listTransactions.observe(viewLifecycleOwner) { transactions ->
            val calendar = Calendar.getInstance()
            val currentMonth = calendar.get(Calendar.MONTH)
            val currentYear = calendar.get(Calendar.YEAR)
            val monthlyTransactions = transactions.filter { transaction ->
                try {
                    val cal = Calendar.getInstance()
                    cal.timeInMillis = transaction.timestamp
                    cal.get(Calendar.MONTH) == currentMonth && cal.get(Calendar.YEAR) == currentYear
                } catch (e: Exception) {
                    false
                }
            }
            val last = if (monthlyTransactions.size > 4) {
                monthlyTransactions.take(4)
            } else {
                monthlyTransactions
            }
            adapter.getList(last)
            currentSpent = monthlyTransactions.sumOf { it.amount }
            updateBudgetUI()
        }
    }

    private fun updateBudgetUI() {
        binding.tvBudget.text = String.format(Locale.US, "%.2f", currentBudget)
        binding.tvTotalSpent.text = String.format(Locale.US, "%.2f", currentSpent)

        val remainingBudget = currentBudget - currentSpent
        binding.tvRemainingBudget.text = String.format(Locale.US, "S/ %.2f", remainingBudget)

        binding.progressCircle.max = currentBudget.toInt().coerceAtLeast(1)
        binding.progressCircle.progress = currentSpent.toInt()

        val savingsPercentage  = if (currentBudget > 0) {
            (remainingBudget / currentBudget) * 100
        } else 0.0
        binding.tvRestante.text = "RESTANTE"
        binding.tvPercentage.text = String.format("%.2f%%", savingsPercentage )
    }

    fun getUserIdOrToast(): String {
        val prefs = requireContext().getSharedPreferences("personal_data", Context.MODE_PRIVATE)
        return prefs.getString("userId", "") ?: ""
    }

    private fun Fragment.showToast(message: String, duration: Int = Toast.LENGTH_SHORT) {
        Toast.makeText(requireContext(), message, duration).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}