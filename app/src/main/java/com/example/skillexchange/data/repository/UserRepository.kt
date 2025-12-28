// UserRepository.kt
package com.example.skillexchange.data.repository

import android.util.Log
import com.example.skillexchange.data.models.User
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await

class UserRepository {
    private val db: FirebaseFirestore = Firebase.firestore
    private val usersCollection = db.collection("users")

    // Сохранить пользователя
    suspend fun saveUser(user: User): Boolean {
        return try {
            usersCollection.document(user.uid).set(user).await()
            true
        } catch (e: Exception) {
            Log.e("UserRepository", "Ошибка сохранения пользователя", e)
            false
        }
    }

    // Получить пользователя по ID
    suspend fun getUser(uid: String): User? {
        return try {
            val document = usersCollection.document(uid).get().await()
            if (document.exists()) {
                document.toObject(User::class.java)
            } else {
                null
            }
        } catch (e: Exception) {
            Log.e("UserRepository", "Ошибка получения пользователя", e)
            null
        }
    }

    // Обновить данные пользователя
    suspend fun updateUser(uid: String, updates: Map<String, Any>): Boolean {
        return try {
            usersCollection.document(uid).update(updates).await()
            true
        } catch (e: Exception) {
            Log.e("UserRepository", "Ошибка обновления пользователя", e)
            false
        }
    }

    // Получить всех пользователей
    suspend fun getAllUsers(): List<User> {
        return try {
            val querySnapshot = usersCollection.get().await()
            querySnapshot.documents.mapNotNull { it.toObject(User::class.java) }
        } catch (e: Exception) {
            Log.e("UserRepository", "Ошибка получения всех пользователей", e)
            emptyList()
        }
    }

    // Получить пользователей по навыкам
    suspend fun getUsersBySkill(skill: String): List<User> {
        return try {
            val querySnapshot = usersCollection
                .whereArrayContains("skills", skill)
                .get()
                .await()
            querySnapshot.documents.mapNotNull { it.toObject(User::class.java) }
        } catch (e: Exception) {
            Log.e("UserRepository", "Ошибка получения пользователей по навыку", e)
            emptyList()
        }
    }
}