package com.example.guia_play.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.guia_play.data.model.MyListItem
import com.example.guia_play.ui.theme.GuiaPLayTheme

@Composable
fun MovieCard(
    item: MyListItem,
    onItemAdded: (MyListItem) -> Unit,
    isAddedToMyList: @Composable (MyListItem) -> Boolean,
    modifier: Modifier = Modifier // Adicionado para permitir customização externa
) {
    // Chama a função isAddedToMyList para obter o estado atual do item
    // Esta chamada retornará o valor booleano do LaunchedEffect dentro da Home Screen
    val addedToList = isAddedToMyList(item)

    Card(
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        modifier = modifier.width(150.dp) // Usa o modifier passado
    ) {
        Column {
            AsyncImage(
                model = item.imageUrl,
                contentDescription = item.title,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                contentScale = ContentScale.Crop
            )

            Column(
                modifier = Modifier.padding(12.dp)
            ) {
                Text(
                    text = item.title,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = item.genero,
                    style = MaterialTheme.typography.bodySmall,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(8.dp))
                Button(
                    onClick = { onItemAdded(item) }, // Delega a ação para quem chamar o MovieCard
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp),
                    contentPadding = PaddingValues(vertical = 4.dp),
                    colors = ButtonDefaults.buttonColors(
                        // Muda a cor com base se está na lista ou não
                        containerColor = if (addedToList) MaterialTheme.colorScheme.primary else Color.LightGray,
                        contentColor = if (addedToList) MaterialTheme.colorScheme.onPrimary else Color.Black
                    )
                ) {
                    // Muda o texto do botão com base se está na lista ou não
                    Text(
                        text = if (addedToList) "Na Minha Lista" else "Adicionar",
                        fontSize = 12.sp
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MovieCardPreview() {
    GuiaPLayTheme {
        Column {
            // Exemplo de item adicionado
            MovieCard(
                item = MyListItem(
                    id = "1", // IDs são importantes para isAddedToMyList
                    imageUrl = "https://res.cloudinary.com/deovjxbec/image/upload/v175324493/vik_wzyeiy6.jpg",
                    seasons = "8 temporadas",
                    title = "Vikings",
                    genero = "História"
                ),
                onItemAdded = {},
                isAddedToMyList = { true } // Simula que o item está na lista para o Preview
            )
            Spacer(modifier = Modifier.height(16.dp))
            // Exemplo de item não adicionado
            MovieCard(
                item = MyListItem(
                    id = "2",
                    imageUrl = "https://example.com/another_movie.jpg",
                    seasons = "Filme",
                    title = "Outro Filme",
                    genero = "Fantasia"
                ),
                onItemAdded = {},
                isAddedToMyList = { false } // Simula que o item NÃO está na lista para o Preview
            )
        }
    }
}