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
import com.dt5gen.ecosystem.domain.models.BinInfoResponse
import com.dt5gen.ecosystem.presentation.bin.BinViewModel
import com.dt5gen.ecosystem.ui.theme.EcoSystemTheme
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.lifecycle.HiltViewModel

@AndroidEntryPoint

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            EcoSystemTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    // Заменяем Greeting на BinScreen
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
    modifier: Modifier = Modifier,
    viewModel: BinViewModel = hiltViewModel()
) {
    val binInfo by viewModel.binInfo.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        var binInput by remember { mutableStateOf("") }

        TextField(
            value = binInput,
            onValueChange = { binInput = it },
            label = { Text("Введите BIN") },
            modifier = Modifier.fillMaxWidth()
        )

        Button(
            onClick = { viewModel.fetchBinInfo(binInput) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp)
        ) {
            Text("Поиск")
        }

        binInfo?.let { info: BinInfoResponse ->
            Text(text = "Страна: ${info.country?.name}")
            Text(text = "Банк: ${info.bank?.name}")
            Text(text = "Телефон: ${info.bank?.phone}")
        }

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
