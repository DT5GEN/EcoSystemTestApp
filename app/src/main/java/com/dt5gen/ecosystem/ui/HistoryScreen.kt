package com.dt5gen.ecosystem.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dt5gen.ecosystem.presentation.bin.BinViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@Composable
fun HistoryScreen(
    viewModel: BinViewModel,
    onBackClick: () -> Unit,
    onClearHistory: () -> Unit,
    onNavigateToMain: () -> Unit
) {
    val history by viewModel.history.collectAsState()
    var isMessageVisible by remember { mutableStateOf(false) }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = {
                        onClearHistory()
                        isMessageVisible = true
                        CoroutineScope(Dispatchers.Main).launch {
                            delay(1200)
                            onNavigateToMain() // Переход на главный экран через 1,2 сек
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Очистить историю")
                }
                Button(
                    onClick = onBackClick,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Назад")
                }
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            Text(
                text = "История запросов",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(horizontal = 64.dp, vertical = 8.dp)
            )
            LazyColumn(
                modifier = Modifier.weight(1f),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(history) { item ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(8.dp),
                        elevation = CardDefaults.cardElevation(4.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text("BIN: ${item.bin}")
                            Text("Тип карты: ${item.type ?: "N/A"}")
                            Text("Платёжная система: ${item.brand ?: "N/A"}")
                            Text("Страна: ${item.countryName ?: "N/A"}")
                            Text("Банк: ${item.bankName ?: "N/A"}")
                            Text("Город: ${item.bankCity ?: "N/A"}")
                            Text("Телефон: ${item.bankPhone ?: "N/A"}")
                            Text("Cайт: ${item.bankUrl ?: "N/A"}")
                        }
                    }
                }
            }
            if (isMessageVisible) {
                Column(
                    modifier = Modifier
                        .fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "История запросов очищена",
                        style = MaterialTheme.typography.bodyMedium,
                        fontSize = 24.sp
                    )
                }
            }
        }
    }
}
