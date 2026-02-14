package com.example.finanzapro.ui

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.finanzapro.MainActivity //
import com.example.finanzapro.R

class DashboardFragment : Fragment(R.layout.fragment_dashboard) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // header
        view.findViewById<TextView>(R.id.tvHeaderTitle).text = "Inicio"
        view.findViewById<View>(R.id.btnSearch).visibility = View.VISIBLE


        val btnRegister = view.findViewById<Button>(R.id.btnRegisterTransaction)
        btnRegister.setOnClickListener {
            (requireActivity() as MainActivity).replaceFragment(RegisterFragment())
        }
    }
}