package com.example.guia_play.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.guia_play.data.datasources.FirebaseAuthDataSource
import com.example.guia_play.data.datasources.FirestoreMediaDataSource
import com.example.guia_play.data.datasources.FirestoreMyListDataSource
import com.example.guia_play.data.repository.AuthRepository
import com.example.guia_play.data.repository.MediaRepository
import com.example.guia_play.data.repository.MyListRepository

// AuthViewModelFactory
class AuthViewModelFactory(private val authRepository: AuthRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AuthViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return AuthViewModel(authRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

// HomeViewModelFactory
class HomeViewModelFactory(private val mediaRepository: MediaRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return HomeViewModel(mediaRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

// ItemActionViewModelFactory
class ItemActionViewModelFactory(
    private val myListRepository: MyListRepository,
    private val authDataSource: FirebaseAuthDataSource
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ItemActionViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ItemActionViewModel(myListRepository, authDataSource) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

// MyListViewModelFactory
class MyListViewModelFactory(
    private val myListRepository: MyListRepository,
    private val authDataSource: FirebaseAuthDataSource
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MyListViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MyListViewModel(myListRepository, authDataSource) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}