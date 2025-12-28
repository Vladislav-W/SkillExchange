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
import com.example.skillexchange.data.repository.SkillsRepository
import com.example.skillexchange.ui.adapters.SkillsAdapter

class AllSkillsFragment : Fragment() {

    private lateinit var skillsRepository: SkillsRepository
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: SkillsAdapter
    private var allSkills = listOf<Skill>()

    companion object {
        fun newInstance(): AllSkillsFragment {
            return AllSkillsFragment()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_all_skills, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        skillsRepository = SkillsRepository(requireContext())
        recyclerView = view.findViewById(R.id.recyclerView)

        // Получаем все навыки
        allSkills = skillsRepository.getAllSkills()

        // Настройка RecyclerView
        setupRecyclerView()
    }

    private fun setupRecyclerView() {
        val layoutManager = GridLayoutManager(requireContext(), 3)
        recyclerView.layoutManager = layoutManager

        adapter = SkillsAdapter(
            allSkills,
            object : SkillsAdapter.OnSkillClickListener {
                override fun onSkillClick(skill: Skill) {
                    // Передаем выбранный навык в диалог
                    (parentFragment as? com.example.skillexchange.SkillSelectionDialog)?.onSkillSelected(skill)
                }
            },
            { skill -> (parentFragment as? com.example.skillexchange.SkillSelectionDialog)?.isSkillSelected(skill) ?: false }
        )

        recyclerView.adapter = adapter
    }

    fun notifySkillUnselected(skill: Skill) {
        adapter.notifyItemChanged(skill)
    }
}