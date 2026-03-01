package com.example.finanzapro.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.finanzapro.LoginActivity
import com.example.finanzapro.MainActivity
import com.example.finanzapro.R
import com.example.finanzapro.adapter.ConfigViewModel
import com.example.finanzapro.adapter.TransactionViewModel
import com.google.android.material.progressindicator.LinearProgressIndicator
import com.google.firebase.auth.FirebaseAuth
import java.util.Calendar
import java.util.Locale

class ConfigFragment : Fragment(R.layout.fragment_configuration) {

    private lateinit var configViewModel: ConfigViewModel
    private lateinit var transactionViewModel: TransactionViewModel

    private lateinit var tvBudgetAmount: TextView
    private lateinit var pbBudget: LinearProgressIndicator
    private lateinit var tvSpent: TextView
    private lateinit var tvRemaining: TextView
    private val auth = FirebaseAuth.getInstance()
    private var currentBudget: Double = 0.0
    private var currentSpent: Double = 0.0

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        configViewModel = ViewModelProvider(requireActivity())[ConfigViewModel::class.java]
        transactionViewModel = ViewModelProvider(requireActivity())[TransactionViewModel::class.java]

        view.findViewById<TextView>(R.id.tvHeaderTitle).text = "Configuración"
        view.findViewById<View>(R.id.btnSearch).visibility = View.GONE
        view.findViewById<View>(R.id.btnFilter).visibility = View.GONE

        tvBudgetAmount = view.findViewById(R.id.tvBudgetAmount)
        pbBudget = view.findViewById(R.id.pbBudget)
        tvSpent = view.findViewById(R.id.tvSpent)
        tvRemaining = view.findViewById(R.id.tvRemaining)

        setupLogout(view)

        // Escucha el clic en el botón de lápiz para editar el presupuesto
        view.findViewById<ImageView>(R.id.btnEditBudget).setOnClickListener {
            (requireActivity() as MainActivity).replaceFragment(EditBudgetFragment())
        }

        val userId = getUserIdFromPrefs()
        if (userId.isNotEmpty()) {
            configViewModel.fetchUserData(userId)
            transactionViewModel.getTransactions(userId, "transactions")
        }

        setupObservers()
        showCurrentUser()
    }

    private fun setupObservers() {
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

            currentSpent = monthlyTransactions.sumOf { it.amount }
            updateBudgetUI()
        }
    }

    private fun updateBudgetUI() {
        tvBudgetAmount.text = String.format(Locale.US, "S/ %,.2f", currentBudget)
        tvSpent.text = String.format(Locale.US, "GASTADO: S/ %,.2f", currentSpent)

        val remaining = currentBudget - currentSpent
        tvRemaining.text = String.format(Locale.US, "RESTANTE: S/ %,.2f", remaining)

        // Usar coerceAtLeast(1) evita errores de división por cero en la barra de progreso
        pbBudget.max = currentBudget.toInt().coerceAtLeast(1)
        pbBudget.progress = currentSpent.toInt()
    }

    private fun getUserIdFromPrefs(): String {
        val prefs = requireContext().getSharedPreferences("personal_data", Context.MODE_PRIVATE)
        return prefs.getString("userId", "") ?: ""
    }

    private fun setupLogout(view: View) {
        view.findViewById<Button>(R.id.btnSignOut).setOnClickListener {
            auth.signOut()
            requireContext().getSharedPreferences("personal_data", Context.MODE_PRIVATE)
                .edit().clear().apply()

            startActivity(Intent(requireContext(), LoginActivity::class.java))
            requireActivity().finish()
        }
    }

    private fun showCurrentUser() {
        val user = auth.currentUser
        if (user != null) {
            val tvUser = view?.findViewById<TextView>(R.id.tvUser)
            tvUser?.text = "Bienvenido: ${user.email}"
        }
    }
}