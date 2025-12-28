package com.example.skillexchange.data.repository

import android.content.Context
import com.example.skillexchange.R
import com.example.skillexchange.data.models.Skill
import com.example.skillexchange.data.models.SkillCategory

class SkillsRepository(private val context: Context) {

    // Временные иконки - используем стандартные ресурсы Android или создаем простые
    companion object {
        // Для цветов категорий
        val categoryColors = mapOf(
            "programming" to R.color.purple_500,
            "design" to R.color.teal_200,
            "languages" to R.color.orange,
            "business" to R.color.green,
            "creative" to R.color.pink,
            "technical" to R.color.blue_grey
        )

        // Для иконок - используем стандартные иконки Android
        val skillIcons = mapOf(
            // Программирование
            "java" to android.R.drawable.ic_dialog_info,
            "kotlin" to android.R.drawable.ic_dialog_info,
            "python" to android.R.drawable.ic_dialog_info,
            "javascript" to android.R.drawable.ic_dialog_info,
            "typescript" to android.R.drawable.ic_dialog_info,
            "csharp" to android.R.drawable.ic_dialog_info,
            "cpp" to android.R.drawable.ic_dialog_info,
            "swift" to android.R.drawable.ic_dialog_info,
            "php" to android.R.drawable.ic_dialog_info,
            "go" to android.R.drawable.ic_dialog_info,
            "ruby" to android.R.drawable.ic_dialog_info,
            "dart" to android.R.drawable.ic_dialog_info,
            "rust" to android.R.drawable.ic_dialog_info,

            // Дизайн
            "figma" to android.R.drawable.ic_dialog_info,
            "photoshop" to android.R.drawable.ic_dialog_info,
            "illustrator" to android.R.drawable.ic_dialog_info,
            "xd" to android.R.drawable.ic_dialog_info,
            "sketch" to android.R.drawable.ic_dialog_info,
            "ui_ux" to android.R.drawable.ic_dialog_info,
            "graphic_design" to android.R.drawable.ic_dialog_info,
            "web_design" to android.R.drawable.ic_dialog_info,
            "motion_design" to android.R.drawable.ic_dialog_info,
            "logo_design" to android.R.drawable.ic_dialog_info,

            // Языки
            "english" to android.R.drawable.ic_dialog_info,
            "german" to android.R.drawable.ic_dialog_info,
            "french" to android.R.drawable.ic_dialog_info,
            "spanish" to android.R.drawable.ic_dialog_info,
            "chinese" to android.R.drawable.ic_dialog_info,
            "japanese" to android.R.drawable.ic_dialog_info,
            "korean" to android.R.drawable.ic_dialog_info,
            "italian" to android.R.drawable.ic_dialog_info,
            "arabic" to android.R.drawable.ic_dialog_info,
            "portuguese" to android.R.drawable.ic_dialog_info,

            // Бизнес
            "project_management" to android.R.drawable.ic_dialog_info,
            "marketing" to android.R.drawable.ic_dialog_info,
            "sales" to android.R.drawable.ic_dialog_info,
            "finance" to android.R.drawable.ic_dialog_info,
            "analytics" to android.R.drawable.ic_dialog_info,
            "seo" to android.R.drawable.ic_dialog_info,
            "smm" to android.R.drawable.ic_dialog_info,
            "copywriting" to android.R.drawable.ic_dialog_info,
            "presentations" to android.R.drawable.ic_dialog_info,
            "negotiations" to android.R.drawable.ic_dialog_info,

            // Творчество
            "photography" to android.R.drawable.ic_dialog_info,
            "video_editing" to android.R.drawable.ic_dialog_info,
            "music_production" to android.R.drawable.ic_dialog_info,
            "drawing" to android.R.drawable.ic_dialog_info,
            "writing" to android.R.drawable.ic_dialog_info,
            "acting" to android.R.drawable.ic_dialog_info,
            "cooking" to android.R.drawable.ic_dialog_info,
            "dancing" to android.R.drawable.ic_dialog_info,
            "handmade" to android.R.drawable.ic_dialog_info,
            "gardening" to android.R.drawable.ic_dialog_info,

            // Технические
            "auto_repair" to android.R.drawable.ic_dialog_info,
            "electronics" to android.R.drawable.ic_dialog_info,
            "carpentry" to android.R.drawable.ic_dialog_info,
            "welding" to android.R.drawable.ic_dialog_info,
            "plumbing" to android.R.drawable.ic_dialog_info,
            "electrician" to android.R.drawable.ic_dialog_info,
            "pc_repair" to android.R.drawable.ic_dialog_info,
            "phone_repair" to android.R.drawable.ic_dialog_info,
            "3d_printing" to android.R.drawable.ic_dialog_info,
            "drone_piloting" to android.R.drawable.ic_dialog_info
        )
    }

    // Получаем все категории с навыками
    fun getAllCategories(): List<SkillCategory> {
        return listOf(
            SkillCategory(
                id = "programming",
                name = "💻 Программирование",
                iconResId = android.R.drawable.ic_dialog_info,
                colorResId = categoryColors["programming"] ?: R.color.purple_500,
                skills = listOf(
                    Skill("java", "Java", "programming", skillIcons["java"] ?: android.R.drawable.ic_dialog_info),
                    Skill("kotlin", "Kotlin", "programming", skillIcons["kotlin"] ?: android.R.drawable.ic_dialog_info),
                    Skill("python", "Python", "programming", skillIcons["python"] ?: android.R.drawable.ic_dialog_info),
                    Skill("javascript", "JavaScript", "programming", skillIcons["javascript"] ?: android.R.drawable.ic_dialog_info),
                    Skill("typescript", "TypeScript", "programming", skillIcons["typescript"] ?: android.R.drawable.ic_dialog_info),
                    Skill("csharp", "C#", "programming", skillIcons["csharp"] ?: android.R.drawable.ic_dialog_info),
                    Skill("cpp", "C++", "programming", skillIcons["cpp"] ?: android.R.drawable.ic_dialog_info),
                    Skill("swift", "Swift", "programming", skillIcons["swift"] ?: android.R.drawable.ic_dialog_info),
                    Skill("php", "PHP", "programming", skillIcons["php"] ?: android.R.drawable.ic_dialog_info),
                    Skill("go", "Go", "programming", skillIcons["go"] ?: android.R.drawable.ic_dialog_info),
                    Skill("ruby", "Ruby", "programming", skillIcons["ruby"] ?: android.R.drawable.ic_dialog_info),
                    Skill("dart", "Dart", "programming", skillIcons["dart"] ?: android.R.drawable.ic_dialog_info),
                    Skill("rust", "Rust", "programming", skillIcons["rust"] ?: android.R.drawable.ic_dialog_info)
                )
            ),
            SkillCategory(
                id = "design",
                name = "🎨 Дизайн",
                iconResId = android.R.drawable.ic_dialog_info,
                colorResId = categoryColors["design"] ?: R.color.teal_200,
                skills = listOf(
                    Skill("figma", "Figma", "design", skillIcons["figma"] ?: android.R.drawable.ic_dialog_info),
                    Skill("photoshop", "Adobe Photoshop", "design", skillIcons["photoshop"] ?: android.R.drawable.ic_dialog_info),
                    Skill("illustrator", "Adobe Illustrator", "design", skillIcons["illustrator"] ?: android.R.drawable.ic_dialog_info),
                    Skill("xd", "Adobe XD", "design", skillIcons["xd"] ?: android.R.drawable.ic_dialog_info),
                    Skill("sketch", "Sketch", "design", skillIcons["sketch"] ?: android.R.drawable.ic_dialog_info),
                    Skill("ui_ux", "UI/UX Дизайн", "design", skillIcons["ui_ux"] ?: android.R.drawable.ic_dialog_info),
                    Skill("graphic_design", "Графический дизайн", "design", skillIcons["graphic_design"] ?: android.R.drawable.ic_dialog_info),
                    Skill("web_design", "Веб-дизайн", "design", skillIcons["web_design"] ?: android.R.drawable.ic_dialog_info),
                    Skill("motion_design", "Моушн-дизайн", "design", skillIcons["motion_design"] ?: android.R.drawable.ic_dialog_info),
                    Skill("logo_design", "Дизайн логотипов", "design", skillIcons["logo_design"] ?: android.R.drawable.ic_dialog_info)
                )
            ),
            SkillCategory(
                id = "languages",
                name = "🗣️ Языки",
                iconResId = android.R.drawable.ic_dialog_info,
                colorResId = categoryColors["languages"] ?: R.color.orange,
                skills = listOf(
                    Skill("english", "Английский", "languages", skillIcons["english"] ?: android.R.drawable.ic_dialog_info),
                    Skill("german", "Немецкий", "languages", skillIcons["german"] ?: android.R.drawable.ic_dialog_info),
                    Skill("french", "Французский", "languages", skillIcons["french"] ?: android.R.drawable.ic_dialog_info),
                    Skill("spanish", "Испанский", "languages", skillIcons["spanish"] ?: android.R.drawable.ic_dialog_info),
                    Skill("chinese", "Китайский", "languages", skillIcons["chinese"] ?: android.R.drawable.ic_dialog_info),
                    Skill("japanese", "Японский", "languages", skillIcons["japanese"] ?: android.R.drawable.ic_dialog_info),
                    Skill("korean", "Корейский", "languages", skillIcons["korean"] ?: android.R.drawable.ic_dialog_info),
                    Skill("italian", "Итальянский", "languages", skillIcons["italian"] ?: android.R.drawable.ic_dialog_info),
                    Skill("arabic", "Арабский", "languages", skillIcons["arabic"] ?: android.R.drawable.ic_dialog_info),
                    Skill("portuguese", "Португальский", "languages", skillIcons["portuguese"] ?: android.R.drawable.ic_dialog_info)
                )
            ),
            SkillCategory(
                id = "business",
                name = "📊 Бизнес",
                iconResId = android.R.drawable.ic_dialog_info,
                colorResId = categoryColors["business"] ?: R.color.green,
                skills = listOf(
                    Skill("project_management", "Управление проектами", "business", skillIcons["project_management"] ?: android.R.drawable.ic_dialog_info),
                    Skill("marketing", "Маркетинг", "business", skillIcons["marketing"] ?: android.R.drawable.ic_dialog_info),
                    Skill("sales", "Продажи", "business", skillIcons["sales"] ?: android.R.drawable.ic_dialog_info),
                    Skill("finance", "Финансы", "business", skillIcons["finance"] ?: android.R.drawable.ic_dialog_info),
                    Skill("analytics", "Аналитика данных", "business", skillIcons["analytics"] ?: android.R.drawable.ic_dialog_info),
                    Skill("seo", "SEO", "business", skillIcons["seo"] ?: android.R.drawable.ic_dialog_info),
                    Skill("smm", "SMM", "business", skillIcons["smm"] ?: android.R.drawable.ic_dialog_info),
                    Skill("copywriting", "Копирайтинг", "business", skillIcons["copywriting"] ?: android.R.drawable.ic_dialog_info),
                    Skill("presentations", "Подготовка презентаций", "business", skillIcons["presentations"] ?: android.R.drawable.ic_dialog_info),
                    Skill("negotiations", "Ведение переговоров", "business", skillIcons["negotiations"] ?: android.R.drawable.ic_dialog_info)
                )
            ),
            SkillCategory(
                id = "creative",
                name = "🎭 Творчество",
                iconResId = android.R.drawable.ic_dialog_info,
                colorResId = categoryColors["creative"] ?: R.color.pink,
                skills = listOf(
                    Skill("photography", "Фотография", "creative", skillIcons["photography"] ?: android.R.drawable.ic_dialog_info),
                    Skill("video_editing", "Видеомонтаж", "creative", skillIcons["video_editing"] ?: android.R.drawable.ic_dialog_info),
                    Skill("music_production", "Создание музыки", "creative", skillIcons["music_production"] ?: android.R.drawable.ic_dialog_info),
                    Skill("drawing", "Рисование", "creative", skillIcons["drawing"] ?: android.R.drawable.ic_dialog_info),
                    Skill("writing", "Писательство", "creative", skillIcons["writing"] ?: android.R.drawable.ic_dialog_info),
                    Skill("acting", "Актерское мастерство", "creative", skillIcons["acting"] ?: android.R.drawable.ic_dialog_info),
                    Skill("cooking", "Кулинария", "creative", skillIcons["cooking"] ?: android.R.drawable.ic_dialog_info),
                    Skill("dancing", "Танцы", "creative", skillIcons["dancing"] ?: android.R.drawable.ic_dialog_info),
                    Skill("handmade", "Рукоделие", "creative", skillIcons["handmade"] ?: android.R.drawable.ic_dialog_info),
                    Skill("gardening", "Садоводство", "creative", skillIcons["gardening"] ?: android.R.drawable.ic_dialog_info)
                )
            ),
            SkillCategory(
                id = "technical",
                name = "🔧 Технические",
                iconResId = android.R.drawable.ic_dialog_info,
                colorResId = categoryColors["technical"] ?: R.color.blue_grey,
                skills = listOf(
                    Skill("auto_repair", "Ремонт авто", "technical", skillIcons["auto_repair"] ?: android.R.drawable.ic_dialog_info),
                    Skill("electronics", "Электроника", "technical", skillIcons["electronics"] ?: android.R.drawable.ic_dialog_info),
                    Skill("carpentry", "Столярное дело", "technical", skillIcons["carpentry"] ?: android.R.drawable.ic_dialog_info),
                    Skill("welding", "Сварка", "technical", skillIcons["welding"] ?: android.R.drawable.ic_dialog_info),
                    Skill("plumbing", "Сантехника", "technical", skillIcons["plumbing"] ?: android.R.drawable.ic_dialog_info),
                    Skill("electrician", "Электрика", "technical", skillIcons["electrician"] ?: android.R.drawable.ic_dialog_info),
                    Skill("pc_repair", "Ремонт ПК", "technical", skillIcons["pc_repair"] ?: android.R.drawable.ic_dialog_info),
                    Skill("phone_repair", "Ремонт телефонов", "technical", skillIcons["phone_repair"] ?: android.R.drawable.ic_dialog_info),
                    Skill("3d_printing", "3D печать", "technical", skillIcons["3d_printing"] ?: android.R.drawable.ic_dialog_info),
                    Skill("drone_piloting", "Пилотирование дронов", "technical", skillIcons["drone_piloting"] ?: android.R.drawable.ic_dialog_info)
                )
            )
        )
    }

    // Поиск навыков по названию
    fun searchSkills(query: String): List<Skill> {
        return getAllCategories()
            .flatMap { it.skills }
            .filter { skill ->
                skill.name.contains(query, ignoreCase = true)
            }
    }

    // Получить навыки по категории
    fun getSkillsByCategory(categoryId: String): List<Skill> {
        return getAllCategories()
            .find { it.id == categoryId }
            ?.skills ?: emptyList()
    }

    // Получить категорию по ID
    fun getCategory(categoryId: String): SkillCategory? {
        return getAllCategories().find { it.id == categoryId }
    }

    // Получить все навыки
    fun getAllSkills(): List<Skill> {
        return getAllCategories().flatMap { it.skills }
    }

    // Получить навык по ID
    fun getSkill(skillId: String): Skill? {
        return getAllSkills().find { it.id == skillId }
    }
}