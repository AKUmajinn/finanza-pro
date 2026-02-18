package com.example.finanzapro.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.finanzapro.LoginActivity
import com.example.finanzapro.R
import com.example.finanzapro.model.UserProfile
import com.google.android.material.progressindicator.LinearProgressIndicator
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class ConfigFragment : Fragment(R.layout.fragment_configuration) {
    private lateinit var tvBudgetAmount: TextView
    private lateinit var pbBudget: LinearProgressIndicator

    private lateinit var auth: FirebaseAuth

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<TextView>(R.id.tvHeaderTitle).text = "Configuración"
        view.findViewById<View>(R.id.btnSearch).visibility = View.GONE
        view.findViewById<View>(R.id.btnFilter).visibility = View.GONE
        view.findViewById<View>(R.id.cvUserProfile).visibility = View.VISIBLE

        tvBudgetAmount = view.findViewById(R.id.tvBudgetAmount)
        pbBudget = view.findViewById(R.id.pbBudget)

        loadConfigFromFirebase()
        auth = FirebaseAuth.getInstance()
        val btnSignOut = view.findViewById<Button>(R.id.btnSignOut)
        btnSignOut.setOnClickListener {
            auth.signOut()
            val prefs = requireContext().getSharedPreferences("personal_data", Context.MODE_PRIVATE).edit()
            prefs.remove("email")
            prefs.remove("userId")
            prefs.apply()

            val intent = Intent(requireContext(), LoginActivity::class.java)
            startActivity(intent)
        }
    }

    private fun loadConfigFromFirebase() {
        val database = Firebase.database
        val myRef = database.getReference("users/user_123/profile")

        myRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val profile = snapshot.getValue(UserProfile::class.java)

                if (profile != null) {
                    val montoFormateado = String.format("%,.2f", profile.monthly_budget)
                    tvBudgetAmount.text = "S/ $montoFormateado"
                    pbBudget.max = profile.monthly_budget.toInt()
                    pbBudget.progress = 850 //hasta que se tenga el calculo
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(context, "Error cargando datos: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}