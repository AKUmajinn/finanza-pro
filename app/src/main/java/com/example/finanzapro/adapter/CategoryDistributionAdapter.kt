package com.example.finanzapro.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.finanzapro.R
import com.example.finanzapro.model.CategoryDistribution
import androidx.core.graphics.toColorInt

class CategoryDistributionAdapter(
    private val categories: List<CategoryDistribution>
) : RecyclerView.Adapter<CategoryDistributionAdapter.ViewHolder>() {

    // Mapa de colores por nombre de categoría
    fun getCategoryColor(categoryName: String): String {
        return when (categoryName) {
            "Alimentación" -> "#4CAF50"      // Verde
            "Transporte" -> "#2196F3"         // Azul
            "Vivienda", "Hogar" -> "#FF7043"  // Naranja claro
            "Entretenimiento" -> "#FF9800"     // Naranja
            "Salud" -> "#F44336"               // Rojo
            "Educación" -> "#9C27B0"           // Púrpura
            "Ropa" -> "#EC407A"                 // Rosa
            "Tecnología" -> "#7B1FA2"           // Púrpura oscuro
            "Viajes" -> "#29B6F6"               // Celeste
            "Otros" -> "#9E9E9E"                 // Gris
            else -> "#008382"                    // Color por defecto (verde app)
        }
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val viewColor = itemView.findViewById<View>(R.id.viewColorIndicator)
        private val tvName = itemView.findViewById<TextView>(R.id.tvCategoryName)
        private val tvAmount = itemView.findViewById<TextView>(R.id.tvCategoryAmount)
        private val tvPercentage = itemView.findViewById<TextView>(R.id.tvCategoryPercentage)

        fun bind(category: CategoryDistribution) {
            tvName.text = category.name
            tvAmount.text = "S/ ${"%.2f".format(category.amount)}"
            tvPercentage.text = "${category.percentage}%"
            viewColor.setBackgroundColor(getCategoryColor(category.name).toColorInt())
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_category_distribution, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(categories[position])
    }

    override fun getItemCount() = categories.size
}