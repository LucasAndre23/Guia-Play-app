package com.example.guia_play.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.guia_play.ui.viewmodel.AuthViewModel
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopAppBarWithNavigationMenu(
    title: String,
    navController: NavController,
    currentRoute: String?
) {
    var showMenu by remember { mutableStateOf(false) }

    // Usando koinViewModel() para injetar o AuthViewModel
    val authViewModel: AuthViewModel = koinViewModel()
    val uiState by authViewModel.uiState.collectAsState()

    // Adicione este bloco LaunchedEffect
    LaunchedEffect(Unit) {
        authViewModel.checkAuthStatus()
    }

    TopAppBar(
        title = { Text(title) },
        actions = {
            IconButton(onClick = { showMenu = !showMenu }) {
                Icon(Icons.Filled.Menu, contentDescription = "Menu de Navegação")
            }
            DropdownMenu(
                expanded = showMenu,
                onDismissRequest = { showMenu = false }
            ) {
                // Opções de navegação para telas principais (Início, Minha Lista)
                if (uiState.isLoggedIn) {
                    DropdownMenuItem(
                        text = { Text("Início") },
                        onClick = {
                            navController.navigate("home") {
                                popUpTo(navController.graph.startDestinationId) { saveState = true }
                                launchSingleTop = true
                                restoreState = true
                            }
                            showMenu = false
                        }
                    )
                    DropdownMenuItem(
                        text = { Text("Minha Lista") },
                        onClick = {
                            navController.navigate("myList") {
                                popUpTo(navController.graph.startDestinationId) { saveState = true }
                                launchSingleTop = true
                                restoreState = true
                            }
                            showMenu = false
                        }
                    )

                    // Opção de Sair (Logout) aparece apenas se o usuário estiver logado
                    DropdownMenuItem(
                        text = { Text("Sair") },
                        onClick = {
                            authViewModel.logout()
                            navController.navigate("login") {
                                popUpTo(navController.graph.startDestinationId) { inclusive = true }
                            }
                            showMenu = false
                        }
                    )
                } else {
                    // Opções para usuário não logado
                    if (currentRoute != "login" && currentRoute != "register") {
                        DropdownMenuItem(
                            text = { Text("Login") },
                            onClick = {
                                navController.navigate("login") {
                                    popUpTo(navController.graph.startDestinationId) { inclusive = true }
                                }
                                showMenu = false
                            }
                        )
                    }
                }
            }
        }
    )
}