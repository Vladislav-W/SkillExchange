package com.example.skillexchange.data.models

data class Skill(
    val id: String = "",
    val name: String = "",
    val categoryId: String = "",
    val iconResId: Int = 0
) {
    // Для сравнения навыков
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as Skill
        return id == other.id
    }

    override fun hashCode(): Int = id.hashCode()
}