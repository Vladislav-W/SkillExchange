package com.example.skillexchange.data.models

data class SkillWithLevel(
    val skill: Skill,
    val level: SkillLevel = SkillLevel.BEGINNER
) {
    enum class SkillLevel(val displayName: String) {
        BEGINNER("Начинающий"),
        INTERMEDIATE("Средний"),
        ADVANCED("Продвинутый"),
        EXPERT("Эксперт")
    }
}