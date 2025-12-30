package com.example.skillexchange

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.fragment.app.DialogFragment

class SkillLevelDialog : DialogFragment() {

    companion object {
        private const val ARG_SKILL_ID = "skill_id"
        private const val ARG_SKILL_NAME = "skill_name"

        fun newInstance(skillId: String, skillName: String, onLevelSelected: (String) -> Unit): SkillLevelDialog {
            val dialog = SkillLevelDialog()
            dialog.onLevelSelected = onLevelSelected

            val args = Bundle()
            args.putString(ARG_SKILL_ID, skillId)
            args.putString(ARG_SKILL_NAME, skillName)
            dialog.arguments = args

            return dialog
        }
    }

    private lateinit var onLevelSelected: (String) -> Unit
    private lateinit var skillId: String
    private lateinit var skillName: String

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        skillId = arguments?.getString(ARG_SKILL_ID) ?: ""
        skillName = arguments?.getString(ARG_SKILL_NAME) ?: ""

        return AlertDialog.Builder(requireContext())
            .setTitle("Выберите уровень владения $skillName")
            .setView(createLevelSelectionView())
            .setPositiveButton("Выбрать") { _, _ ->
                // Уровень будет выбран в RadioGroup
            }
            .setNegativeButton("Отмена", null)
            .create()
    }

    private fun createLevelSelectionView(): RadioGroup {
        val radioGroup = RadioGroup(requireContext())
        radioGroup.orientation = RadioGroup.VERTICAL

        val levels = listOf(
            "BEGINNER" to "Начинающий",
            "INTERMEDIATE" to "Средний",
            "ADVANCED" to "Продвинутый",
            "EXPERT" to "Эксперт"
        )

        levels.forEachIndexed { index, (level, displayName) ->
            val radioButton = RadioButton(requireContext()).apply {
                text = displayName
                id = index
                textSize = 16f
                setPadding(32, 32, 32, 32)
            }

            radioGroup.addView(radioButton)

            // Выбираем "Начинающий" по умолчанию
            if (level == "BEGINNER") {
                radioGroup.check(index)
            }
        }

        radioGroup.setOnCheckedChangeListener { group, checkedId ->
            val selectedIndex = group.checkedRadioButtonId
            val selectedLevel = levels[selectedIndex].first
            onLevelSelected(selectedLevel)
            dismiss()
        }

        return radioGroup
    }
}