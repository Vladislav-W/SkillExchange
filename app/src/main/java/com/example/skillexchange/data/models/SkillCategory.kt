package com.example.skillexchange.data.models

import com.example.skillexchange.data.models.Skill

data class SkillCategory(
    val id: String = "",
    val name: String = "",
    val iconResId: Int = 0,
    val colorResId: Int = 0, // Цвет категории
    val skills: List<Skill> = emptyList()
)