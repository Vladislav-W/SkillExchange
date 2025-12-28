package com.example.skillexchange

import com.example.skillexchange.data.models.Skill
import com.example.skillexchange.data.models.SkillCategory

object SkillsData {

    val categories = listOf(
        SkillCategory(
            id = "programming",
            name = "💻 Программирование",
            iconResId = android.R.drawable.ic_dialog_info, // Используем Int вместо String
            colorResId = android.R.color.holo_purple,
            skills = listOf(
                Skill("java", "Java", "programming", android.R.drawable.ic_dialog_info),
                Skill("kotlin", "Kotlin", "programming", android.R.drawable.ic_dialog_info),
                Skill("python", "Python", "programming", android.R.drawable.ic_dialog_info),
                Skill("javascript", "JavaScript", "programming", android.R.drawable.ic_dialog_info),
                Skill("csharp", "C#", "programming", android.R.drawable.ic_dialog_info),
                Skill("swift", "Swift", "programming", android.R.drawable.ic_dialog_info),
                Skill("php", "PHP", "programming", android.R.drawable.ic_dialog_info),
                Skill("cpp", "C++", "programming", android.R.drawable.ic_dialog_info),
                Skill("go", "Go", "programming", android.R.drawable.ic_dialog_info),
                Skill("ruby", "Ruby", "programming", android.R.drawable.ic_dialog_info)
            )
        ),
        SkillCategory(
            id = "design",
            name = "🎨 Дизайн",
            iconResId = android.R.drawable.ic_dialog_info,
            colorResId = android.R.color.holo_blue_bright,
            skills = listOf(
                Skill("figma", "Figma", "design", android.R.drawable.ic_dialog_info),
                Skill("photoshop", "Adobe Photoshop", "design", android.R.drawable.ic_dialog_info),
                Skill("illustrator", "Adobe Illustrator", "design", android.R.drawable.ic_dialog_info),
                Skill("ui_ux", "UI/UX Design", "design", android.R.drawable.ic_dialog_info),
                Skill("graphic_design", "Graphic Design", "design", android.R.drawable.ic_dialog_info),
                Skill("web_design", "Web Design", "design", android.R.drawable.ic_dialog_info)
            )
        ),
        SkillCategory(
            id = "languages",
            name = "🗣️ Языки",
            iconResId = android.R.drawable.ic_dialog_info,
            colorResId = android.R.color.holo_orange_light,
            skills = listOf(
                Skill("english", "Английский", "languages", android.R.drawable.ic_dialog_info),
                Skill("german", "Немецкий", "languages", android.R.drawable.ic_dialog_info),
                Skill("french", "Французский", "languages", android.R.drawable.ic_dialog_info),
                Skill("spanish", "Испанский", "languages", android.R.drawable.ic_dialog_info),
                Skill("chinese", "Китайский", "languages", android.R.drawable.ic_dialog_info),
                Skill("japanese", "Японский", "languages", android.R.drawable.ic_dialog_info),
                Skill("italian", "Итальянский", "languages", android.R.drawable.ic_dialog_info)
            )
        )
    )

    // Все навыки в одном списке для поиска
    val allSkills: List<Skill> by lazy {
        categories.flatMap { it.skills }
    }

    fun getSkillsByCategory(categoryId: String): List<Skill> {
        return categories.find { it.id == categoryId }?.skills ?: emptyList()
    }

    fun searchSkills(query: String): List<Skill> {
        return allSkills.filter {
            it.name.contains(query, ignoreCase = true)
        }
    }
}