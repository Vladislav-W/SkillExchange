package com.example.skillexchange.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.skillexchange.R
import com.example.skillexchange.data.repository.SkillsRepository
import com.example.skillexchange.ui.viewmodels.UserViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class ProfileFragment : Fragment() {

    private lateinit var auth: FirebaseAuth
    private lateinit var skillsRepository: SkillsRepository
    private val viewModel: UserViewModel by viewModels()

    // UI элементы
    private lateinit var tvName: TextView
    private lateinit var tvEmail: TextView
    private lateinit var tvRating: TextView
    private lateinit var tvCompletedExchanges: TextView
    private lateinit var tvSkills: TextView
    private lateinit var tvBio: TextView
    private lateinit var btnEditProfile: Button
    private lateinit var btnLogout: Button

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth = Firebase.auth
        skillsRepository = SkillsRepository(requireContext())

        // Инициализация UI элементов
        tvName = view.findViewById(R.id.tvName)
        tvEmail = view.findViewById(R.id.tvEmail)
        tvRating = view.findViewById(R.id.tvRating)
        tvCompletedExchanges = view.findViewById(R.id.tvCompletedExchanges)
        tvSkills = view.findViewById(R.id.tvSkills)
        tvBio = view.findViewById(R.id.tvBio)
        btnEditProfile = view.findViewById(R.id.btnEditProfile)
        btnLogout = view.findViewById(R.id.btnLogout)

        // Загружаем данные пользователя
        loadUserData()

        // Обработчики кнопок
        btnEditProfile.setOnClickListener {
            findNavController().navigate(R.id.action_profileFragment_to_editProfileFragment)
        }

        btnLogout.setOnClickListener {
            logoutUser()
        }
    }

    private fun loadUserData() {
        val currentUser = auth.currentUser

        if (currentUser == null) {
            showToast("Пользователь не авторизован")
            findNavController().navigate(R.id.loginFragment)
            return
        }

        // Отображаем базовые данные из Firebase Auth
        tvName.text = currentUser.displayName ?: "Без имени"
        tvEmail.text = currentUser.email ?: "Нет email"

        // ЗАГРУЖАЕМ ДАННЫЕ ИЗ FIRESTORE ЧЕРЕЗ VIEWMODEL
        viewModel.loadUserData()
        viewModel.userData.observe(viewLifecycleOwner) { user ->
            if (user != null) {
                displayUserData(user)
            } else {
                // Если данных нет в Firestore, показываем сообщение
                tvSkills.text = "Данные не загружены. Отредактируйте профиль"
                tvBio.text = "Информация не загружена"
            }
        }

        // Также наблюдаем за состоянием загрузки
        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            if (isLoading) {
                tvSkills.text = "Загрузка навыков..."
                tvBio.text = "Загрузка информации о себе..."
            }
        }
    }

    private fun displayUserData(user: com.example.skillexchange.data.models.User) {
        tvName.text = user.name.ifEmpty { "Без имени" }
        tvRating.text = user.rating.toString()
        tvCompletedExchanges.text = user.completedExchanges.toString()
        tvBio.text = user.bio.ifEmpty { "Пользователь пока не добавил информацию о себе" }

        // Отображаем навыки
        if (user.skills.isNotEmpty()) {
            val skillsText = buildString {
                user.skills.forEach { userSkill ->
                    val skill = skillsRepository.getSkill(userSkill.skillId)
                    if (skill != null) {
                        val levelText = when (userSkill.level) {
                            "BEGINNER" -> "Начинающий"
                            "INTERMEDIATE" -> "Средний"
                            "ADVANCED" -> "Продвинутый"
                            "EXPERT" -> "Эксперт"
                            else -> "Начинающий"
                        }
                        append("• ${skill.name} ($levelText)\n")
                    }
                }
            }
            tvSkills.text = skillsText
        } else {
            tvSkills.text = "Пока нет навыков. Добавьте первый!"
        }
    }

    private fun logoutUser() {
        auth.signOut()
        showToast("Вы вышли из аккаунта")
        findNavController().navigate(R.id.loginFragment)
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }
}