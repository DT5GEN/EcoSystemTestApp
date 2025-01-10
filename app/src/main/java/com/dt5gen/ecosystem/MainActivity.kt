package com.dt5gen.ecosystem


import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.dt5gen.ecosystem.presentation.bin.BinViewModel
import com.dt5gen.ecosystem.ui.theme.EcoSystemTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            EcoSystemTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    BinScreen(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}


@Composable
fun BinScreen(
    viewModel: BinViewModel = hiltViewModel(),
    modifier: Modifier = Modifier
) {
    val binInfo = viewModel.binInfo.collectAsState().value
    val errorMessage = viewModel.errorMessage.collectAsState().value

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        var binInput by remember { mutableStateOf("") }
        val maxLength = 16

        // Поле ввода BIN
        TextField(
            value = binInput,
            onValueChange = { input ->
                if (input.length <= maxLength && input.all { it.isDigit() }) {
                    binInput = input
                }
            },
            label = { Text("Введите BIN (6-16 цифр)") },
            modifier = Modifier.fillMaxWidth()
        )

        // Кнопка поиска
        Button(
            onClick = {
                if (binInput.length >= 6) {
                    viewModel.fetchBinInfo(binInput.take(6)) // Берём только первые 6 цифр
                } else {
                    viewModel.setError("Введите минимум 6 цифр!")
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            enabled = binInput.length >= 6 // Включаем кнопку только при достаточной длине ввода
        ) {
            Text("Поиск")
        }

        // Отображение результата
        binInfo?.let { info ->
            Text(text = "Страна: ${info.country?.name ?: "N/A"}")
            Text(text = "Банк: ${info.bank?.name ?: "N/A"}")
            Text(text = "Телефон: ${info.bank?.phone ?: "N/A"}")
        }

        // Отображение ошибки
        errorMessage?.let { error ->
            Text(text = "Ошибка: $error", color = Color.Red)
        }
    }
}


@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    EcoSystemTheme {
        // Показываем пример BinScreen
        BinScreen()
    }
}
