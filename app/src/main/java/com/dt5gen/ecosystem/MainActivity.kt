package com.dt5gen.ecosystem

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
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
                var showHistory by remember { mutableStateOf(false) }

                if (showHistory) {
                    HistoryScreen(
                        viewModel = hiltViewModel(),
                        onBackClick = { showHistory = false }
                    )
                } else {
                    BinScreen(
                        viewModel = hiltViewModel(),
                        onHistoryClick = { showHistory = true }
                    )
                }
            }
        }
    }
}

@Composable
fun BinScreen(
    modifier: Modifier = Modifier,
    viewModel: BinViewModel,
    onHistoryClick: () -> Unit
) {
    val binInfo by viewModel.binInfo.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(36.dp)
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

        // Кнопка перехода в историю
        Button(
            onClick = onHistoryClick,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp)
        ) {
            Text("История")
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
    val context = LocalContext.current // Получаем контекст внутри @Composable функции

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 36.dp),
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
                Text("Страна: ${info.country?.name ?: "N/A"}")
                info.country?.latitude?.let { lat ->
                    info.country.longitude?.let { lng ->
                        Text(
                            text = "Координаты: $lat, $lng",
                            color = Color.Blue,
                            modifier = Modifier.clickable {
                                val uri = Uri.parse("geo:$lat,$lng")
                                val intent = Intent(Intent.ACTION_VIEW, uri).apply {
                                    setPackage("com.google.android.apps.maps")
                                }
                                context.startActivity(intent)
                            }
                        )
                    }
                }
                Text("Банк: ${info.bank?.name ?: "N/A"}")
                info.bank?.url?.let { url ->
                    Text(
                        text = "URL: $url",
                        color = Color.Blue,
                        modifier = Modifier.clickable {
                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                            context.startActivity(intent)
                        }
                    )
                }
                info.bank?.phone?.let { phone ->
                    Text(
                        text = "Телефон: $phone",
                        color = Color.Blue,
                        modifier = Modifier.clickable {
                            val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:$phone"))
                            context.startActivity(intent)
                        }
                    )
                }
            } else {
                Text(
                    text = "Введите данные и нажмите Поиск",
                    color = Color.Gray
                )
            }
        }
    }
}


@Composable
fun HistoryScreen(viewModel: BinViewModel, onBackClick: () -> Unit) {
    val history by viewModel.history.collectAsState()

    Column(modifier = Modifier
        .fillMaxSize()
        .padding(36.dp)) {
        Button(onClick = onBackClick, modifier = Modifier.fillMaxWidth()) {
            Text("Назад")
        }
        Text("История запросов", style = MaterialTheme.typography.titleLarge)
        history.forEach { item ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                shape = RoundedCornerShape(8.dp)
            ) {
                Column(modifier = Modifier.padding(8.dp)) {
                    Text("BIN: ${item.bin}")
                    Text("Страна: ${item.countryName ?: "N/A"}")
                    Text("Банк: ${item.bankName ?: "N/A"}")
                }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun BinScreenPreview() {
    EcoSystemTheme {
        BinScreen(viewModel = hiltViewModel(),
            onHistoryClick = {})
    }
}
