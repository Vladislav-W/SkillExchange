package com.example.skillexchange.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
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

        // Подсветка выбранного навыка
        val selected = isSelected(skill)
        holder.selectedOverlay.visibility = if (selected) View.VISIBLE else View.GONE

        holder.itemView.setOnClickListener {
            listener.onSkillClick(skill)
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