package com.example.guia_play.di

import com.example.guia_play.data.datasources.FirebaseAuthDataSource
import com.example.guia_play.data.datasources.FirestoreMediaDataSource
import com.example.guia_play.data.datasources.FirestoreMyListDataSource
import com.example.guia_play.data.repository.AuthRepository
import com.example.guia_play.data.repository.MediaRepository
import com.example.guia_play.data.repository.MyListRepository
import com.example.guia_play.ui.viewmodel.AuthViewModel
import com.example.guia_play.ui.viewmodel.HomeViewModel
import com.example.guia_play.ui.viewmodel.ItemActionViewModel
import com.example.guia_play.ui.viewmodel.MyListViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {

    // Data Sources
    single { FirebaseAuthDataSource() }
    single { FirestoreMediaDataSource() }
    single { FirestoreMyListDataSource() }

    // Repositories
    single { AuthRepository(get()) }
    single { MediaRepository(get()) }
    single { MyListRepository(get()) }

    // ViewModels
    viewModel { AuthViewModel(get()) }
    viewModel { HomeViewModel(get()) }
    viewModel { ItemActionViewModel(get(), get()) }
    viewModel { MyListViewModel(get(), get()) }
}