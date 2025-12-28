package com.example.skillexchange.ui.fragments.editprofile.skills.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.skillexchange.R
import com.example.skillexchange.data.models.Skill
import com.example.skillexchange.data.models.SkillCategory
import com.example.skillexchange.data.repository.SkillsRepository
import com.example.skillexchange.ui.adapters.CategoriesAdapter
import com.example.skillexchange.ui.adapters.SkillsAdapter

class CategoriesFragment : Fragment() {

    private lateinit var skillsRepository: SkillsRepository
    private lateinit var recyclerView: RecyclerView
    private lateinit var categoriesAdapter: CategoriesAdapter
    private lateinit var skillsAdapter: SkillsAdapter
    private var categories = listOf<SkillCategory>()
    private var isShowingSkills = false
    private var currentCategory: SkillCategory? = null

    companion object {
        fun newInstance(): CategoriesFragment {
            return CategoriesFragment()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_categories, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        skillsRepository = SkillsRepository(requireContext())
        recyclerView = view.findViewById(R.id.recyclerView)

        categories = skillsRepository.getAllCategories()
        setupRecyclerView()
    }

    private fun setupRecyclerView() {
        val layoutManager = GridLayoutManager(requireContext(), 2)
        recyclerView.layoutManager = layoutManager

        categoriesAdapter = CategoriesAdapter(categories, object : CategoriesAdapter.OnCategoryClickListener {
            override fun onCategoryClick(category: SkillCategory) {
                showSkillsForCategory(category)
            }
        })

        recyclerView.adapter = categoriesAdapter
    }

    private fun showSkillsForCategory(category: SkillCategory) {
        isShowingSkills = true
        currentCategory = category

        skillsAdapter = SkillsAdapter(
            category.skills,
            object : SkillsAdapter.OnSkillClickListener {
                override fun onSkillClick(skill: Skill) {
                    (parentFragment as? com.example.skillexchange.SkillSelectionDialog)?.onSkillSelected(skill)
                }
            },
            { skill -> (parentFragment as? com.example.skillexchange.SkillSelectionDialog)?.isSkillSelected(skill) ?: false }
        )

        recyclerView.adapter = skillsAdapter
    }

    fun notifySkillUnselected(skill: Skill) {
        if (isShowingSkills) {
            skillsAdapter.notifyItemChanged(skill)
        }
    }
}