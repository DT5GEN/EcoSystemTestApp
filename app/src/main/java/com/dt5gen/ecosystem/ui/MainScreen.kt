package com.dt5gen.ecosystem.ui

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.dt5gen.ecosystem.domain.models.BinInfoResponse
import com.dt5gen.ecosystem.presentation.bin.BinViewModel

@Composable
fun MainScreen(
    viewModel: BinViewModel,
    onNavigateToHistory: () -> Unit
) {
    val binInfoResponse by viewModel.binInfo.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()

    val focusManager = LocalFocusManager.current // Для управления фокусом

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        var binInput by rememberSaveable { mutableStateOf("") }
        OutlinedTextField(
            value = binInput,
            onValueChange = { input ->
                if (input.length <= 16) {
                    binInput = input
                }
            },
            label = { Text("Введите BIN (6-16 цифр)") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = {
                focusManager.clearFocus() // Скрытие клавиатуры
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

        ResultCard(info = binInfoResponse, errorMessage = errorMessage)
    }
}


@Composable
fun ResultCard(
    info: BinInfoResponse?,
    errorMessage: String?
) {
    val context = LocalContext.current

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 36.dp),
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
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
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
            } else if (info != null) {
                info.country?.name?.let { countryName ->
                    Text(

                        modifier = Modifier.padding(bottom = 8.dp),
                        text = "Страна: $countryName",
                        style = MaterialTheme.typography.bodyLarge

                    )
                }

                info.country?.latitude?.let { lat ->
                    info.country.longitude?.let { lng ->
                        Text(
                            text = "Координаты: $lat, $lng",
                            color = Color.Blue,
                            style = MaterialTheme.typography.bodyLarge,

                            modifier = Modifier
                                .padding(bottom = 8.dp)
                                .clickable {
                                    val uri = Uri.parse("geo:$lat,$lng")
                                    val intent = Intent(Intent.ACTION_VIEW, uri).apply {
                                        setPackage("com.google.android.apps.maps")
                                    }
                                    context.startActivity(intent)
                                }
                        )
                    }
                }

                info.bank?.name?.let { bankName ->
                    Text(
                        text = "Банк: $bankName",
                        color = Color.Black,
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                }

                info.bank?.city?.let { bankCity ->
                    Text(
                        text = "Город: $bankCity",
                        color = Color.Black,
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                }

                info.bank?.url?.let { url ->
                    Text(
                        text = "URL: $url",
                        color = Color.Blue,
                        style = MaterialTheme.typography.bodyLarge,
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
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.clickable {
                            val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:$phone"))
                            context.startActivity(intent)
                        }
                    )
                }

                info.brand?.let { brand ->
                    Text(
                        text = "Платёжная система: $brand",
                        color = Color.Black,
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                }

                info.type?.let { type ->
                    Text(
                        text = "Тип карты: $type",
                        color = Color.Black,
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                }
            } else {
                Text(
                    text = "Введите данные и нажмите Поиск",
                    color = Color.Gray,
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }
    }
}
