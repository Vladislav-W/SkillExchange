package com.example.skillexchange.ui.fragments.editprofile.skills.fragments

import com.example.skillexchange.data.models.Skill

interface SkillSelectionFragment {
    fun notifySkillUnselected(skill: Skill)
}