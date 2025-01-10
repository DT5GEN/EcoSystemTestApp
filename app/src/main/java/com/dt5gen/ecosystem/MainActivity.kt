package com.dt5gen.ecosystem

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
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

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            EcoSystemTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    BinScreen(
                        modifier = Modifier.padding(innerPadding),
                        viewModel = hiltViewModel()
                    )
                }
            }
        }
    }
}

@Composable
fun BinScreen(modifier: Modifier = Modifier, viewModel: BinViewModel) {
    val binInfo by viewModel.binInfo.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Поле ввода
        var binInput by remember { mutableStateOf("") }
        OutlinedTextField(
            value = binInput,
            onValueChange = { binInput = it },
            label = { Text("Введите BIN (6-16 цифр)") },
            modifier = Modifier.fillMaxWidth()
        )

        // Кнопка поиска
        Button(
            onClick = {
                if (binInput.length in 6..16) {
                    viewModel.fetchBinInfo(binInput.take(6))
                } else {
                    viewModel.setError("Введите от 6 до 16 цифр!")
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            enabled = binInput.length in 6..16
        ) {
            Text("Поиск")
        }

        // Карточка с результатами
        ResultCard(info = binInfo, errorMessage = errorMessage)
    }
}

@Composable
fun ResultCard(
    info: BinInfoResponse?,
    errorMessage: String?
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp),
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            if (!errorMessage.isNullOrEmpty()) {
                Text(
                    text = errorMessage,
                    color = Color.Red,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
            } else if (info != null) {
                Text(
                    text = "Страна: ${info.country?.name ?: "N/A"}",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(bottom = 4.dp)
                )
                Text(
                    text = "Банк: ${info.bank?.name ?: "N/A"}",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(bottom = 4.dp)
                )
                Text(
                    text = "Телефон: ${info.bank?.phone ?: "N/A"}",
                    style = MaterialTheme.typography.bodyMedium
                )
            } else {
                Text(
                    text = "Введите данные и нажмите Поиск",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun BinScreenPreview() {
    EcoSystemTheme {
        BinScreen(viewModel = hiltViewModel())
    }
}
