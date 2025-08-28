package com.example.guia_play.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.guia_play.data.datasources.FirebaseAuthDataSource
import com.example.guia_play.data.model.MyListItem
import com.example.guia_play.data.repository.MyListRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

class ItemActionViewModel(
    private val myListRepository: MyListRepository,
    private val authDataSource: FirebaseAuthDataSource
) : ViewModel() {

    private val _actionFeedback = MutableSharedFlow<String>()
    val actionFeedback: SharedFlow<String> = _actionFeedback.asSharedFlow()

    fun addItemToMyList(item: MyListItem) {
        val userId = authDataSource.getCurrentUserId()
        if (userId == null) {
            viewModelScope.launch {
                _actionFeedback.emit("Erro: Usuário não logado. Faça login para adicionar itens à lista.")
            }
            return
        }

        val itemToSave = item.copy(userId = userId)

        viewModelScope.launch {
            try {
                // 1. Adiciona o item no Room (operação rápida e offline)
                myListRepository.addRoom(itemToSave)
                // A UI se atualiza
                _actionFeedback.emit("Adicionado à Minha Lista!")

                // 2. Tenta sincronizar com o Firestore em segundo plano
                myListRepository.addFirestore(itemToSave)
            } catch (e: Exception) {
                _actionFeedback.emit("Erro ao adicionar: ${e.localizedMessage}")
            }
        }
    }

    fun removeItemFromMyList(itemId: String) {
        val userId = authDataSource.getCurrentUserId()
        if (userId == null) {
            viewModelScope.launch {
                _actionFeedback.emit("Erro: Usuário não logado. Não é possível remover itens.")
            }
            return
        }
        viewModelScope.launch {
            try {
                // 1. Remove o item do Room (operação rápida e offline)
                myListRepository.removeRoom(itemId)
                // A UI se atualiza
                _actionFeedback.emit("Removido da Minha Lista!")

                // 2. Tenta sincronizar com o Firestore em segundo plano
                myListRepository.removeFirestore(itemId)
            } catch (e: Exception) {
                _actionFeedback.emit("Erro ao remover: ${e.localizedMessage}")
            }
        }
    }

    suspend fun isItemInMyList(itemId: String): Boolean {
        val userId = authDataSource.getCurrentUserId()
        return if (userId != null) {
            // A verificação é sempre feita no Room
            myListRepository.isItemInMyList(itemId, userId)
        } else {
            false
        }
    }
}