package com.example.finanzapro.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AutoCompleteTextView
import android.widget.EditText
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.finanzapro.R
import com.example.finanzapro.model.Transaction
import com.google.android.material.textfield.TextInputEditText

class TransactionAdapter(
    var listTran: List<Transaction>,
    val onBorrarClick: (String) -> Unit,
    val onEditarClick: (Transaction) -> Unit) : RecyclerView.Adapter<TransactionAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        // this class is for the adapter for dashboard fragment
        val amount: EditText = itemView.findViewById(R.id.etAmount)
        val details: EditText = itemView.findViewById(R.id.etDescripcion)
        val category: AutoCompleteTextView = itemView.findViewById(R.id.actvCategoria)
        val date: TextInputEditText = itemView.findViewById(R.id.etFecha)
        val hour: TextInputEditText = itemView.findViewById(R.id.etHora)
        val radioGroup: RadioGroup = itemView.findViewById(R.id.rgMetodoPago)
        val rbEfectivo: RadioButton = itemView.findViewById(R.id.rbEfectivo)
        val rbTarjeta: RadioButton = itemView.findViewById(R.id.rbTarjeta)


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransactionAdapter.ViewHolder {
        //todo here impl el item_transaction.xml (fragment_register ? delete)
        val view = LayoutInflater.from(parent.context).inflate(R.layout.fragment_register, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: TransactionAdapter.ViewHolder, position: Int) {
        val transaction = listTran[position]
        holder.amount.setText(transaction.amount.toString())
        holder.details.setText(transaction.description)
        holder.category.setText(transaction.category)
        holder.date.setText(transaction.date)

    }

    override fun getItemCount(): Int {
        return listTran.size
    }
}