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
import com.google.android.material.card.MaterialCardView
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class TicketsAdapter(private val tickets: List<Ticket>) :
    RecyclerView.Adapter<TicketsAdapter.TicketViewHolder>() {

    class TicketViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val ivIcon: ImageView = view.findViewById(R.id.ivCategoryIcon)
        val cvIconBackground: MaterialCardView = view.findViewById(R.id.cvIconBackground)
        val tvName: TextView = view.findViewById(R.id.tvTicketName)
        val tvDate: TextView = view.findViewById(R.id.tvTicketDate)
        val tvAmount: TextView = view.findViewById(R.id.tvTicketAmount)
        val tvCategory: TextView = view.findViewById(R.id.tvTicketCategoryLabel)
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
            holder.tvDate.text = sdf.format(Date(ticket.uploadDate))
        } catch (e: Exception) {
            holder.tvDate.text = "Fecha desconocida"
        }

        holder.tvName.text = ticket.merchant
        holder.tvAmount.text = "S/ ${String.format("%.2f", ticket.amount)}"
        holder.tvCategory.text = getCategoryName(ticket.categoryId).uppercase()

        val (iconRes, colorHex, bgHex) = getCategoryStyle(ticket.categoryId)
        holder.ivIcon.setImageResource(iconRes)
        holder.ivIcon.setColorFilter(Color.parseColor(colorHex))
        holder.cvIconBackground.setCardBackgroundColor(Color.parseColor(bgHex))
    }

    private fun getCategoryName(categoryId: String): String {
        return when (categoryId) {
            "cat_food" -> "Alimentación"
            "cat_transport" -> "Transporte"
            "cat_home" -> "Hogar"
            "cat_entertainment" -> "Entretenimiento"
            "cat_health" -> "Salud"
            else -> "Otros"
        }
    }

    private fun getCategoryStyle(categoryId: String): Triple<Int, String, String> {
        return when (categoryId) {
            "cat_food" -> Triple(R.drawable.ic_food, "#FF5722", "#FFF0E6") // Naranja
            "cat_transport" -> Triple(R.drawable.ic_transport, "#2196F3", "#E3F2FD") // Azul
            "cat_home" -> Triple(R.drawable.ic_home, "#4CAF50", "#E8F5E9") // Verde
            "cat_entertainment" -> Triple(R.drawable.ic_entertainment, "#9C27B0", "#F3E5F5") // Morado
            "cat_health" -> Triple(R.drawable.ic_health, "#0D9488", "#E0F2F1") // Teal (Tu diseño)
            else -> Triple(R.drawable.ic_others, "#9E9E9E", "#F5F5F5") // Gris
        }
    }

    override fun getItemCount() = tickets.size
}