package com.example.finanzapro.ui

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.finanzapro.R

class ConfigFragment : Fragment(R.layout.fragment_configuration) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // header titulo
        view.findViewById<TextView>(R.id.tvHeaderTitle).text = "Configuración"

        //  iconos
        view.findViewById<View>(R.id.btnSearch).visibility = View.GONE
        view.findViewById<View>(R.id.btnFilter).visibility = View.GONE
        view.findViewById<View>(R.id.cvUserProfile).visibility = View.VISIBLE
    }
}