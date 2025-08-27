package com.example.guia_play.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.guia_play.data.repository.AuthRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AuthViewModel(private val authRepository: AuthRepository) : ViewModel() {

    // Uso do UI State
    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()

    private val _eventFlow = MutableSharedFlow<AuthEvent>()
    val eventFlow: SharedFlow<AuthEvent> = _eventFlow.asSharedFlow()

    fun login(email: String, password: String) {
        _uiState.value = _uiState.value.copy(isLoading = true, error = null)
        viewModelScope.launch {
            try {
                authRepository.login(email, password)
                _eventFlow.emit(AuthEvent.LoginSuccess)
                _uiState.value = _uiState.value.copy(isLoading = false, isLoggedIn = true) // Alterado para isLoggedIn
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(isLoading = false, error = e.localizedMessage ?: "Erro de login")
                _eventFlow.emit(AuthEvent.Error(e.localizedMessage ?: "Erro desconhecido"))
            }
        }
    }

    fun register(email: String, password: String) {
        _uiState.value = _uiState.value.copy(isLoading = true, error = null)
        viewModelScope.launch {
            try {
                authRepository.register(email, password)
                _eventFlow.emit(AuthEvent.RegisterSuccess)
                _uiState.value = _uiState.value.copy(isLoading = false) // Não define isLoggedIn aqui, pois o login é feito separadamente
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(isLoading = false, error = e.localizedMessage ?: "Erro de registro")
                _eventFlow.emit(AuthEvent.Error(e.localizedMessage ?: "Erro desconhecido"))
            }
        }
    }

    fun checkAuthStatus() {
        _uiState.value = _uiState.value.copy(isLoggedIn = authRepository.isUserLoggedIn()) // Alterado para isLoggedIn
    }

    fun logout() {
        authRepository.logout()
        _uiState.value = _uiState.value.copy(isLoggedIn = false) // Alterado para isLoggedIn
        viewModelScope.launch {
            _eventFlow.emit(AuthEvent.LogoutSuccess)
        }
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }
}

data class AuthUiState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val isLoggedIn: Boolean = false // Alterado para isLoggedIn
)

sealed class AuthEvent {
    object LoginSuccess : AuthEvent()
    object RegisterSuccess : AuthEvent()
    object LogoutSuccess : AuthEvent()
    data class Error(val message: String) : AuthEvent()
}
