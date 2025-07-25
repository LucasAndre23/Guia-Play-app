package com.example.guia_play.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState

@Composable
fun AppBottomNavigationBar(navController: NavController) {
    val navBackStackEntry = navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry.value?.destination?.route

    NavigationBar(
        containerColor = Color.Black, // Cor de fundo da barra
        contentColor = Color.White // Cor do conteúdo (ícones, texto)
    ) {
        // Item de navegação para "Início"
        NavigationBarItem(
            selected = currentRoute == "home",
            onClick = {
                navController.navigate("home") {
                    // Evita múltiplas cópias da mesma tela na pilha
                    popUpTo(navController.graph.startDestinationId) {
                        saveState = true
                    }
                    // Evita recriar a tela se já estiver no topo
                    launchSingleTop = true
                    // Restaura o estado quando volta para a tela
                    restoreState = true
                }
            },
            icon = {
                Icon(
                    imageVector = Icons.Default.Home,
                    contentDescription = "Início",
                    tint = if (currentRoute == "home") Color.White else Color.Gray // Cor do ícone selecionado/não selecionado
                )
            },
            label = {
                Text(
                    text = "Início",
                    color = if (currentRoute == "home") Color.White else Color.Gray // Cor do texto selecionado/não selecionado
                )
            }
        )
    }
}