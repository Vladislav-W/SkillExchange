package com.example.skillexchange.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.skillexchange.data.models.User
import com.example.skillexchange.data.repository.UserRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch

// ВАЖНО: Конструктор БЕЗ параметров для by viewModels()
class UserViewModel : ViewModel() {

    // Создаем зависимости внутри ViewModel
    private val userRepository = UserRepository()
    private val auth = Firebase.auth

    private val _userData = MutableLiveData<User?>()
    val userData: LiveData<User?> = _userData

    private val _isLoading = MutableLiveData<Boolean>(false)
    val isLoading: LiveData<Boolean> = _isLoading

    fun loadUserData() {
        val currentUser = auth.currentUser
        if (currentUser != null) {
            _isLoading.value = true
            viewModelScope.launch {
                try {
                    val user = userRepository.getUser(currentUser.uid)
                    _userData.value = user
                } catch (e: Exception) {
                    _userData.value = null
                    e.printStackTrace()
                } finally {
                    _isLoading.value = false
                }
            }
        } else {
            _userData.value = null
        }
    }

    fun updateUser(user: User) {
        viewModelScope.launch {
            try {
                userRepository.saveUser(user)
                loadUserData() // Перезагружаем данные после обновления
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}