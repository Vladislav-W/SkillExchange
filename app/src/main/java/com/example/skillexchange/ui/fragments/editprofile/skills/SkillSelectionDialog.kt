package com.example.skillexchange

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.example.skillexchange.data.models.Skill
import com.example.skillexchange.data.repository.SkillsRepository
import com.example.skillexchange.ui.fragments.editprofile.skills.fragments.AllSkillsFragment
import com.example.skillexchange.ui.fragments.editprofile.skills.fragments.CategoriesFragment
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class SkillSelectionDialog : DialogFragment() {

    interface SkillSelectionListener {
        fun onSkillsSelected(selectedSkills: List<Skill>)
    }

    private var listener: SkillSelectionListener? = null
    private lateinit var skillsRepository: SkillsRepository
    private val selectedSkills = mutableSetOf<Skill>()

    private lateinit var chipGroupSelected: ChipGroup
    private lateinit var tvSelectedTitle: TextView
    private lateinit var viewPager: ViewPager2
    private lateinit var btnDone: Button
    private lateinit var btnCancel: Button

    fun setSkillSelectionListener(listener: SkillSelectionListener) {
        this.listener = listener
    }

    fun setInitialSelectedSkills(skills: List<Skill>) {
        selectedSkills.clear()
        selectedSkills.addAll(skills)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.dialog_skill_selection, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        skillsRepository = SkillsRepository(requireContext())

        // Инициализация UI
        chipGroupSelected = view.findViewById(R.id.chipGroupSelected)
        tvSelectedTitle = view.findViewById(R.id.tvSelectedTitle)
        viewPager = view.findViewById(R.id.viewPager)
        btnDone = view.findViewById(R.id.btnDone)
        btnCancel = view.findViewById(R.id.btnCancel)

        // Настройка ViewPager с вкладками
        val tabLayout = view.findViewById<TabLayout>(R.id.tabLayout)
        viewPager.adapter = SkillsPagerAdapter(this)

        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = when (position) {
                0 -> "Категории"
                1 -> "Все навыки"
                else -> ""
            }
        }.attach()

        // Обновляем выбранные навыки
        updateSelectedSkillsUI()

        // Обработчики кнопок
        btnDone.setOnClickListener {
            listener?.onSkillsSelected(selectedSkills.toList())
            dismiss()
        }

        btnCancel.setOnClickListener {
            dismiss()
        }
    }

    private fun updateSelectedSkillsUI() {
        chipGroupSelected.removeAllViews()

        if (selectedSkills.isNotEmpty()) {
            tvSelectedTitle.visibility = View.VISIBLE
            chipGroupSelected.visibility = View.VISIBLE
            tvSelectedTitle.text = "Выбрано: ${selectedSkills.size}"

            selectedSkills.forEach { skill ->
                val chip = Chip(requireContext()).apply {
                    text = skill.name
                    isCloseIconVisible = true
                    setOnCloseIconClickListener {
                        selectedSkills.remove(skill)
                        updateSelectedSkillsUI()
                        notifySkillUnselected(skill)
                    }
                }
                chipGroupSelected.addView(chip)
            }
        } else {
            tvSelectedTitle.visibility = View.GONE
            chipGroupSelected.visibility = View.GONE
        }
    }

    private fun notifySkillUnselected(skill: Skill) {
        val adapter = viewPager.adapter as? SkillsPagerAdapter
        adapter?.notifySkillUnselected(skill)
    }

    fun onSkillSelected(skill: Skill) {
        if (selectedSkills.contains(skill)) {
            selectedSkills.remove(skill)
        } else {
            selectedSkills.add(skill)
        }
        updateSelectedSkillsUI()
    }

    fun isSkillSelected(skill: Skill): Boolean {
        return selectedSkills.contains(skill)
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
    }

    private inner class SkillsPagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

        private val fragments = listOf(
            CategoriesFragment.newInstance(),
            AllSkillsFragment.newInstance()
        )

        override fun getItemCount(): Int = fragments.size

        override fun createFragment(position: Int): Fragment {
            return fragments[position]
        }

        fun notifySkillUnselected(skill: Skill) {
            fragments.forEachIndexed { index, fragment ->
                when (index) {
                    0 -> (fragment as? CategoriesFragment)?.notifySkillUnselected(skill)
                    1 -> (fragment as? AllSkillsFragment)?.notifySkillUnselected(skill)
                }
            }
        }
    }
}