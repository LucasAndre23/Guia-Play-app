package com.example.guia_play.di

import androidx.room.Room
import com.example.guia_play.data.datasources.FirebaseAuthDataSource
import com.example.guia_play.data.datasources.FirestoreMediaDataSource
import com.example.guia_play.data.datasources.FirestoreMyListDataSource
import com.example.guia_play.data.datasources.RoomMyListDataSource
import com.example.guia_play.data.local.AppDatabase
import com.example.guia_play.data.repository.AuthRepository
import com.example.guia_play.data.repository.MediaRepository
import com.example.guia_play.data.repository.MyListRepository
import com.example.guia_play.ui.viewmodel.AuthViewModel
import com.example.guia_play.ui.viewmodel.HomeViewModel
import com.example.guia_play.ui.viewmodel.ItemActionViewModel
import com.example.guia_play.ui.viewmodel.MyListViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {

    // Data Sources
    single { FirebaseAuthDataSource() }
    single { FirestoreMediaDataSource() }
    single { FirestoreMyListDataSource() }

    // Configuração e DataSource do Room
    single {
        Room.databaseBuilder(
            androidContext(),
            AppDatabase::class.java,
            "guia_play_db"
        ).build()
    }
    single { get<AppDatabase>().myListDao() }
    single { RoomMyListDataSource(get()) }

    // Repositórios
    single { AuthRepository(get()) }
    single { MediaRepository(get()) }
    // Recebe ambos os DataSources
    single { MyListRepository(get(), get()) }

    // ViewModels
    viewModel { AuthViewModel(get()) }
    viewModel { HomeViewModel(get()) }
    // Recebendo o MyListRepository e o FirebaseAuthDataSource
    viewModel { ItemActionViewModel(get(), get()) }
    // Recebendo o MyListRepository e o FirebaseAuthDataSource
    viewModel { MyListViewModel(get(), get()) }
}