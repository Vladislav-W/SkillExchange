package com.example.skillexchange

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var navController: NavController
    private lateinit var bottomNavigationView: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Инициализация Firebase
        auth = Firebase.auth

        // Находим элементы
        bottomNavigationView = findViewById(R.id.bottom_navigation)

        // Настраиваем навигацию
        setupNavigation()

        // Слушатель состояния авторизации
        auth.addAuthStateListener { firebaseAuth ->
            updateUI(firebaseAuth.currentUser)
        }
    }

    private fun setupNavigation() {
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController

        // Связываем BottomNavigation с NavController
        bottomNavigationView.setupWithNavController(navController)

        // Скрываем BottomNavigation на экранах логина/регистрации
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.loginFragment, R.id.registerFragment -> {
                    bottomNavigationView.visibility = View.GONE
                }
                else -> {
                    if (auth.currentUser != null) {
                        bottomNavigationView.visibility = View.VISIBLE
                    }
                }
            }
        }
    }

    private fun updateUI(user: com.google.firebase.auth.FirebaseUser?) {
        if (user == null) {
            // Пользователь не авторизован
            bottomNavigationView.visibility = View.GONE
            // Убедимся что на экране логина
            if (navController.currentDestination?.id != R.id.loginFragment) {
                navController.navigate(R.id.loginFragment)
            }
        } else {
            // Пользователь авторизован
            bottomNavigationView.visibility = View.VISIBLE
            // Если на экране логина - переходим на главную
            if (navController.currentDestination?.id == R.id.loginFragment) {
                navController.navigate(R.id.homeFragment)
            }
        }
    }
}