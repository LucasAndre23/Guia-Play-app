package com.example.guia_play.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.guia_play.data.model.MyListItem
import com.example.guia_play.data.repository.MediaRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class HomeUiState(
    val recommendedItems: List<MyListItem> = emptyList(),
    val newArrivalsItems: List<MyListItem> = emptyList(),
    val searchResults: List<MyListItem> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

class HomeViewModel(private val mediaRepository: MediaRepository) : ViewModel() {

    // Uso do UI State
    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    private val _searchText = MutableStateFlow("")
    val searchText: StateFlow<String> = _searchText.asStateFlow()

    // NOVO: Estado para controlar se uma busca foi disparada
    private val _searchTriggered = MutableStateFlow(false)
    val searchTriggered: StateFlow<Boolean> = _searchTriggered.asStateFlow()

    init {
        loadHomeContent()
    }

    fun loadHomeContent() {
        _uiState.value = _uiState.value.copy(isLoading = true, error = null)
        viewModelScope.launch {
            try {
                _searchText.value = ""
                _uiState.value = _uiState.value.copy(searchResults = emptyList())
                _searchTriggered.value = false // Reseta o estado de busca disparada

                val recommended = mediaRepository.getRecommendedItems()
                val newArrivals = mediaRepository.getNewArrivalsItems()

                _uiState.value = _uiState.value.copy(
                    recommendedItems = recommended,
                    newArrivalsItems = newArrivals,
                    isLoading = false
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    error = "Erro ao carregar dados: ${e.localizedMessage}",
                    isLoading = false
                )
            }
        }
    }

    fun onSearchTextChanged(text: String) {
        _searchText.value = text
        // Se o texto de busca estiver vazio, limpa os resultados e reseta o estado de busca disparada
        if (text.isBlank()) {
            clearSearchResults()
        }
        // IMPORTANTE: Se o usuário começar a digitar novamente, a busca não está "disparada" ainda
        _searchTriggered.value = false
    }

    fun searchMedia(query: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null, searchResults = emptyList())
            _searchTriggered.value = true // Define como true quando a busca é disparada
            try {
                val results = mediaRepository.searchMediaItems(query)
                _uiState.value = _uiState.value.copy(
                    searchResults = results,
                    isLoading = false
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    error = "Erro ao buscar: ${e.localizedMessage}",
                    isLoading = false
                )
            }
        }
    }

    fun clearSearchResults() {
        _uiState.value = _uiState.value.copy(searchResults = emptyList())
        _searchText.value = ""
        _searchTriggered.value = false // Reseta o estado de busca disparada
    }
}