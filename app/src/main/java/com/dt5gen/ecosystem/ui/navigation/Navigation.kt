package com.dt5gen.ecosystem.ui.navigation



import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.dt5gen.ecosystem.presentation.bin.BinViewModel
import com.dt5gen.ecosystem.ui.HistoryScreen
import com.dt5gen.ecosystem.ui.MainScreen

@Composable
fun NavigationGraph(
    navController: NavHostController,
    viewModel: BinViewModel
) {
    NavHost(navController = navController, startDestination = "main_screen") {
        composable("main_screen") {
            MainScreen(
                viewModel = viewModel,
                onNavigateToHistory = { navController.navigate("history_screen") }
            )
        }
        composable("history_screen") {
            HistoryScreen(
                viewModel = viewModel,
                onBackClick = { navController.popBackStack() },
                onClearHistory = { viewModel.clearHistory() } // Здесь передаем логику очистки
            )
        }
    }
}