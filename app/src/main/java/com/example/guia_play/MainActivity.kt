package com.example.guia_play

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.guia_play.ui.components.AppBottomNavigationBar
import com.example.guia_play.ui.screens.HomeScreen
import com.example.guia_play.ui.screens.LoginScreen
import com.example.guia_play.ui.screens.MyListScreen
import com.example.guia_play.ui.screens.RegisterScreen
import com.example.guia_play.ui.theme.GuiaPLayTheme

import com.google.firebase.auth.FirebaseAuth

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            GuiaPLayTheme {
                AppNavigator()
            }
        }
    }
}

@Composable
fun AppNavigator() {
    val navController = rememberNavController()

    val startDestination = if (FirebaseAuth.getInstance().currentUser != null) {
        "home"
    } else {
        "login"
    }

    Scaffold(
        bottomBar = {
            val navBackStackEntry by navController.currentBackStackEntryAsState()
            val currentRoute = navBackStackEntry?.destination?.route
            // A barra inferior só aparece nas telas principais onde ela faz sentido
            if (currentRoute == "home" || currentRoute == "myList") {
                AppBottomNavigationBar(navController = navController)
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = startDestination,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable("login") {
                LoginScreen(navController = navController)
            }
            composable("register") {
                RegisterScreen(navController = navController)
            }
            composable("home") {
                HomeScreen(navController = navController)
            }
            composable("myList") {
                MyListScreen(navController = navController)
            }
            composable("search") {
                // A tela de busca ainda existe, mas não é acessível pela barra inferior
                Text("Tela de Busca", modifier = Modifier.fillMaxSize().wrapContentSize(Alignment.Center))
            }
        }
    }
}