package com.example.guia_play.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
import com.example.guia_play.ui.components.MovieCard
import com.example.guia_play.ui.components.TopAppBarWithNavigationMenu
import com.example.guia_play.ui.theme.GuiaPLayTheme
import com.example.guia_play.ui.viewmodel.HomeViewModel
import com.example.guia_play.ui.viewmodel.ItemActionViewModel
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavController) {
    // Usando koinViewModel()
    val homeViewModel: HomeViewModel = koinViewModel()
    // Usando koinViewModel()
    val itemActionViewModel: ItemActionViewModel = koinViewModel()

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
                .verticalScroll(rememberScrollState())
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
                // Nota: O ViewModel não deve saber se o usuário está logado. Essa lógica
                // foi movida para a MainActivity. Se você quer esconder a tela,
                // você deve usar a navegação.
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
                                        LaunchedEffect(mediaItem.id) {
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
                                                LaunchedEffect(mediaItem.id) {
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
                                                LaunchedEffect(mediaItem.id) {
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
