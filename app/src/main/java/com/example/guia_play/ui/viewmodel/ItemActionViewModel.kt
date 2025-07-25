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

// Construtor que recebe myListRepository E authDataSource
class ItemActionViewModel(
    private val myListRepository: MyListRepository,
    private val authDataSource: FirebaseAuthDataSource
) : ViewModel() {

    private val _actionFeedback = MutableSharedFlow<String>()
    val actionFeedback: SharedFlow<String> = _actionFeedback.asSharedFlow()

    // addItemToMyList AGORA RECEBE MyListItem diretamente
    // Não há necessidade de conversão de MediaItem, pois ele não existe no seu projeto.
    fun addItemToMyList(item: MyListItem) {
        val userId = authDataSource.getCurrentUserId()
        if (userId == null) {
            viewModelScope.launch {
                _actionFeedback.emit("Erro: Usuário não logado. Faça login para adicionar itens à lista.")
            }
            return
        }

        // Garante que o item sendo salvo tem o userId correto associado.
        // Se o item já veio com userId, ele será sobrescrito/confirmado.
        val itemToSave = item.copy(userId = userId)

        viewModelScope.launch {
            try {
                myListRepository.addItemToMyList(itemToSave, userId) // Passa o item com userId e o userId
                _actionFeedback.emit("Adicionado à Minha Lista!")
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
                myListRepository.removeItemFromMyList(itemId) // Chama a função do repositório
                _actionFeedback.emit("Removido da Minha Lista!")
            } catch (e: Exception) {
                _actionFeedback.emit("Erro ao remover: ${e.localizedMessage}")
            }
        }
    }

    // isItemInMyList OBTÉM O userId internamente
    suspend fun isItemInMyList(itemId: String): Boolean {
        val userId = authDataSource.getCurrentUserId()
        return if (userId != null) {
            myListRepository.isItemInMyList(itemId, userId)
        } else {
            false // Se não há userId, o item não pode estar na lista do usuário logado
        }
    }
}