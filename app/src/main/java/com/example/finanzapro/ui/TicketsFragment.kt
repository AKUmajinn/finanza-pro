package com.example.finanzapro.ui

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
import com.example.finanzapro.adapter.TicketsAdapter
import com.example.finanzapro.adapter.TicketsViewModel
import com.example.finanzapro.databinding.FragmentMyTicketsBinding
import com.example.finanzapro.utils.SessionManager

class TicketsFragment : Fragment() {

    private var _binding: FragmentMyTicketsBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: TicketsAdapter
    private lateinit var viewModel: TicketsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMyTicketsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(requireActivity())[TicketsViewModel::class.java]

        val userId = SessionManager.getUserId(requireContext())
        if (userId.isNullOrEmpty()) {
            Toast.makeText(requireContext(), "No hay usuario autenticado", Toast.LENGTH_SHORT).show()
            startActivity(Intent(requireContext(), LoginActivity::class.java))
            return
        }

        binding.customHeader.tvHeaderTitle.text = "Mis Boletas"
        binding.customHeader.btnSearch.visibility = View.VISIBLE
        binding.customHeader.btnFilter.visibility = View.VISIBLE

        adapter = TicketsAdapter(emptyList())
        binding.rvTickets.layoutManager = LinearLayoutManager(requireContext())
        binding.rvTickets.adapter = adapter

        binding.fabScan.setOnClickListener {
            (requireActivity() as MainActivity).replaceFragment(RegisterFragment())
        }

        loadTickets(userId)
    }

    private fun loadTickets(userId: String) {
        viewModel.getTickets(userId)

        viewModel.listTickets.observe(viewLifecycleOwner) { tickets ->
            adapter.updateList(tickets)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}