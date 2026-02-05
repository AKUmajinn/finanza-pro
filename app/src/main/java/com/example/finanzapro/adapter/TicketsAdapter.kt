package com.example.finanzapro.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.finanzapro.R
import com.example.finanzapro.model.Ticket
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class TicketsAdapter(private val tickets: List<Ticket>) :
    RecyclerView.Adapter<TicketsAdapter.TicketViewHolder>() {

    class TicketViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val ivImage: ImageView = view.findViewById(R.id.ivTicketImage)
        val tvStatus: TextView = view.findViewById(R.id.tvStatus)
        val tvDate: TextView = view.findViewById(R.id.tvDate)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TicketViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_ticket, parent, false)
        return TicketViewHolder(view)
    }

    override fun onBindViewHolder(holder: TicketViewHolder, position: Int) {
        val ticket = tickets[position]

        try {
            val sdf = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
            // x1 para mantener el formato
            holder.tvDate.text = sdf.format(Date(ticket.uploadDate))
        } catch (e: Exception) {
            holder.tvDate.text = "Fecha desconocida"
        }

        if (ticket.status == "processed") {
            holder.tvStatus.text = "Procesado"
            holder.tvStatus.setTextColor(Color.parseColor("#4CAF50")) // Verde
        } else {
            holder.tvStatus.text = "Pendiente"
            holder.tvStatus.setTextColor(Color.parseColor("#FF9800")) // Naranja
        }

        // ToDo: yo habia ponido una imagen aqui
        holder.ivImage.setBackgroundColor(Color.LTGRAY)
    }

    override fun getItemCount() = tickets.size
}