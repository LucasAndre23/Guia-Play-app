package com.example.guia_play.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.guia_play.data.model.MyListItem
import kotlinx.coroutines.flow.Flow

@Dao
interface MyListDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertItem(item: MyListItem)

    @Delete
    suspend fun deleteItem(item: MyListItem)

    @Query("SELECT * FROM my_list_items WHERE userId = :userId")
    fun getMyListItems(userId: String): Flow<List<MyListItem>>

    @Query("SELECT EXISTS(SELECT 1 FROM my_list_items WHERE id = :itemId AND userId = :userId LIMIT 1)")
    suspend fun isItemInMyList(itemId: String, userId: String): Boolean
}