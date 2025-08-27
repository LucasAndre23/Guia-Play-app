package com.example.guia_play.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.guia_play.ui.components.MyListItemCard
import com.example.guia_play.ui.components.TopAppBarWithNavigationMenu
import com.example.guia_play.ui.theme.GuiaPLayTheme
import com.example.guia_play.ui.viewmodel.MyListViewModel
import com.example.guia_play.ui.viewmodel.ItemActionViewModel
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyListScreen(navController: NavController) {
    // Usando koinViewModel()
    val myListViewModel: MyListViewModel = koinViewModel()
    // Usando koinViewModel()
    val itemActionViewModel: ItemActionViewModel = koinViewModel()

    val uiState by myListViewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    // Observa o SharedFlow para feedback do snackbar (para remoção de itens)
    LaunchedEffect(itemActionViewModel) {
        itemActionViewModel.actionFeedback.collect { message ->
            scope.launch {
                snackbarHostState.showSnackbar(
                    message = message,
                    withDismissAction = true
                )
            }
            // Recarrega a lista após uma ação de remoção para atualizar a UI
            myListViewModel.loadMyList()
        }
    }

    // O LaunchedEffect para carregar a lista ao entrar na tela
    LaunchedEffect(Unit) {
        myListViewModel.loadMyList()
    }

    Scaffold(
        topBar = {
            val navBackStackEntry by navController.currentBackStackEntryAsState()
            val currentRoute = navBackStackEntry?.destination?.route
            TopAppBarWithNavigationMenu(
                title = "Minha lista",
                navController = navController,
                currentRoute = currentRoute
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp)
        ) {
            when {
                uiState.isLoading -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                }
                uiState.error != null -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text(text = uiState.error ?: "Erro desconhecido", color = Color.Red)
                    }
                }
                uiState.myItems.isEmpty() -> { // Se a lista estiver vazia e não houver erro/carregamento
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text(
                            text = "Sua lista está vazia. Adicione alguns itens na tela inicial!",
                            color = Color.Gray,
                            style = MaterialTheme.typography.bodyLarge,
                            modifier = Modifier.padding(16.dp),
                            textAlign = TextAlign.Center // Melhor para mensagens longas
                        )
                    }
                }
                else -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxWidth(),
                        contentPadding = PaddingValues(vertical = 8.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        items(uiState.myItems) { item ->
                            MyListItemCard(
                                item = item,
                                onRemoveItem = { itemId ->
                                    itemActionViewModel.removeItemFromMyList(itemId)
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun MyListScreenPreview() {
    GuiaPLayTheme {
        MyListScreen(navController = rememberNavController())
    }
}
