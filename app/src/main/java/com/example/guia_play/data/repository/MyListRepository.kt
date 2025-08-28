package com.example.guia_play.data.repository

import com.example.guia_play.data.datasources.FirestoreMyListDataSource
import com.example.guia_play.data.datasources.RoomMyListDataSource
import com.example.guia_play.data.model.MyListItem
import kotlinx.coroutines.flow.Flow

class MyListRepository(
    private val firestoreDataSource: FirestoreMyListDataSource,
    private val roomDataSource: RoomMyListDataSource
) {
    // Funções para adicionar e remover no Room
    suspend fun addRoom(item: MyListItem) {
        roomDataSource.addItem(item)
    }

    suspend fun removeRoom(itemId: String) {
        roomDataSource.removeItem(MyListItem(id = itemId))
    }

    // Funções para adicionar e remover no Firestore
    suspend fun addFirestore(item: MyListItem) {
        // Assume que o item tem userId
        firestoreDataSource.addItem(item, item.userId!!)
    }

    suspend fun removeFirestore(itemId: String) {
        firestoreDataSource.removeItem(itemId)
    }

    // Sincroniza dados do Firestore para o Room
    suspend fun syncFirestoreToRoom(userId: String) {
        val firestoreItems = firestoreDataSource.getMyListItems(userId)
        firestoreItems.forEach { item ->
            roomDataSource.addItem(item)
        }
    }

    // Retorna a lista do cache local
    fun getMyList(userId: String): Flow<List<MyListItem>> {
        return roomDataSource.getMyList(userId)
    }

    // Verifica a existência do item no cache local
    suspend fun isItemInMyList(itemId: String, userId: String): Boolean {
        return roomDataSource.isItemInMyList(itemId, userId)
    }
}