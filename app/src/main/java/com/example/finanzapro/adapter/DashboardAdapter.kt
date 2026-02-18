package com.example.finanzapro.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.Constraints
import androidx.recyclerview.widget.RecyclerView
import com.example.finanzapro.R
import com.example.finanzapro.model.Transaction



class DashboardAdapter(
    var listTransaction: List<Transaction>

): RecyclerView.Adapter<DashboardAdapter.ViewHolder>(){

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

        val cl_spent: Constraints = itemView.findViewById(R.id.cl_spent)
        val iv_icon_bills: ImageView = itemView.findViewById(R.id.iv_icon_bills)
        val tv_category: TextView = itemView.findViewById(R.id.tv_category)
        val tv_date: TextView = itemView.findViewById(R.id.tv_date)
        val tv_simbolo: TextView = itemView.findViewById(R.id.tv_simbolo)
        val tv_item_bills: TextView = itemView.findViewById(R.id.tv_item_bills)
        val tv_description: TextView = itemView.findViewById(R.id.tv_description)


    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): DashboardAdapter.ViewHolder {

        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_dashboard, parent, false)
        return ViewHolder(view)

    }

    override fun onBindViewHolder(holder: DashboardAdapter.ViewHolder, position: Int) {
        val transaction = listTransaction[position]

        holder.tv_category.text = transaction.category
        holder.tv_date.text = transaction.date
        holder.tv_item_bills.text = "$ %.2f".format(transaction.amount)
        holder.tv_description.text = transaction.description
    }

    override fun getItemCount(): Int = listTransaction.size
}