package com.example.skillexchange.data.models

import java.util.Date

data class User(
    val uid: String = "",
    val email: String = "",
    val name: String = "",
    val skills: List<UserSkill> = emptyList(),  // UserSkill вместо String
    val bio: String = "",
    val avatarUrl: String = "",
    val createdAt: Date? = null,
    val rating: Double = 5.0,
    val completedExchanges: Int = 0
) {
    // Пустой конструктор для Firestore
    constructor() : this("", "", "", emptyList(), "", "", null, 5.0, 0)

    data class UserSkill(
        val skillId: String = "",
        val level: String = "BEGINNER"  // BEGINNER, INTERMEDIATE, ADVANCED, EXPERT
    )
}