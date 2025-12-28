package com.example.skillexchange.ui.fragments.editprofile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.skillexchange.R
import com.example.skillexchange.data.models.Skill
import com.example.skillexchange.data.models.User
import com.example.skillexchange.data.repository.SkillsRepository
import com.example.skillexchange.data.repository.UserRepository
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class EditProfileFragment : Fragment() {

    private lateinit var auth: FirebaseAuth
    private val userRepository = UserRepository()
    private lateinit var skillsRepository: SkillsRepository
    private val coroutineScope = CoroutineScope(Dispatchers.Main)

    // UI элементы
    private lateinit var etName: EditText
    private lateinit var etBio: EditText
    private lateinit var chipGroupSkills: ChipGroup
    private lateinit var btnSelectSkills: Button
    private lateinit var tvSelectedSkillsCount: TextView
    private lateinit var btnSave: Button
    private lateinit var btnCancel: Button
    private lateinit var progressBar: ProgressBar

    // Выбранные навыки (пока пустой список)
    private val selectedSkills = mutableListOf<Skill>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_edit_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth = Firebase.auth
        skillsRepository = SkillsRepository(requireContext()) // Инициализируем здесь

        // Инициализация UI элементов
        etName = view.findViewById(R.id.etName)
        etBio = view.findViewById(R.id.etBio)
        chipGroupSkills = view.findViewById(R.id.chipGroupSkills)
        btnSelectSkills = view.findViewById(R.id.btnSelectSkills)
        tvSelectedSkillsCount = view.findViewById(R.id.tvSelectedSkillsCount)
        btnSave = view.findViewById(R.id.btnSave)
        btnCancel = view.findViewById(R.id.btnCancel)
        progressBar = view.findViewById(R.id.progressBar)

        // Временно отключаем кнопку выбора навыков
        btnSelectSkills.isEnabled = false
        btnSelectSkills.text = "Навыки (скоро)"

        // Загружаем текущие данные пользователя
        loadCurrentUserData()

        // Обработчики кнопок
        btnSave.setOnClickListener {
            saveProfile()
        }

        btnCancel.setOnClickListener {
            // Возврат назад
            findNavController().popBackStack()
        }
    }

    private fun loadCurrentUserData() {
        val currentUser = auth.currentUser

        if (currentUser == null) {
            // Пользователь не авторизован - возвращаемся назад
            findNavController().popBackStack()
            return
        }

        coroutineScope.launch {
            try {
                val userData = userRepository.getUser(currentUser.uid)

                if (userData != null) {
                    // Заполняем поля текущими данными
                    etName.setText(userData.name)
                    etBio.setText(userData.bio)

                    // Временно: загружаем навыки как строки (будет заменено)
                    loadSelectedSkillsAsStrings(userData.skills)
                }
            } catch (e: Exception) {
                // Продолжаем с пустыми полями
            }
        }
    }

    private fun loadSelectedSkillsAsStrings(skillIds: List<String>) {
        // Временная функция - просто показываем ID как текст
        updateSkillsUI(skillIds)
    }

    private fun updateSkillsUI(skillNames: List<String> = emptyList()) {
        chipGroupSkills.removeAllViews()

        // Добавляем чипы для каждого навыка
        skillNames.forEach { skillName ->
            val chip = Chip(requireContext()).apply {
                text = skillName
                isCloseIconVisible = true
                setOnCloseIconClickListener {
                    // Временно: просто удаляем из UI
                    // Позже будем удалять из selectedSkills
                }
            }
            chipGroupSkills.addView(chip)
        }

        // Обновляем счетчик
        tvSelectedSkillsCount.text = if (skillNames.isNotEmpty()) {
            "Выбрано: ${skillNames.size} навыков"
        } else {
            "Навыки не выбраны"
        }
    }

    private fun saveProfile() {
        val name = etName.text.toString().trim()
        val bio = etBio.text.toString().trim()

        // Валидация
        if (name.isEmpty()) {
            showToast("Введите имя")
            etName.requestFocus()
            return
        }

        val currentUser = auth.currentUser
        if (currentUser == null) {
            showToast("Ошибка: пользователь не авторизован")
            return
        }

        progressBar.visibility = View.VISIBLE

        coroutineScope.launch {
            try {
                // Получаем текущего пользователя
                val currentUserData = userRepository.getUser(currentUser.uid)

                // Временно: берем навыки из чипов (текст)
                val skillIds = getSkillIdsFromChips()

                // Создаем обновленного пользователя
                val updatedUser = currentUserData?.copy(
                    name = name,
                    bio = bio,
                    skills = skillIds
                ) ?: User(
                    uid = currentUser.uid,
                    email = currentUser.email ?: "",
                    name = name,
                    bio = bio,
                    skills = skillIds
                )

                // Сохраняем в Firestore
                val isSaved = userRepository.saveUser(updatedUser)

                if (isSaved) {
                    showToast("Профиль успешно обновлен!")
                    // Возвращаемся назад
                    findNavController().popBackStack()
                } else {
                    showToast("Ошибка сохранения. Попробуйте снова.")
                }
            } catch (e: Exception) {
                showToast("Ошибка: ${e.message}")
            } finally {
                progressBar.visibility = View.GONE
            }
        }
    }

    private fun getSkillIdsFromChips(): List<String> {
        // Временная функция - возвращаем текст чипов как ID
        val skillIds = mutableListOf<String>()
        for (i in 0 until chipGroupSkills.childCount) {
            val chip = chipGroupSkills.getChildAt(i) as Chip
            skillIds.add(chip.text.toString())
        }
        return skillIds
    }

    private fun showToast(message: String) {
        android.widget.Toast.makeText(requireContext(), message, android.widget.Toast.LENGTH_SHORT).show()
    }
}