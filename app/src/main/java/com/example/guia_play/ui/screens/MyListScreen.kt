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
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.guia_play.data.datasources.FirebaseAuthDataSource
import com.example.guia_play.data.datasources.FirestoreMyListDataSource
import com.example.guia_play.data.repository.MyListRepository
import com.example.guia_play.ui.components.MyListItemCard
import com.example.guia_play.ui.components.TopAppBarWithNavigationMenu
import com.example.guia_play.ui.theme.GuiaPLayTheme
import com.example.guia_play.ui.viewmodel.MyListViewModel
import com.example.guia_play.ui.viewmodel.ItemActionViewModel
import com.example.guia_play.ui.viewmodel.MyListViewModelFactory
import com.example.guia_play.ui.viewmodel.ItemActionViewModelFactory
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyListScreen(navController: NavController) {
    val authDataSource = remember { FirebaseAuthDataSource() } // Instancia AuthDataSource
    val myListRepository = remember { MyListRepository(FirestoreMyListDataSource()) } // Instancia MyListRepository

    val myListViewModel: MyListViewModel = viewModel(
        factory = MyListViewModelFactory(
            myListRepository = myListRepository, // Passa o repositório
            authDataSource = authDataSource // Passa o AuthDataSource
        )
    )

    val itemActionViewModel: ItemActionViewModel = viewModel(
        factory = ItemActionViewModelFactory(
            myListRepository = myListRepository, // Reutiliza o mesmo repositório
            authDataSource = authDataSource // Reutiliza o mesmo AuthDataSource
        )
    )

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

    // Verifica se o usuário está logado
    val isUserLoggedIn = authDataSource.isUserLoggedIn()

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
                !isUserLoggedIn -> { // Se o usuário não está logado, mostre uma mensagem e peça para fazer login
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = "Você precisa estar logado para ver sua lista.",
                                color = Color.Gray,
                                style = MaterialTheme.typography.bodyLarge
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Button(onClick = { navController.navigate("login") }) {
                                Text("Fazer Login")
                            }
                        }
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