// LoginFragment.kt (обновленный)
package com.example.skillexchange.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.skillexchange.R
import com.example.skillexchange.data.repository.UserRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope // ДОБАВЬТЕ
import kotlinx.coroutines.Dispatchers // ДОБАВЬТЕ

class LoginFragment : Fragment() {

    private lateinit var auth: FirebaseAuth
    private val userRepository = UserRepository() // ДОБАВЬТЕ ЭТУ СТРОКУ
    private val coroutineScope = CoroutineScope(Dispatchers.Main) // ДОБАВЬТЕ ЭТУ СТРОКУ

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Используем НОВЫЙ layout fragment_login
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth = Firebase.auth

        // Инициализируем элементы из fragment_login
        val etEmail = view.findViewById<EditText>(R.id.etEmail)
        val etPassword = view.findViewById<EditText>(R.id.etPassword)
        val btnLogin = view.findViewById<Button>(R.id.btnLogin)
        val btnRegister = view.findViewById<Button>(R.id.btnRegister)
        val tvForgotPassword = view.findViewById<TextView>(R.id.tvForgotPassword)
        val progressBar = view.findViewById<ProgressBar>(R.id.progressBar)

        btnLogin.setOnClickListener {
            val email = etEmail.text.toString().trim()
            val password = etPassword.text.toString().trim()

            if (validateInput(email, password)) {
                loginUser(email, password, progressBar)
            }
        }

        btnRegister.setOnClickListener {
            // Используем созданный action для перехода
            findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
        }

        tvForgotPassword.setOnClickListener {
            showForgotPasswordDialog()
        }
    }

    private fun validateInput(email: String, password: String): Boolean {
        if (email.isEmpty()) {
            showToast("Введите email")
            return false
        }
        if (password.isEmpty()) {
            showToast("Введите пароль")
            return false
        }
        if (password.length < 6) {
            showToast("Пароль должен быть минимум 6 символов")
            return false
        }
        return true
    }

    private fun loginUser(email: String, password: String, progressBar: ProgressBar) {
        progressBar.visibility = View.VISIBLE

        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(requireActivity()) { task ->
                progressBar.visibility = View.GONE

                if (task.isSuccessful) {
                    // Firebase сохранит сессию автоматически
                    // MainActivity сам переведет на homeFragment через updateUI()
                    Toast.makeText(requireContext(), "Вход выполнен!", Toast.LENGTH_SHORT).show()

                    // НЕ делаем navigate здесь - MainActivity сделает это через AuthStateListener
                    // findNavController().navigate(R.id.homeFragment) // <-- УДАЛИТЬ ЭТУ СТРОКУ
                } else {
                    Toast.makeText(requireContext(),
                        "Ошибка: ${task.exception?.message}", Toast.LENGTH_LONG).show()
                }
            }
    }

    private fun showForgotPasswordDialog() {
        Toast.makeText(requireContext(),
            "Функция восстановления пароля в разработке", Toast.LENGTH_SHORT).show()
    }

    private fun showToast(message: String) { // ДОБАВЬТЕ ЭТУ ФУНКЦИЮ
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }
}