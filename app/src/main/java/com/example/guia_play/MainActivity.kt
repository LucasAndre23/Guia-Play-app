package com.example.guia_play

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.guia_play.ui.theme.GuiaPLayTheme
import androidx.compose.foundation.Image
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.filled.Menu
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow


data class MovieItem(
    val imageRes: Int,
    val title: String,
    val subtitle: String
)
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            GuiaPLayTheme {
                AppNavigator() // Inicializa o navegador do nosso app
            }
        }
    }
}

/**
 * Componente principal que gerencia a navegação do aplicativo.
 */
@Composable
fun AppNavigator() {
    val navController = rememberNavController()

    Scaffold(
        bottomBar = {
            // Mostra a barra de navegação inferior apenas se não estivermos na tela de login
            val navBackStackEntry by navController.currentBackStackEntryAsState()
            val currentRoute = navBackStackEntry?.destination?.route
            if (currentRoute != "login") {
                AppBottomNavigationBar(navController = navController)
            }
        }
    ) { innerPadding ->
        // NavHost é o container que exibirá a tela atual
        NavHost(
            navController = navController,
            startDestination = "login", // A primeira tela a ser mostrada
            modifier = Modifier.padding(innerPadding)
        ) {

            composable("login") {
                LoginScreen(navController = navController)
            }
            composable("home") {
                HomeScreen(navController = navController)
            }
            composable("myList") {
                MyListScreen(navController = navController)
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopAppBarWithNavigationMenu(
    title: String,
    navController: NavController,
    currentRoute: String? // Para saber qual opção marcar como selecionada no menu
) {
    var showMenu by remember { mutableStateOf(false) }

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
                // Opção para Home
                DropdownMenuItem(
                    text = { Text("Início") },
                    onClick = {
                        navController.navigate("home") {
                            popUpTo(navController.graph.startDestinationId) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                        showMenu = false // Fechar o menu após a seleção
                    }
                )
                // Opção para Minha Lista
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
                // Opção para login
                if (currentRoute != "login") { // Só mostra a opção de login se não estivermos nela
                    DropdownMenuItem(
                        text = { Text("Login") },
                        onClick = {
                            navController.navigate("login") {
                                popUpTo(navController.graph.startDestinationId) { inclusive = true } // Limpa a pilha
                            }
                            showMenu = false
                        }
                    )
                }
            }
        }
    )
}


// --- Telas do Aplicativo ---

/**
 * Tela de Login/Criação de Conta.
 * @param navController Controlador para navegar para a próxima tela.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(navController: NavController) {
    var emailText by remember { mutableStateOf("") }
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    Scaffold(
        topBar = {
            TopAppBarWithNavigationMenu(
                title = "Guia Play",
                navController = navController,
                currentRoute = currentRoute
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Guia Play",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                fontSize = 40.sp
            )
            Spacer(modifier = Modifier.height(24.dp))
            Text(
                text = "Crie uma conta",
                style = MaterialTheme.typography.headlineSmall
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Insira seu e-mail para se cadastrar no app",
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(24.dp))

            OutlinedTextField(
                value = emailText,
                onValueChange = { newText -> emailText = newText },
                label = { Text("Email") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                singleLine = true,
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {

                    navController.navigate("home") {
                        popUpTo("login") { inclusive = true }
                    }
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Black,
                    contentColor = Color.White
                ),
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Continuar", modifier = Modifier.padding(8.dp))
            }
        }
    }
}

/**
 * Tela Inicial do aplicativo com um carrossel de filmes.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavController) {
    val movies = remember {
        listOf(
            MovieItem(R.drawable.dexter, "Título do Filme Um", "Ação / Aventura"),
            MovieItem(R.drawable.got, "Filme Espetacular Dois", "Ficção Científica"),
            MovieItem(R.drawable.tlou, "A Grande Jornada Três", "Drama / Suspense"),
            MovieItem(R.drawable.you, "Comédia Quatro", "Comédia / Família"),
            MovieItem(R.drawable.vik, "Mistério na Mansão Cinco", "Mistério")
        )
    }
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    Scaffold(
        topBar = {
            TopAppBarWithNavigationMenu(
                title = "Início",
                navController = navController,
                currentRoute = currentRoute
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(vertical = 16.dp)
        ) {
            // Primeiro carrosel
            Text(
                text = "Recomendados para Você",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            LazyRow(
                contentPadding = PaddingValues(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp) // Espaçamento entre os itens
            ) {
                items(movies) { movie ->
                    MovieCard(movie = movie)
                }
            }

            // Espaço entre carroseis
            Spacer(modifier = Modifier.height(24.dp)) // Um pouco mais de espaço para separar os carrosseis

            // --- SEGUNDO CARROSSEL ---
            Text(
                text = "Novidades",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            LazyRow(
                contentPadding = PaddingValues(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(movies) { movie ->
                    MovieCard(movie = movie)
                }
            }


        }
    }
}

/**
 * Representa um único card no carrossel.
 * @param movie O item de dados contendo a imagem e os textos.
 */
@Composable
fun MovieCard(movie: MovieItem) {
    Card(
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        modifier = Modifier
            .width(150.dp) // Largura fixa para cada card
    ) {
        Column {
            Image(
                painter = painterResource(id = movie.imageRes),
                contentDescription = movie.title, // Texto alternativo para acessibilidade
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                contentScale = ContentScale.Crop
            )

            Column(
                modifier = Modifier.padding(12.dp)
            ) {
                Text(
                    text = movie.title,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1, //  para que  o título não quebre em várias linhas
                    overflow = TextOverflow.Ellipsis // Adiciona ... se o texto for muito longo
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = movie.subtitle,
                    style = MaterialTheme.typography.bodySmall,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

/**
 * Tela "Minha Lista" do aplicativo.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyListScreen(navController: NavController) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    Scaffold(
        topBar = {
            TopAppBarWithNavigationMenu(
                title = "Minha Lista",
                navController = navController,
                currentRoute = currentRoute
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Minha Lista",
                style = MaterialTheme.typography.headlineMedium
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Aqui ficarão os filmes e séries que você salvar.",
                textAlign = TextAlign.Center
            )
        }
    }
}


// --- Componentes de Navegação ---

/**
 * Barra de Navegação Inferior.
 * @param navController Controlador para alternar entre as telas.
 */
@Composable
fun AppBottomNavigationBar(navController: NavHostController) {
    NavigationBar {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route

        // Item para a Tela Inicial
        NavigationBarItem(
            icon = { Icon(Icons.Filled.Home, contentDescription = "Início") },
            label = { Text("Início") },
            selected = currentRoute == "home",
            onClick = {
                navController.navigate("home") {
                    // Evita empilhar a mesma tela várias vezes
                    launchSingleTop = true
                    restoreState = true // Restaura o estado da tela se ela já existia
                }
            }
        )

        // Item para a Tela "Minha Lista"
        NavigationBarItem(
            icon = { Icon(Icons.Filled.List, contentDescription = "Minha Lista") },
            label = { Text("Minha Lista") },
            selected = currentRoute == "myList",
            onClick = {
                navController.navigate("myList") {
                    // Evita empilhar a mesma tela várias vezes
                    launchSingleTop = true
                    restoreState = true // Restaura o estado da tela se ela já existia
                }
            }
        )
    }
}

//Previews

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun LoginScreenPreview() {
    GuiaPLayTheme {

        LoginScreen(navController = rememberNavController())
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    GuiaPLayTheme {
        HomeScreen(navController = rememberNavController())
    }
}

@Preview(showBackground = true)
@Composable
fun MyListScreenPreview() {
    GuiaPLayTheme {
        MyListScreen(navController = rememberNavController())
    }
}