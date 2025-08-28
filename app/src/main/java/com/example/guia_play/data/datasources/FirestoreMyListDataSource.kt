package com.example.guia_play.data.datasources

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject
import com.example.guia_play.data.model.MyListItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await

class FirestoreMyListDataSource {
    private val db = FirebaseFirestore.getInstance()
    private val myListCollection = db.collection("myList")

    /**
     * Adiciona um item à lista pessoal do usuário.
     * @param item O MyListItem a ser adicionado.
     * @param userId O ID do usuário ao qual o item pertence.
     */
    suspend fun addItem(item: MyListItem, userId: String) {
        // Cria uma cópia do item com o userId associado antes de salvar
        val itemWithUser = item.copy(userId = userId)
        myListCollection.document(item.id).set(itemWithUser).await()
    }

    /**
     * Remove um item da lista pessoal.
     * @param itemId O ID do item a ser removido.
     */
    suspend fun removeItem(itemId: String) {
        myListCollection.document(itemId).delete().await()
    }

    /**
     * Retorna um Flow de todos os itens na lista pessoal para um userId específico.
     * Os itens são filtrados pelo campo 'userId' no Firestore.
     * @param userId O ID do usuário para o qual a lista deve ser recuperada.
     */
    fun getMyList(userId: String): Flow<List<MyListItem>> = flow {
        // Filtra a coleção pelo campo 'userId' para obter apenas os itens do usuário atual
        val snapshot = myListCollection.whereEqualTo("userId", userId).get().await()
        val items = snapshot.documents.mapNotNull { it.toObject<MyListItem>() }
        emit(items)
    }

    /**
     * Verifica se um item está presente na lista pessoal de um usuário específico.
     * @param itemId O ID do item a ser verificado.
     * @param userId O ID do usuário cuja lista deve ser verificada.
     * @return true se o item existe na lista do usuário, false caso contrário.
     */
    suspend fun isItemInMyList(itemId: String, userId: String): Boolean {
        val doc = myListCollection
            .document(itemId) // Tenta obter o documento pelo ID do item
            .get()
            .await()
        // Verifica se o documento existe E se o campo 'userId' no documento corresponde ao userId fornecido
        return doc.exists() && doc.getString("userId") == userId
    }

    suspend fun getMyListItems(userId: String): List<MyListItem> {
        return try {
            val snapshot = myListCollection
                .whereEqualTo("userId", userId)
                .get()
                .await()
            snapshot.toObjects(MyListItem::class.java)
        } catch (e: Exception) {
            throw e
        }
    }
}