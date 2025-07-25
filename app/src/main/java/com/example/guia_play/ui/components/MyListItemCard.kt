package com.example.guia_play.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
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
fun MyListItemCard(
    item: MyListItem,
    onRemoveItem: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        modifier = modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = item.imageUrl,
                contentDescription = item.title,
                modifier = Modifier
                    .size(80.dp)
                    .clip(RoundedCornerShape(4.dp)),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column(
                modifier = Modifier.weight(1f) // Ocupa o espaço restante
            ) {
                Text(
                    text = item.title,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "${item.genero} - ${item.seasons}",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            // Botão de remover
            IconButton(
                onClick = { onRemoveItem(item.id) }, // Chama o callback com o ID do item
                modifier = Modifier.size(40.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Remover da lista",
                    tint = MaterialTheme.colorScheme.error // Cor para indicar ação de remover
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MyListItemCardPreview() {
    GuiaPLayTheme {
        MyListItemCard(
            item = MyListItem(
                id = "1",
                imageUrl = "https://res.cloudinary.com/deovjxbec/image/upload/v175324493/vik_wzyeiy6.jpg",
                title = "Vikings",
                seasons = "6 Temporadas",
                genero = "História, Drama",
                userId = "testUser123"
            ),
            onRemoveItem = {}
        )
    }
}