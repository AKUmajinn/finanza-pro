package com.example.finanzapro.ui

import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.finanzapro.MainActivity
import com.example.finanzapro.R
import com.example.finanzapro.adapter.TicketsAdapter
import com.example.finanzapro.model.Ticket
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class TicketsFragment : Fragment(R.layout.fragment_my_tickets) {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: TicketsAdapter
    private val ticketList = mutableListOf<Ticket>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<TextView>(R.id.tvHeaderTitle).text = "Mis Boletas"
        view.findViewById<View>(R.id.btnSearch).visibility = View.VISIBLE
        view.findViewById<View>(R.id.btnFilter).visibility = View.VISIBLE

        recyclerView = view.findViewById(R.id.rvTickets)
        recyclerView.layoutManager = LinearLayoutManager(context)

        adapter = TicketsAdapter(ticketList)
        recyclerView.adapter = adapter

        loadTicketsFromFirebase()
    }

    private fun loadTicketsFromFirebase() {
        val database = Firebase.database
        val myRef = database.getReference("users/user_123/tickets")

        myRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                ticketList.clear()

                for (postSnapshot in snapshot.children) {
                    val ticket = postSnapshot.getValue(Ticket::class.java)

                    if (ticket != null) {
                        ticketList.add(ticket)
                    }
                }

                adapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(context, "Error cargando datos: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}