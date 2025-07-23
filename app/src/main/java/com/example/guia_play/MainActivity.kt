package com.example.guia_play

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow

import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.firestore.FieldPath
import coil.compose.AsyncImage
import kotlinx.coroutines.launch
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers

data class MyListItem(
    val id: String = "",
    val imageUrl: String = "",
    val seasons: String = "",
    val title: String = "",
    val genero: String = ""
)

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

/**
 * Componente principal que gerencia a navegação do aplicativo.
 */
@Composable
fun AppNavigator() {
    val navController = rememberNavController()

    Scaffold(
        bottomBar = {
            val navBackStackEntry by navController.currentBackStackEntryAsState()
            val currentRoute = navBackStackEntry?.destination?.route
            // A barra inferior é exibida apenas nas telas 'home' e 'search'
            if (currentRoute == "home" || currentRoute == "search") {
                AppBottomNavigationBar(navController = navController)
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = "login",
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
            composable("search") {
                Text("Tela de Busca", modifier = Modifier.fillMaxSize().wrapContentSize(Alignment.Center))
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopAppBarWithNavigationMenu(
    title: String,
    navController: NavController,
    currentRoute: String?
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
                // O item de Login só aparece se a rota atual NÃO for "login"
                if (currentRoute != "login") {
                    DropdownMenuItem(
                        text = { Text("Login") },
                        onClick = {
                            navController.navigate("login") {
                                // Limpa o back stack para que o usuário não volte para as telas autenticadas
                                popUpTo(navController.graph.startDestinationId) { inclusive = true }
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
                        // Ao navegar para 'home' após o login, o 'login' é removido do back stack
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavController) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val recommendedItems = remember { mutableStateListOf<MyListItem>() }
    val newArrivalsItems = remember { mutableStateListOf<MyListItem>() }
    var isLoading by remember { mutableStateOf(true) }
    var error by remember { mutableStateOf<String?>(null) }

    val db = Firebase.firestore
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        db.collection("mediaItems")
            .get()
            .addOnSuccessListener { result ->
                val fetchedItems = mutableListOf<MyListItem>()
                for (document in result) {
                    val imageUrl = document.getString("urlDaImagem")
                    val title = document.getString("titulo")
                    val genero = document.getString("genero")
                    val seasons = document.getString("numeroDeTemporadas")

                    val item = MyListItem(
                        id = document.id,
                        imageUrl = imageUrl ?: "",
                        seasons = seasons ?: "",
                        title = title ?: "",
                        genero = genero ?: ""
                    )
                    fetchedItems.add(item)
                }

                recommendedItems.addAll(fetchedItems.shuffled())
                newArrivalsItems.addAll(fetchedItems.shuffled())

                isLoading = false
            }
            .addOnFailureListener { exception ->
                error = "Erro ao carregar dados: ${exception.message}"
                isLoading = false
            }
    }

    Scaffold(
        topBar = {
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
                .padding(vertical = 16.dp)
        ) {
            if (isLoading) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            } else if (error != null) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(text = error ?: "Erro desconhecido", color = Color.Red)
                }
            } else {
                Text(
                    text = "Recomendados para Você",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )

                Spacer(modifier = Modifier.height(16.dp))

                LazyRow(
                    contentPadding = PaddingValues(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(recommendedItems) { item ->
                        MovieCard(item = item, snackbarHostState = snackbarHostState)
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

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
                    items(newArrivalsItems) { item ->
                        MovieCard(item = item, snackbarHostState = snackbarHostState)
                    }
                }
            }
        }
    }
}

@Composable
fun MovieCard(item: MyListItem, snackbarHostState: SnackbarHostState) {
    val db = Firebase.firestore
    Card(
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        modifier = Modifier
            .width(150.dp)
    ) {
        Column {
            AsyncImage(
                model = item.imageUrl,
                contentDescription = item.title,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                contentScale = ContentScale.Crop
            )

            Column(
                modifier = Modifier.padding(12.dp)
            ) {
                Text(
                    text = item.title,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = item.genero,
                    style = MaterialTheme.typography.bodySmall,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(8.dp))
                Button(
                    onClick = {
                        db.collection("myList")
                            .document(item.id)
                            .get()
                            .addOnSuccessListener { documentSnapshot ->
                                if (!documentSnapshot.exists()) {
                                    db.collection("myList")
                                        .document(item.id)
                                        .set(mapOf(
                                            "mediaItemId" to item.id,
                                        ))
                                        .addOnSuccessListener {
                                            CoroutineScope(Dispatchers.Main).launch {
                                                snackbarHostState.showSnackbar(
                                                    message = "Adicionado à Minha Lista!",
                                                    withDismissAction = true
                                                )
                                            }
                                        }
                                        .addOnFailureListener { e ->
                                            CoroutineScope(Dispatchers.Main).launch {
                                                snackbarHostState.showSnackbar(
                                                    message = "Erro ao adicionar: ${e.localizedMessage}",
                                                    withDismissAction = true
                                                )
                                            }
                                        }
                                } else {
                                    CoroutineScope(Dispatchers.Main).launch {
                                        snackbarHostState.showSnackbar(
                                            message = "Já está na Minha Lista!",
                                            withDismissAction = true
                                        )
                                    }
                                }
                            }
                            .addOnFailureListener { e ->
                                CoroutineScope(Dispatchers.Main).launch {
                                    snackbarHostState.showSnackbar(
                                        message = "Erro ao verificar lista: ${e.localizedMessage}",
                                        withDismissAction = true
                                    )
                                }
                            }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp),
                    contentPadding = PaddingValues(vertical = 4.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.LightGray,
                        contentColor = Color.Black
                    )
                ) {
                    Text("Adicionar", fontSize = 12.sp)
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyListScreen(navController: NavController) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val myItems = remember { mutableStateListOf<MyListItem>() }
    var isLoading by remember { mutableStateOf(true) }
    var error by remember { mutableStateOf<String?>(null) }

    val db = Firebase.firestore

    LaunchedEffect(Unit) {
        db.collection("myList")
            .get()
            .addOnSuccessListener { myListResult ->
                val savedMediaIds = myListResult.map { it.getString("mediaItemId") }.filterNotNull()

                if (savedMediaIds.isNotEmpty()) {
                    db.collection("mediaItems")
                        .whereIn(FieldPath.documentId(), savedMediaIds)
                        .get()
                        .addOnSuccessListener { mediaItemsResult ->
                            myItems.clear()
                            for (document in mediaItemsResult) {
                                val imageUrl = document.getString("urlDaImagem")
                                val title = document.getString("titulo")
                                val genero = document.getString("genero")
                                val seasons = document.getString("numeroDeTemporadas")

                                val item = MyListItem(
                                    id = document.id,
                                    imageUrl = imageUrl ?: "",
                                    seasons = seasons ?: "",
                                    title = title ?: "",
                                    genero = genero ?: ""
                                )
                                myItems.add(item)
                            }
                            isLoading = false
                        }
                        .addOnFailureListener { exception ->
                            error = "Erro ao carregar detalhes dos itens da Minha Lista: ${exception.message}"
                            isLoading = false
                        }
                } else {
                    myItems.clear()
                    isLoading = false
                }
            }
            .addOnFailureListener { exception ->
                error = "Erro ao carregar IDs da Minha Lista: ${exception.message}"
                isLoading = false
            }
    }

    Scaffold(
        topBar = {
            TopAppBarWithNavigationMenu(
                title = "Minha lista",
                navController = navController,
                currentRoute = currentRoute
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp)
        ) {
            if (isLoading) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            } else if (error != null) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(text = error ?: "Erro desconhecido", color = Color.Red)
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxWidth(),
                    contentPadding = PaddingValues(vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(myItems) { item ->
                        MyListItemCard(item = item)
                    }

                    item {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 16.dp),
                            horizontalArrangement = Arrangement.End,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = "Adicionar",
                                tint = Color.White
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "adicionar",
                                color = Color.White,
                                fontWeight = FontWeight.Normal,
                                fontSize = 16.sp
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun MyListItemCard(item: MyListItem) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AsyncImage(
            model = item.imageUrl,
            contentDescription = item.title,
            modifier = Modifier
                .size(90.dp)
                .clip(RoundedCornerShape(8.dp)),
            contentScale = ContentScale.Crop
        )
        Spacer(modifier = Modifier.width(16.dp))

        Column {
            Text(
                text = item.seasons,
                style = MaterialTheme.typography.bodySmall,
                color = Color.Gray
            )
            Text(
                text = item.title,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.titleMedium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
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

        // Item para a Tela de Busca
        NavigationBarItem(
            icon = { Icon(Icons.Default.Search, contentDescription = "Buscar") },
            label = { Text("Buscar") },
            selected = currentRoute == "search",
            onClick = {
                navController.navigate("search") {
                    launchSingleTop = true
                    restoreState = true
                }
            }
        )

        // Item para a Tela Inicial
        NavigationBarItem(
            icon = { Icon(Icons.Filled.Home, contentDescription = "Início") },
            label = { Text("Início") },
            selected = currentRoute == "home",
            onClick = {
                navController.navigate("home") {
                    launchSingleTop = true
                    restoreState = true
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

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun MyListScreenPreview() {
    GuiaPLayTheme {
        MyListScreen(navController = rememberNavController())
    }
}

@Preview(showBackground = true)
@Composable
fun MyListItemCardPreview() {
    GuiaPLayTheme {
        MyListItemCard(item = MyListItem(imageUrl = "https://res.cloudinary.com/deovjxbec/image/upload/v175324493/vik_wzyeiy6.jpg", seasons = "8 temporadas", title = "Vikings"))
    }
}