package com.example.finanzapro.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.finanzapro.MainActivity //
import com.example.finanzapro.R
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.example.finanzapro.databinding.FragmentDashboardBinding


class DashboardFragment : Fragment(R.layout.fragment_dashboard) {


    private lateinit var btnRegisterSpent: FloatingActionButton
    private lateinit var binding: FragmentDashboardBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDashboardBinding.inflate(inflater, container, false)
        return binding.root
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