package com.example.finanzapro.ui

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.finanzapro.MainActivity
import com.example.finanzapro.R
import com.example.finanzapro.adapter.ConfigViewModel
import com.google.android.material.textfield.TextInputEditText

class EditBudgetFragment : Fragment(R.layout.fragment_edit_budget) {

    private lateinit var configViewModel: ConfigViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        configViewModel = ViewModelProvider(requireActivity())[ConfigViewModel::class.java]

        // Configuración de la cabecera
        view.findViewById<TextView>(R.id.tvHeaderTitle).text = "Editar Presupuesto"
        view.findViewById<View>(R.id.btnSearch).visibility = View.GONE
        view.findViewById<View>(R.id.btnFilter).visibility = View.GONE

        val etNewBudget = view.findViewById<TextInputEditText>(R.id.etNewBudget)

        // Pre-cargar el presupuesto actual
        configViewModel.userProfile.value?.let { profile ->
            if (profile.monthly_budget > 0) {
                etNewBudget.setText(profile.monthly_budget.toString())
            }
        }

        view.findViewById<Button>(R.id.btnSaveBudget).setOnClickListener {
            val newBudgetStr = etNewBudget.text.toString().trim()

            if (newBudgetStr.isNotEmpty() && newBudgetStr.toDoubleOrNull() != null) {
                val newBudget = newBudgetStr.toDouble()
                val prefs = requireContext().getSharedPreferences("personal_data", Context.MODE_PRIVATE)
                val userId = prefs.getString("userId", "") ?: ""

                if (userId.isNotEmpty()) {
                    configViewModel.updateBudget(userId, newBudget) { success ->
                        if (success) {
                            Toast.makeText(requireContext(), "Presupuesto actualizado", Toast.LENGTH_SHORT).show()
                            (requireActivity() as MainActivity).replaceFragment(ConfigFragment())
                        } else {
                            Toast.makeText(requireContext(), "Error al guardar", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            } else {
                Toast.makeText(requireContext(), "Ingresa un monto válido", Toast.LENGTH_SHORT).show()
            }
        }

        view.findViewById<Button>(R.id.btnCancel).setOnClickListener {
            (requireActivity() as MainActivity).replaceFragment(ConfigFragment())
        }
    }
}