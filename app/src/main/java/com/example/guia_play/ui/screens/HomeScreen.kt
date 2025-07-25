package com.example.guia_play.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.guia_play.data.datasources.FirebaseAuthDataSource
import com.example.guia_play.data.datasources.FirestoreMediaDataSource
import com.example.guia_play.data.datasources.FirestoreMyListDataSource
import com.example.guia_play.data.repository.MediaRepository
import com.example.guia_play.data.repository.MyListRepository
import com.example.guia_play.ui.components.MovieCard
import com.example.guia_play.ui.components.TopAppBarWithNavigationMenu
import com.example.guia_play.ui.theme.GuiaPLayTheme
import com.example.guia_play.ui.viewmodel.HomeViewModel
import com.example.guia_play.ui.viewmodel.HomeViewModelFactory
import com.example.guia_play.ui.viewmodel.ItemActionViewModel
import com.example.guia_play.ui.viewmodel.ItemActionViewModelFactory
import kotlinx.coroutines.launch
import android.util.Log
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavController) {
    val homeViewModel: HomeViewModel = viewModel(
        factory = HomeViewModelFactory(
            MediaRepository(FirestoreMediaDataSource())
        )
    )
    val authDataSource = remember { FirebaseAuthDataSource() }

    val itemActionViewModel: ItemActionViewModel = viewModel(
        factory = ItemActionViewModelFactory(
            MyListRepository(FirestoreMyListDataSource()),
            authDataSource
        )
    )

    val uiState by homeViewModel.uiState.collectAsState()
    val searchText by homeViewModel.searchText.collectAsState()
    val searchTriggered by homeViewModel.searchTriggered.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    LaunchedEffect(itemActionViewModel) {
        itemActionViewModel.actionFeedback.collect { message ->
            scope.launch {
                snackbarHostState.showSnackbar(
                    message = message,
                    withDismissAction = true
                )
            }
        }
    }

    val isUserLoggedIn = authDataSource.isUserLoggedIn()

    // NOVO: Estado de rolagem para a tela
    val scrollState = rememberScrollState()

    Scaffold(
        topBar = {
            val navBackStackEntry by navController.currentBackStackEntryAsState()
            val currentRoute = navBackStackEntry?.destination?.route
            TopAppBarWithNavigationMenu(
                title = "Início",
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
                .verticalScroll(scrollState) // APLICADO AQUI: Torna a coluna rolagem
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
                !isUserLoggedIn -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = "Você precisa estar logado para ver o conteúdo.",
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
                else -> {
                    // Campo de Busca (OutlinedTextField)
                    OutlinedTextField(
                        value = searchText,
                        onValueChange = { newText ->
                            homeViewModel.onSearchTextChanged(newText)
                            if (newText.isBlank()) {
                                homeViewModel.clearSearchResults()
                            }
                        },
                        label = { Text("Buscar filmes ou séries...", color = Color.White) },
                        leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Buscar") },
                        trailingIcon = {
                            if (searchText.isNotBlank()) {
                                IconButton(onClick = {
                                    homeViewModel.clearSearchResults()
                                }) {
                                    Icon(Icons.Default.Clear, contentDescription = "Limpar busca")
                                }
                            }
                        },
                        singleLine = true,
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 8.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = Color.White, unfocusedTextColor = Color.White,
                            focusedBorderColor = Color.White, unfocusedBorderColor = Color.LightGray,
                            cursorColor = Color.White
                        )
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // Botão de Busca
                    Button(
                        onClick = { homeViewModel.searchMedia(searchText) },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.LightGray,
                            contentColor = Color.Black
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp)
                    ) {
                        Text("Buscar")
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    if (searchText.isNotBlank() && uiState.searchResults.isNotEmpty()) {
                        Text(
                            text = "Resultados para \"$searchText\"",
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                            color = Color.White
                        )
                        // LazyColumn já é rolagem por si só, mas a coluna pai precisa ser rolagem se houver outros elementos
                        LazyColumn(
                            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                            verticalArrangement = Arrangement.spacedBy(16.dp),
                            modifier = Modifier.heightIn(max = 400.dp)
                        ) {
                            items(uiState.searchResults) { item ->
                                MovieCard(
                                    item = item,
                                    onItemAdded = {
                                        itemActionViewModel.addItemToMyList(it)
                                    },
                                    isAddedToMyList = { mediaItem ->
                                        var isInList by remember { mutableStateOf(false) }
                                        LaunchedEffect(mediaItem.id, authDataSource.getCurrentUserId()) {
                                            isInList = itemActionViewModel.isItemInMyList(mediaItem.id)
                                        }
                                        isInList
                                    }
                                )
                            }
                        }
                    } else if (searchTriggered && searchText.isNotBlank() && uiState.searchResults.isEmpty() && !uiState.isLoading) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "Nenhum resultado encontrado para \"$searchText\".",
                                color = Color.Gray,
                                style = MaterialTheme.typography.bodyLarge
                            )
                        }
                    } else {
                        Column(modifier = Modifier.fillMaxSize()) {
                            Text(
                                text = "Recomendados para Você",
                                style = MaterialTheme.typography.headlineMedium,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 16.dp),
                                color = Color.White
                            )

                            Spacer(modifier = Modifier.height(16.dp))

                            if (uiState.recommendedItems.isNotEmpty()) {
                                LazyRow(
                                    contentPadding = PaddingValues(horizontal = 16.dp),
                                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                                ) {
                                    items(uiState.recommendedItems) { item ->
                                        MovieCard(
                                            item = item,
                                            onItemAdded = {
                                                itemActionViewModel.addItemToMyList(it)
                                            },
                                            isAddedToMyList = { mediaItem ->
                                                var isInList by remember { mutableStateOf(false) }
                                                LaunchedEffect(mediaItem.id, authDataSource.getCurrentUserId()) {
                                                    isInList = itemActionViewModel.isItemInMyList(mediaItem.id)
                                                }
                                                isInList
                                            }
                                        )
                                    }
                                }
                            } else {
                                Text(
                                    text = "Nenhum item recomendado encontrado.",
                                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                                    color = Color.Gray,
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            }


                            Spacer(modifier = Modifier.height(24.dp))

                            Text(
                                text = "Novidades",
                                style = MaterialTheme.typography.headlineMedium,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 16.dp),
                                color = Color.White
                            )

                            Spacer(modifier = Modifier.height(16.dp))

                            if (uiState.newArrivalsItems.isNotEmpty()) {
                                LazyRow(
                                    contentPadding = PaddingValues(horizontal = 16.dp),
                                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                                ) {
                                    items(uiState.newArrivalsItems) { item ->
                                        MovieCard(
                                            item = item,
                                            onItemAdded = {
                                                itemActionViewModel.addItemToMyList(it)
                                            },
                                            isAddedToMyList = { mediaItem ->
                                                var isInList by remember { mutableStateOf(false) }
                                                LaunchedEffect(mediaItem.id, authDataSource.getCurrentUserId()) {
                                                    isInList = itemActionViewModel.isItemInMyList(mediaItem.id)
                                                }
                                                isInList
                                            }
                                        )
                                    }
                                }
                            } else {
                                Text(
                                    text = "Nenhuma novidade encontrada.",
                                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                                    color = Color.Gray,
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    GuiaPLayTheme {
        HomeScreen(navController = rememberNavController())
    }
}