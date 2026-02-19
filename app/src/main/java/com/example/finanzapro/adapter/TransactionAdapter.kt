package com.example.finanzapro.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.finanzapro.databinding.ItemDashboardBinding
import com.example.finanzapro.model.Transaction

class TransactionAdapter(private var listTran: List<Transaction>) : RecyclerView.Adapter<TransactionItemViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransactionItemViewHolder {
        val binding = ItemDashboardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TransactionItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TransactionItemViewHolder, position: Int) {
        val item = listTran[position]
        holder.binding.tvItemBills.text = item.amount.toString()
        holder.binding.tvCategory.text = item.category
        holder.binding.tvDate.text = item.date
        holder.binding.tvDescription.text = item.description
        holder.binding.tvSimbolo.text = "S/ "
        val context = holder.itemView.context
    }

    override fun getItemCount(): Int {
        return listTran.count()
    }

    fun getList(newList: List<Transaction>){
        listTran = newList
        notifyDataSetChanged()
    }
}