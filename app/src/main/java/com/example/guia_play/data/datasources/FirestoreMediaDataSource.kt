package com.example.guia_play.data.datasources

import com.example.guia_play.data.model.MyListItem
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await
import android.util.Log

class FirestoreMediaDataSource {
    private val db = Firebase.firestore
    private val mediaCollection = db.collection("mediaItems")

    suspend fun getMediaItems(): List<MyListItem> {
        return try {
            val result = mediaCollection.get().await()
            result.map { document ->
                MyListItem(
                    id = document.id,
                    imageUrl = document.getString("urlDaImagem") ?: "",
                    seasons = document.getString("numeroDeTemporadas") ?: "",
                    title = document.getString("titulo") ?: "",
                    genero = document.getString("genero") ?: ""
                )
            }
        } catch (e: Exception) {
            Log.e("FirestoreDataSource", "Erro ao obter itens de mídia: ${e.message}", e)
            throw e
        }
    }

    suspend fun searchMediaItems(query: String): List<MyListItem> {
        if (query.isBlank()) return emptyList()

        return try {
            val lowerCaseQuery = query.lowercase() // A query de busca em minúsculas
            val snapshot = mediaCollection
                .orderBy("titulo_lowercase")
                .startAt(lowerCaseQuery)
                .endAt(lowerCaseQuery + '\uf8ff')
                .get()
                .await()

            snapshot.documents.mapNotNull { document ->
                // Mapeia o documento do Firestore para MyListItem
                MyListItem(
                    id = document.id,
                    imageUrl = document.getString("urlDaImagem") ?: "",
                    seasons = document.getString("numeroDeTemporadas") ?: "",
                    title = document.getString("titulo") ?: "", // Continua pegando o título original para exibição
                    genero = document.getString("genero") ?: ""
                )
            }
        } catch (e: Exception) {
            Log.e("FirestoreDataSource", "Erro ao buscar itens: ${e.message}", e)
            throw e
        }
    }
}