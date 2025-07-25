package com.example.guia_play.data.repository

import com.example.guia_play.data.datasources.FirebaseAuthDataSource

class AuthRepository(private val dataSource: FirebaseAuthDataSource) {

    suspend fun register(email: String, password: String) {
        dataSource.createUser(email, password)
    }

    suspend fun login(email: String, password: String) {
        dataSource.signIn(email, password)
    }

    fun logout() {
        dataSource.signOut()
    }

    fun getCurrentUserId(): String? {
        return dataSource.getCurrentUserId()
    }

    fun isUserLoggedIn(): Boolean {
        return dataSource.isUserLoggedIn()
    }
}