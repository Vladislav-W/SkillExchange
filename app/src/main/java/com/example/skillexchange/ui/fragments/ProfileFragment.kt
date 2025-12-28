package com.example.skillexchange.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.skillexchange.R
import com.example.skillexchange.data.models.User
import com.example.skillexchange.data.repository.UserRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ProfileFragment : Fragment() {

    private lateinit var auth: FirebaseAuth
    private val userRepository = UserRepository()
    private val coroutineScope = CoroutineScope(Dispatchers.Main)

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
        // Используем новый макет fragment_profile.xml
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth = Firebase.auth

        // Инициализируем UI элементы (должны совпадать с fragment_profile.xml)
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

        // Настройка кнопки редактирования профиля
        btnEditProfile.setOnClickListener {
            // Переход на экран редактирования
            findNavController().navigate(R.id.action_profileFragment_to_editProfileFragment)
        }

        // Настройка кнопки выхода
        btnLogout.setOnClickListener {
            logout()
        }
    }

    private fun loadUserData() {
        val currentUser = auth.currentUser

        if (currentUser == null) {
            showToast("Пользователь не авторизован")
            return
        }

        // Показываем email из Firebase Auth (он всегда есть)
        tvEmail.text = currentUser.email ?: "Email не указан"

        // Загружаем остальные данные из Firestore
        coroutineScope.launch {
            try {
                val userData = userRepository.getUser(currentUser.uid)

                if (userData != null) {
                    // Заполняем данные из Firestore
                    tvName.text = userData.name.ifEmpty { "Без имени" }
                    tvRating.text = String.format("%.1f", userData.rating)
                    tvCompletedExchanges.text = userData.completedExchanges.toString()

                    // Навыки
                    if (userData.skills.isNotEmpty()) {
                        tvSkills.text = userData.skills.joinToString(", ")
                    } else {
                        tvSkills.text = "Пока нет навыков. Добавьте первый!"
                    }

                    // О себе
                    if (userData.bio.isNotEmpty()) {
                        tvBio.text = userData.bio
                    } else {
                        tvBio.text = "Расскажите о себе, чтобы другие пользователи могли лучше узнать вас"
                    }
                } else {
                    // Если пользователь есть в Auth, но нет в Firestore
                    tvName.text = "Без имени"
                    tvSkills.text = "Пока нет навыков"
                    tvBio.text = "Расскажите о себе"

                    // Создаем запись в Firestore
                    val newUser = User(
                        uid = currentUser.uid,
                        email = currentUser.email ?: "",
                        name = currentUser.displayName ?: currentUser.email?.substringBefore("@") ?: "User"
                    )
                    userRepository.saveUser(newUser)
                }
            } catch (e: Exception) {
                showToast("Ошибка загрузки данных: ${e.message}")
            }
        }
    }

    private fun logout() {
        auth.signOut()
        showToast("Вы вышли из аккаунта")

        // MainActivity автоматически перенаправит на логин через AuthStateListener
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    override fun onResume() {
        super.onResume()
        // Обновляем данные при возвращении на экран
        loadUserData()
    }
}