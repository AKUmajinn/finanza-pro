package com.example.finanzapro.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.finanzapro.databinding.ItemTicketBinding
import com.example.finanzapro.model.Transaction
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class TicketsAdapter(private var tickets: List<Transaction>) :
    RecyclerView.Adapter<TicketItemViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TicketItemViewHolder {
        val binding = ItemTicketBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TicketItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TicketItemViewHolder, position: Int) {
        val transaction = tickets[position]

        // formateo del timestamp par fecha
        val sdf = SimpleDateFormat("dd MMM yyyy hh:mm", Locale.getDefault())
        holder.binding.tvTicketDate.text = sdf.format(Date(transaction.timestamp))

        holder.binding.tvTicketName.text = transaction.description
        holder.binding.tvTicketAmount.text = "S/ ${String.format(Locale.US, "%.2f", transaction.amount)}"
        holder.binding.tvTicketCategoryLabel.text = getCategoryName(transaction.categoryId).uppercase()

        val (iconRes, colorHex, bgHex) = getCategoryStyle(transaction.categoryId)
        holder.binding.ivCategoryIcon.setImageResource(iconRes)
        holder.binding.ivCategoryIcon.setColorFilter(Color.parseColor(colorHex))
        holder.binding.cvIconBackground.setCardBackgroundColor(Color.parseColor(bgHex))
    }

    override fun getItemCount() = tickets.size

    fun updateList(newList: List<Transaction>) {
        tickets = newList
        notifyDataSetChanged()
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
            "cat_food" -> Triple(com.example.finanzapro.R.drawable.ic_food, "#FF5722", "#FFF0E6")
            "cat_transport" -> Triple(com.example.finanzapro.R.drawable.ic_transport, "#2196F3", "#E3F2FD")
            "cat_home" -> Triple(com.example.finanzapro.R.drawable.ic_home, "#4CAF50", "#E8F5E9")
            "cat_entertainment" -> Triple(com.example.finanzapro.R.drawable.ic_entertainment, "#9C27B0", "#F3E5F5")
            "cat_health" -> Triple(com.example.finanzapro.R.drawable.ic_health, "#0D9488", "#E0F2F1")
            else -> Triple(com.example.finanzapro.R.drawable.ic_others, "#9E9E9E", "#F5F5F5")
        }
    }
}