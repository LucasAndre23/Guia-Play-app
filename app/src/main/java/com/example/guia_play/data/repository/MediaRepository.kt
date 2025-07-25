package com.example.guia_play.data.repository

import com.example.guia_play.data.datasources.FirestoreMediaDataSource
import com.example.guia_play.data.model.MyListItem

class MediaRepository(private val mediaDataSource: FirestoreMediaDataSource) {

    suspend fun getRecommendedItems(): List<MyListItem> {
        return mediaDataSource.getMediaItems().shuffled()
    }

    suspend fun getNewArrivalsItems(): List<MyListItem> {
        return mediaDataSource.getMediaItems().shuffled()
    }

    suspend fun searchMediaItems(query: String): List<MyListItem> {
        return mediaDataSource.searchMediaItems(query)
    }
}