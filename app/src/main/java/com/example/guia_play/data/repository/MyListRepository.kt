package com.example.guia_play.data.repository

import com.example.guia_play.data.datasources.FirestoreMyListDataSource
import com.example.guia_play.data.model.MyListItem
import kotlinx.coroutines.flow.Flow

class MyListRepository(private val dataSource: FirestoreMyListDataSource) {

    suspend fun addItemToMyList(item: MyListItem, userId: String) {
        dataSource.addItem(item, userId)
    }

    suspend fun removeItemFromMyList(itemId: String) {
        dataSource.removeItem(itemId)
    }

    fun getMyList(userId: String): Flow<List<MyListItem>> {
        return dataSource.getMyList(userId)
    }

    suspend fun isItemInMyList(itemId: String, userId: String): Boolean {
        return dataSource.isItemInMyList(itemId, userId)
    }
}