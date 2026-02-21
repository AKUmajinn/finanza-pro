package com.example.finanzapro.ui

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.finanzapro.R
import com.example.finanzapro.adapter.TransactionViewModel
import com.example.finanzapro.databinding.FragmentAnalysisBinding
import com.example.finanzapro.model.CategoryDistribution
import com.example.finanzapro.model.Transaction
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import androidx.core.graphics.toColorInt
import com.example.finanzapro.adapter.CategoryDistributionAdapter
import com.example.finanzapro.utils.Constrain

class AnalysisFragment : Fragment(R.layout.fragment_analysis) {

    private var _binding: FragmentAnalysisBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: TransactionViewModel
    private lateinit var categoryAdapter: CategoryDistributionAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        _binding = FragmentAnalysisBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(requireActivity())[TransactionViewModel::class.java]
        binding.customHeader.tvHeaderTitle.text = "Analisis"
        // Configurar RecyclerView para categorías
        setupRecyclerView()
        observeTransactions()
        // Obtener userId y cargar datos
        val userId = getUserIdOrToast()
        if (userId.isNotEmpty()) {
            loadTransactions(userId, "transactions")
        } else {
            showNoDataMessage()
        }
    }

    private fun setupRecyclerView() {
        binding.rvCategories.layoutManager = LinearLayoutManager(requireContext())
    }

    private fun observeTransactions() {
        viewModel.listTransactions.observe(viewLifecycleOwner) { transactions ->
            Log.d("AnalysisFragment", "Observer triggered with ${transactions.size} transactions")
            if (transactions.isEmpty()) {
                Log.d("AnalysisFragment", "No hay datos, mostrando mensaje vacío")
                showNoDataMessage()
            } else {
                Log.d("AnalysisFragment", "Hay datos, mostrando UI")
                showData()
                calculateMonthlySummary(transactions)
                calculateCategoryDistribution(transactions)
            }
        }
    }

    private fun loadTransactions(userId: String, table: String) {
        viewModel.getTransactions(userId, table)
    }

    @SuppressLint("SetTextI18n")
    private fun calculateMonthlySummary(transactions: List<Transaction>) {
        // Filtrar transacciones del mes actual
        val calendar = java.util.Calendar.getInstance()
        val currentMonth = calendar.get(java.util.Calendar.MONTH)
        val currentYear = calendar.get(java.util.Calendar.YEAR)

        val monthTransactions = transactions.filter { transaction ->
            try {
                val cal = java.util.Calendar.getInstance()
                cal.timeInMillis = transaction.timestamp
                cal.get(java.util.Calendar.MONTH) == currentMonth &&
                        cal.get(java.util.Calendar.YEAR) == currentYear
            } catch (e: Exception) {
                false
            }
        }

        // Calcular total gastado en el mes
        val totalSpent = monthTransactions.sumOf { it.amount }
        val daysWithTransactions = monthTransactions
            .map {
                val cal = java.util.Calendar.getInstance()
                cal.timeInMillis = it.timestamp
                cal.get(java.util.Calendar.DAY_OF_MONTH)  // Solo el día del mes
            }
            .distinct()
            .size

        val dailyAverage = if (daysWithTransactions > 0) {
            totalSpent / daysWithTransactions
        } else {
            0.0
        }

        // Actualizar UI
        binding.tvTotalSpent.text = "S/ ${"%.2f".format(totalSpent)}"
        binding.tvDailyAverage.text = "S/ ${"%.2f".format(dailyAverage)}"
        binding.tvMonthTotal.text = "S/ ${"%.2f".format(totalSpent)}"

        // Configurar mes y año
        binding.tvMonthYear.text = "${Constrain.MONTH_NAMES[currentMonth]} $currentYear"
    }

    private fun calculateCategoryDistribution(transactions: List<Transaction>) {
        // Filtrar transacciones del mes actual
        val calendar = java.util.Calendar.getInstance()
        val currentMonth = calendar.get(java.util.Calendar.MONTH)
        val currentYear = calendar.get(java.util.Calendar.YEAR)

        val monthTransactions = transactions.filter { transaction ->
            try {
                val cal = java.util.Calendar.getInstance()
                cal.timeInMillis = transaction.timestamp
                cal.get(java.util.Calendar.MONTH) == currentMonth &&
                        cal.get(java.util.Calendar.YEAR) == currentYear
            } catch (e: Exception) {
                false
            }
        }

        // Agrupar por categoría y sumar montos
        val categoryMap = mutableMapOf<String, Double>()
        monthTransactions.forEach { transaction ->
            val current = categoryMap[transaction.categoryId] ?: 0.0
            categoryMap[transaction.categoryId] = current + transaction.amount
        }

        val totalAmount = monthTransactions.sumOf { it.amount }

        // Crear lista de distribución
        val distribution = categoryMap.map { (category, amount) ->
            val percentage = if (totalAmount > 0) {
                ((amount / totalAmount) * 100).toInt()
            } else 0
            CategoryDistribution(getCategoryName(category), amount, percentage)
        }.sortedByDescending { it.amount }

        // Asignar adapter si hay datos
        if (distribution.isNotEmpty()) {
            binding.tvNoData.visibility = View.GONE
            binding.rvCategories.visibility = View.VISIBLE
            binding.cardPieChart.visibility = View.VISIBLE
            setupPieChart(distribution)
            categoryAdapter = CategoryDistributionAdapter(distribution)
            binding.rvCategories.adapter = categoryAdapter
        } else {
            showNoDataMessage()
        }
    }

    private fun getUserIdOrToast(): String {
        val prefs = requireContext().getSharedPreferences("personal_data", Context.MODE_PRIVATE)
        return prefs.getString("userId", null) ?: ""
    }

    private fun showNoDataMessage() {
        binding.tvNoData.visibility = View.VISIBLE
        binding.rvCategories.visibility = View.GONE
        binding.cardPieChart.visibility = View.GONE

        // Valores por defecto
        binding.tvTotalSpent.text = "S/ 0.00"
        binding.tvDailyAverage.text = "S/ 0.00"
        binding.tvMonthTotal.text = "S/ 0.00"

        val calendar = java.util.Calendar.getInstance()
        val currentMonth = calendar.get(java.util.Calendar.MONTH)
        val currentYear = calendar.get(java.util.Calendar.YEAR)
        binding.tvMonthYear.text = "${Constrain.MONTH_NAMES[currentMonth]} $currentYear"
    }

    private fun showData() {
        binding.tvNoData.visibility = View.GONE
        binding.rvCategories.visibility = View.VISIBLE
        binding.cardPieChart.visibility = View.VISIBLE
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    private fun setupPieChart(distribution: List<CategoryDistribution>) {
        val pieChart = binding.pieChart // Asegúrate de tener este ID en tu layout

        val entries = distribution.map { category ->
            PieEntry(category.amount.toFloat(), category.name)
        }

        val dataSet = PieDataSet(entries, "Categorías")
        dataSet.colors = distribution.map { category ->
            getCategoryColor(category.name).toColorInt()
        }

        dataSet.valueTextSize = 12f
        dataSet.valueTextColor = Color.WHITE

        val data = PieData(dataSet)
        pieChart.data = data
        pieChart.description.isEnabled = false
        pieChart.centerText = "Gastos"
        pieChart.animateY(1000)
        pieChart.invalidate()
    }


    private fun getCategoryName(categoryId: String): String {
        return when (categoryId) {
            "cat_food" -> "Alimentación"
            "cat_transport" -> "Transporte"
            "cat_home" -> "Hogar"
            "cat_entertainment" -> "Entretenimiento"
            "cat_health" -> "Salud"
            "cat_education" -> "Educación"
            "cat_clothing" -> "Ropa"
            "cat_technology" -> "Tecnología"
            "cat_travel" -> "Viajes"
            "cat_others" -> "Otros"
            else -> categoryId.replace("cat_", "").replaceFirstChar { it.uppercase() }
        }
    }

    private fun getCategoryColor(categoryName: String): String {
        return when (categoryName) {
            "Alimentación" -> "#4CAF50"      // Verde
            "Transporte" -> "#2196F3"         // Azul
            "Vivienda", "Hogar" -> "#FF7043"  // Naranja claro
            "Entretenimiento" -> "#FF9800"     // Naranja
            "Salud" -> "#F44336"               // Rojo
            "Educación" -> "#9C27B0"           // Púrpura
            "Ropa" -> "#EC407A"                 // Rosa
            "Tecnología" -> "#7B1FA2"           // Púrpura oscuro
            "Viajes" -> "#29B6F6"               // Celeste
            "Otros" -> "#9E9E9E"                 // Gris
            else -> "#008382"                    // Color por defecto (verde app)
        }
    }
}