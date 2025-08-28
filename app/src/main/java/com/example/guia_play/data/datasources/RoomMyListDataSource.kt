package com.example.guia_play.data.datasources

import com.example.guia_play.data.model.MyListItem
import com.example.guia_play.data.local.MyListDao
import kotlinx.coroutines.flow.Flow

class RoomMyListDataSource(private val myListDao: MyListDao) {
    suspend fun addItem(item: MyListItem) {
        myListDao.insertItem(item)
    }

    suspend fun removeItem(item: MyListItem) {
        myListDao.deleteItem(item)
    }

    fun getMyList(userId: String): Flow<List<MyListItem>> {
        return myListDao.getMyListItems(userId)
    }

    suspend fun isItemInMyList(itemId: String, userId: String): Boolean {
        return myListDao.isItemInMyList(itemId, userId)
    }
}