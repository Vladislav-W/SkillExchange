package com.example.skillexchange.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.skillexchange.R
import com.example.skillexchange.data.models.SkillCategory

class CategoriesAdapter(
    private val categories: List<SkillCategory>,
    private val listener: OnCategoryClickListener
) : RecyclerView.Adapter<CategoriesAdapter.CategoryViewHolder>() {

    interface OnCategoryClickListener {
        fun onCategoryClick(category: SkillCategory)
    }

    class CategoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val icon: ImageView = itemView.findViewById(R.id.ivIcon)
        val name: TextView = itemView.findViewById(R.id.tvName)
        val count: TextView = itemView.findViewById(R.id.tvSkillCount)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_category, parent, false)
        return CategoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        val category = categories[position]

        holder.icon.setImageResource(category.iconResId)
        holder.name.text = category.name
        holder.count.text = "${category.skills.size} навыков"

        holder.itemView.setOnClickListener {
            listener.onCategoryClick(category)
        }

        // Установите цвет фона
        holder.itemView.setBackgroundColor(
            holder.itemView.context.getColor(category.colorResId)
        )
    }

    override fun getItemCount() = categories.size
}