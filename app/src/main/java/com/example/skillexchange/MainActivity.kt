package com.example.skillexchange

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.NavOptions
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

            // Если мы не на экране логина и нажимаем "Назад" - выходим из приложения
            if (navController.currentDestination?.id != R.id.loginFragment) {
                // Создаем NavOptions для очистки стека
                val navOptions = NavOptions.Builder()
                    .setPopUpTo(R.id.nav_graph, true)
                    .setLaunchSingleTop(true)
                    .build()

                navController.navigate(R.id.loginFragment, null, navOptions)
            }
        } else {
            // Пользователь авторизован
            bottomNavigationView.visibility = View.VISIBLE

            // Если на экране логина - переходим на главную, очищая стек
            if (navController.currentDestination?.id == R.id.loginFragment) {
                // Создаем NavOptions для очистки стека
                val navOptions = NavOptions.Builder()
                    .setPopUpTo(R.id.nav_graph, true)
                    .setLaunchSingleTop(true)
                    .build()

                navController.navigate(R.id.homeFragment, null, navOptions)
            }
        }
    }

    override fun onBackPressed() {
        val currentDestination = navController.currentDestination?.id

        // Если пользователь авторизован и на главном экране
        if (auth.currentUser != null && currentDestination == R.id.homeFragment) {
            // Спрашиваем подтверждение выхода
            if (isTaskRoot) { // Если это корневая активность
                showExitConfirmationDialog()
            } else {
                super.onBackPressed()
            }
        } else {
            super.onBackPressed()
        }
    }

    private fun showExitConfirmationDialog() {
        androidx.appcompat.app.AlertDialog.Builder(this)
            .setTitle("Выход")
            .setMessage("Вы действительно хотите выйти из приложения?")
            .setPositiveButton("Да") { _, _ ->
                // Закрываем приложение
                finishAffinity()
            }
            .setNegativeButton("Нет", null)
            .show()
    }
}