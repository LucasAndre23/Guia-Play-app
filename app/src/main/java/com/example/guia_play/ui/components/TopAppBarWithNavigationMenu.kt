package com.example.guia_play.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.guia_play.data.datasources.FirebaseAuthDataSource
import com.example.guia_play.data.repository.AuthRepository
import com.example.guia_play.ui.viewmodel.AuthViewModel
import com.example.guia_play.ui.viewmodel.AuthViewModelFactory
import androidx.lifecycle.viewmodel.compose.viewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopAppBarWithNavigationMenu(
    title: String,
    navController: NavController,
    currentRoute: String?
) {
    var showMenu by remember { mutableStateOf(false) }
    val authViewModel: AuthViewModel = viewModel(
        factory = AuthViewModelFactory(AuthRepository(FirebaseAuthDataSource()))
    )
    val authDataSource = remember { FirebaseAuthDataSource() } // Para verificar o estado de login
    val isUserLoggedIn = authDataSource.isUserLoggedIn()

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
                // Apenas exibe se o usuário estiver logado.
                if (isUserLoggedIn) {
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
                            authViewModel.logout() // Chama a função de logout no ViewModel
                            navController.navigate("login") { // Redireciona para a tela de login
                                popUpTo(navController.graph.startDestinationId) { inclusive = true }
                            }
                            showMenu = false
                        }
                    )
                } else {
                    // Opções para usuário não logado (login ou cadastro)
                    // Só mostra a opção "Login" se não estiver já na tela de login/register
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