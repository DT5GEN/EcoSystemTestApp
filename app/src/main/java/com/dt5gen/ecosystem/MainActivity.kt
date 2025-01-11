package com.dt5gen.ecosystem

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.dt5gen.ecosystem.presentation.bin.BinViewModel
import com.dt5gen.ecosystem.ui.HistoryScreen
import com.dt5gen.ecosystem.ui.MainScreen
import com.dt5gen.ecosystem.ui.theme.EcoSystemTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            EcoSystemTheme {
                val navController = rememberNavController()
                val viewModel: BinViewModel = hiltViewModel()

                NavHost(
                    navController = navController,
                    startDestination = "main_screen"
                ) {
                    composable(route = "main_screen") {
                        MainScreen(
                            viewModel = viewModel,
                            onNavigateToHistory = {
                                navController.navigate(route = "history_screen")
                            }
                        )
                    }
                    composable(route = "history_screen") {
                        HistoryScreen(
                            viewModel = viewModel,
                            onBackClick = {
                                navController.popBackStack()
                            },
                            onClearHistory = {
                                viewModel.clearHistory() // Убедитесь, что эта функция существует в ViewModel
                            }
                        )
                    }
                }
            }
        }
    }
}

