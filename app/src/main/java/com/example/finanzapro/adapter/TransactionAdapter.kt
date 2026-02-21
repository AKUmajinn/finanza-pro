package com.example.finanzapro.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.finanzapro.databinding.ItemDashboardBinding
import com.example.finanzapro.model.Transaction
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class TransactionAdapter(private var listTran: List<Transaction>) : RecyclerView.Adapter<TransactionItemViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransactionItemViewHolder {
        val binding = ItemDashboardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TransactionItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TransactionItemViewHolder, position: Int) {
        val item = listTran[position]

        holder.binding.tvSimbolo.text = "S/ "
        holder.binding.tvItemBills.text = String.format(Locale.US, "%.2f", item.amount)

        val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        holder.binding.tvDate.text = sdf.format(Date(item.timestamp))

        holder.binding.tvCategory.text = item.description
        holder.binding.tvDescription.text = getCategoryName(item.categoryId).uppercase()

        val (iconRes, colorHex, bgHex) = getCategoryStyle(item.categoryId)
        holder.binding.ivIconBills.setImageResource(iconRes)
        holder.binding.ivIconBills.setColorFilter(Color.parseColor(colorHex))
        holder.binding.cvIconBackground.setCardBackgroundColor(Color.parseColor(bgHex))
    }

    override fun getItemCount(): Int {
        return listTran.count()
    }

    fun getList(newList: List<Transaction>){
        listTran = newList
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