package com.dt5gen.ecosystem.ui

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.dt5gen.ecosystem.domain.models.BinInfoResponse
import com.dt5gen.ecosystem.presentation.bin.BinViewModel

@Composable
fun MainScreen(
    viewModel: BinViewModel,
    onNavigateToHistory: () -> Unit
) {
    val binInfoState by viewModel.binInfo.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()
    // val binInput = remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Поле ввода
        var binInput by rememberSaveable { mutableStateOf("") }
        OutlinedTextField(
            value = binInput,
            onValueChange = { input ->
                if (input.length <= 16) { // Ограничиваем количество символов
                    binInput = input
                }
            },
            label = { Text("Введите BIN (6-16 цифр)") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))
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

        Spacer(modifier = Modifier.height(8.dp))
        Button(
            onClick = onNavigateToHistory,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("История")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Отображение информации
        binInfoState?.let { binInfo ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                elevation = CardDefaults.cardElevation(8.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Тип карты: ${binInfo.type ?: "N/A"}")
                    Text("Платёжная система: ${binInfo.brand ?: "N/A"}")
                    Text("Страна: ${binInfo.country?.name ?: "N/A"}")
                    Text("Банк: ${binInfo.bank?.name?: "N/A"}")
                    Text("Город: ${binInfo.bank?.city ?: "N/A"}")
                    Text("Телефон: ${binInfo.bank?.phone ?: "N/A"}")
                    Text("сайт: ${binInfo.bank?.phone ?: "N/A"}")
                }
            }
        }

        // Карточка с результатами
        ResultCard(info = binInfoState, errorMessage = errorMessage)
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

