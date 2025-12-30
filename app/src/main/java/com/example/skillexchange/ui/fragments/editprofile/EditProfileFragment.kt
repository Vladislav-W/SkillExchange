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
import com.example.skillexchange.SkillWithLevelDialog
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

    // Выбранные навыки с уровнями
    private val selectedSkills = mutableListOf<User.UserSkill>()

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
        skillsRepository = SkillsRepository(requireContext())

        // Инициализация UI элементов
        etName = view.findViewById(R.id.etName)
        etBio = view.findViewById(R.id.etBio)
        chipGroupSkills = view.findViewById(R.id.chipGroupSkills)
        btnSelectSkills = view.findViewById(R.id.btnSelectSkills)
        tvSelectedSkillsCount = view.findViewById(R.id.tvSelectedSkillsCount)
        btnSave = view.findViewById(R.id.btnSave)
        btnCancel = view.findViewById(R.id.btnCancel)
        progressBar = view.findViewById(R.id.progressBar)

        // Активируем кнопку выбора навыков
        btnSelectSkills.isEnabled = true
        btnSelectSkills.text = "Выбрать навыки"

        // Обработчик для кнопки выбора навыков
        btnSelectSkills.setOnClickListener {
            showSkillSelectionDialog()
        }

        // Загружаем текущие данные пользователя
        loadCurrentUserData()

        // Обработчики кнопок
        btnSave.setOnClickListener {
            saveProfile()
        }

        btnCancel.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun showSkillSelectionDialog() {
        val dialog = SkillWithLevelDialog().apply {
            setInitialSelectedSkills(selectedSkills)

            setSkillSelectionListener(object : SkillWithLevelDialog.SkillSelectionListener {
                override fun onSkillsSelected(selectedSkills: List<User.UserSkill>) {
                    this@EditProfileFragment.selectedSkills.clear()
                    this@EditProfileFragment.selectedSkills.addAll(selectedSkills)
                    updateSkillsUI()
                }
            })
        }

        dialog.show(parentFragmentManager, "SkillWithLevelDialog")
    }

    private fun loadCurrentUserData() {
        val currentUser = auth.currentUser

        if (currentUser == null) {
            findNavController().popBackStack()
            return
        }

        coroutineScope.launch {
            try {
                val userData = userRepository.getUser(currentUser.uid)

                if (userData != null) {
                    etName.setText(userData.name)
                    etBio.setText(userData.bio)

                    // Загружаем навыки
                    selectedSkills.clear()
                    selectedSkills.addAll(userData.skills)
                    updateSkillsUI()
                }
            } catch (e: Exception) {
                // Продолжаем с пустыми полями
            }
        }
    }

    private fun updateSkillsUI() {
        chipGroupSkills.removeAllViews()

        selectedSkills.forEach { userSkill ->
            val skill = skillsRepository.getSkill(userSkill.skillId)
            if (skill != null) {
                val levelText = when (userSkill.level) {
                    "BEGINNER" -> "Начинающий"
                    "INTERMEDIATE" -> "Средний"
                    "ADVANCED" -> "Продвинутый"
                    "EXPERT" -> "Эксперт"
                    else -> "Начинающий"
                }

                val chip = Chip(requireContext()).apply {
                    text = "${skill.name} ($levelText)"
                    isCloseIconVisible = true
                    setOnCloseIconClickListener {
                        selectedSkills.remove(userSkill)
                        updateSkillsUI()
                    }
                }
                chipGroupSkills.addView(chip)
            }
        }

        tvSelectedSkillsCount.text = if (selectedSkills.isNotEmpty()) {
            "Выбрано: ${selectedSkills.size} навыков"
        } else {
            "Навыки не выбраны"
        }
    }

    private fun saveProfile() {
        val name = etName.text.toString().trim()
        val bio = etBio.text.toString().trim()

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
                val currentUserData = userRepository.getUser(currentUser.uid)

                val updatedUser = currentUserData?.copy(
                    name = name,
                    bio = bio,
                    skills = selectedSkills
                ) ?: User(
                    uid = currentUser.uid,
                    email = currentUser.email ?: "",
                    name = name,
                    bio = bio,
                    skills = selectedSkills
                )

                val isSaved = userRepository.saveUser(updatedUser)

                if (isSaved) {
                    showToast("Профиль успешно обновлен!")
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

    private fun showToast(message: String) {
        android.widget.Toast.makeText(requireContext(), message, android.widget.Toast.LENGTH_SHORT).show()
    }
}