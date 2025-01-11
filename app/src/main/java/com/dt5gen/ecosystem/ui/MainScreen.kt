package com.dt5gen.ecosystem.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.dt5gen.ecosystem.presentation.bin.BinViewModel

@Composable
fun MainScreen(
    viewModel: BinViewModel,
    onNavigateToHistory: () -> Unit
) {
    val binInfoState by viewModel.binInfo.collectAsState()
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
                elevation = CardDefaults.cardElevation(4.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Phone: ${binInfo.bank?.phone ?: "N/A"}")
                    Text("Страна: ${binInfo.country?.name ?: "N/A"}")
                    Text("Банк: ${binInfo.bank?.name ?: "N/A"}")
                }
            }
        }
    }
}

