package com.example.skillexchange.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.skillexchange.R
import com.example.skillexchange.data.models.Skill

class SkillsAdapter(
    private val skills: List<Skill>,
    private val listener: OnSkillClickListener,
    private val isSelected: (Skill) -> Boolean
) : RecyclerView.Adapter<SkillsAdapter.SkillViewHolder>() {

    interface OnSkillClickListener {
        fun onSkillClick(skill: Skill)
    }

    class SkillViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val icon: ImageView = itemView.findViewById(R.id.ivIcon)
        val name: TextView = itemView.findViewById(R.id.tvName)
        val selectedOverlay: View = itemView.findViewById(R.id.selectedOverlay)
        // Убрали skillContainer, используем сам itemView
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SkillViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_skill, parent, false)
        return SkillViewHolder(view)
    }

    override fun onBindViewHolder(holder: SkillViewHolder, position: Int) {
        val skill = skills[position]

        holder.icon.setImageResource(skill.iconResId)
        holder.name.text = skill.name

        // ПРОВЕРКА ВЫБРАН ЛИ НАВЫК
        val selected = isSelected(skill)
        updateSelectionUI(holder, selected)

        holder.itemView.setOnClickListener {
            // Анимация при клике
            holder.itemView.animate()
                .scaleX(0.9f)
                .scaleY(0.9f)
                .setDuration(100)
                .withEndAction {
                    holder.itemView.animate()
                        .scaleX(1f)
                        .scaleY(1f)
                        .setDuration(100)
                        .start()
                    listener.onSkillClick(skill)
                }
                .start()
        }
    }

    private fun updateSelectionUI(holder: SkillViewHolder, selected: Boolean) {
        if (selected) {
            holder.selectedOverlay.visibility = View.VISIBLE
            // Меняем фон самого itemView
            holder.itemView.setBackgroundColor(
                ContextCompat.getColor(holder.itemView.context, R.color.selected_skill)
            )
        } else {
            holder.selectedOverlay.visibility = View.GONE
            holder.itemView.setBackgroundColor(
                ContextCompat.getColor(holder.itemView.context, android.R.color.transparent)
            )
        }
    }

    override fun getItemCount() = skills.size

    fun notifyItemChanged(skill: Skill) {
        val position = skills.indexOfFirst { it.id == skill.id }
        if (position != -1) {
            notifyItemChanged(position)
        }
    }
}