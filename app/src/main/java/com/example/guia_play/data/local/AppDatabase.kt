package com.example.guia_play.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.guia_play.data.model.MyListItem

@Database(entities = [MyListItem::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun myListDao(): MyListDao
}