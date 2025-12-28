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

class CategoriesFragment : Fragment() {

    private lateinit var skillsRepository: SkillsRepository
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: CategoriesAdapter
    private var categories = listOf<SkillCategory>()

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

        // Получаем все категории
        categories = skillsRepository.getAllCategories()

        // Настройка RecyclerView
        setupRecyclerView()
    }

    private fun setupRecyclerView() {
        val layoutManager = GridLayoutManager(requireContext(), 2)
        recyclerView.layoutManager = layoutManager

        adapter = CategoriesAdapter(categories, object : CategoriesAdapter.OnCategoryClickListener {
            override fun onCategoryClick(category: SkillCategory) {
                // При клике на категорию можно показать навыки этой категории
                // Пока просто игнорируем
            }
        })

        recyclerView.adapter = adapter
    }

    fun notifySkillUnselected(skill: Skill) {
        // В этой версии ничего не делаем
    }
}