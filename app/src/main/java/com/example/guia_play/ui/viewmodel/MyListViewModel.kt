package com.example.guia_play.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.guia_play.data.datasources.FirebaseAuthDataSource
import com.example.guia_play.data.model.MyListItem
import com.example.guia_play.data.repository.MyListRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class MyListUiState(
    val isLoading: Boolean = false,
    val myItems: List<MyListItem> = emptyList(),
    val error: String? = null
)

class MyListViewModel(
    private val myListRepository: MyListRepository,
    private val authDataSource: FirebaseAuthDataSource
) : ViewModel() {

    // Uso do UI State
    private val _uiState = MutableStateFlow(MyListUiState())
    val uiState: StateFlow<MyListUiState> = _uiState.asStateFlow()

    init {
        loadMyList() // Carrega a lista ao inicializar o ViewModel
    }

    fun loadMyList() {
        _uiState.value = _uiState.value.copy(isLoading = true, error = null)
        viewModelScope.launch {
            val userId = authDataSource.getCurrentUserId()
            if (userId == null) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = "Usuário não logado. Faça login para ver sua lista."
                )
                return@launch
            }

            try {
                // Sincroniza os dados do Firestore para o Room antes de coletar
                myListRepository.syncFirestoreToRoom(userId)

                // Coleta os dados do Room
                myListRepository.getMyList(userId).collect { items ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        myItems = items,
                        error = null
                    )
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = e.localizedMessage ?: "Erro ao carregar lista."
                )
            }
        }
    }
}