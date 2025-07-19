package com.example.guia_play

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.guia_play.ui.theme.GuiaPLayTheme
import java.util.Date.from

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            GuiaPLayTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    GreetingText(
                        message = "Happy Birthday Sam!",
                        modifier = Modifier.padding(8.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun GreetingText(message: String, modifier: Modifier = Modifier) {
    // estado para o texto do e-mail
    var emailText by remember { mutableStateOf("") }

    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = message,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            fontSize = 40.sp,
            lineHeight = 116.sp,
            textAlign = TextAlign.Center,
        )
        Text(
            text = "Crie uma conta",
            fontSize = 20.sp,
            style = MaterialTheme.typography.bodyMedium,
            lineHeight = 20.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        Text(
            text = "Insira seu e-mail para se cadastrar no app",
            fontSize = 20.sp,
            style = MaterialTheme.typography.bodyMedium,
            lineHeight = 20.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Caixa de entrada
        OutlinedTextField(
            value = emailText, // O valor atual do campo de texto
            onValueChange = { newText -> emailText = newText }, // Atualiza o estado quando o texto muda
            label = { Text("Email") }, // Rótulo que aparece no campo
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email), // Sugere teclado de e-mail
            singleLine = true, // Garante que o texto fique em uma única linha
            shape = RoundedCornerShape(percent = 20),
            modifier = Modifier
                .padding(horizontal = 3.dp)
                .fillMaxWidth(),

        )

        Button(
            onClick = {
                // exemplo
                println("Email digitado: $emailText")
                //  lógica de login/registro aqui
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Black, // Define a cor de fundo do botão como preto
                contentColor = Color.White // Define a cor do texto do botão como branco para contraste
            ),
            modifier = Modifier.padding(top = 16.dp)
                .padding(horizontal = 12.dp)
                .fillMaxWidth()
        ) {
            Text("Continuar")
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun BirthdayCardPreview() {
    GuiaPLayTheme {
        GreetingText(message = "Guia Play")
    }
}


