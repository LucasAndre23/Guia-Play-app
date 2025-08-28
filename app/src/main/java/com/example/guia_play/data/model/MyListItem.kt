package com.example.guia_play.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "my_list_items")
data class MyListItem(
    @PrimaryKey val id: String = "",
    val imageUrl: String = "",
    val seasons: String = "",
    val title: String = "",
    val genero: String = "",
    val userId: String? = null
)