package com.example.lab2

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent { MaterialTheme { MainApp() } }
    }
}

enum class AppDestinations(val label: String) {
    HOME("Головна"),
    PROFILE("Профіль"),
}

@Composable
fun MainApp() {
    var current by rememberSaveable { mutableStateOf(AppDestinations.HOME) }

    NavigationSuiteScaffold(
        navigationSuiteItems = {
            AppDestinations.entries.forEach {
                item(
                    icon = {},
                    label = { Text(it.label) },
                    selected = it == current,
                    onClick = { current = it }
                )
            }
        }
    ) {
        Scaffold { padding ->
            val mod = Modifier.fillMaxSize().padding(padding)
            when (current) {
                AppDestinations.HOME -> HomeScreen(mod)
                AppDestinations.PROFILE -> ProfileScreen(mod)
            }
        }
    }
}

// Екран 1 з вкладеною навігацією (NavHost)
@Composable
fun HomeScreen(modifier: Modifier) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "home_main",
        modifier = modifier
    ) {
        // Підекран 1: попередня розмітка + кнопка переходу
        composable("home_main") {
            HomeMainSubscreen(
                onOpenDetails = { navController.navigate("home_details") }
            )
        }

        // Підекран 2: довільна розмітка + повернення назад
        composable("home_details") {
            HomeDetailsSubscreen(
                onBack = { navController.popBackStack() }
            )
        }
    }
}

@Composable
fun HomeMainSubscreen(onOpenDetails: () -> Unit) {
    var count by remember { mutableIntStateOf(0) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFE3F2FD))
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Натискань: $count",
            fontSize = 26.sp,
            color = Color(0xFF0D47A1)
        )
        Spacer(Modifier.height(16.dp))
        Button(onClick = { count++ }) {
            Text("Додати +1")
        }
        Spacer(Modifier.height(12.dp))
        // Перехід 1: на другий підекран
        OutlinedButton(onClick = onOpenDetails) {
            Text("Перейти на другий підекран")
        }
    }
}

@Composable
fun HomeDetailsSubscreen(onBack: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFFF3E0))
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Другий підекран",
            fontSize = 24.sp,
            color = Color(0xFFE65100)
        )
        Spacer(Modifier.height(8.dp))
        Text(
            text = "Можна повернутися кнопкою нижче або системним жестом «Назад».",
            fontSize = 15.sp,
            color = Color(0xFFBF360C)
        )
        Spacer(Modifier.height(20.dp))
        // Перехід 2: повернення на перший підекран
        Button(onClick = onBack) {
            Text("Назад")
        }
    }
}

// Екран 2 (без змін з попередньої лабораторної)
@Composable
fun ProfileScreen(modifier: Modifier) {
    var status by rememberSaveable { mutableStateOf("Статус: очікування") }

    Column(
        modifier = modifier
            .background(Color(0xFFE8F5E9))
            .padding(24.dp),
        verticalArrangement = Arrangement.Bottom,
        horizontalAlignment = Alignment.End
    ) {
        Text(
            text = status,
            fontSize = 18.sp,
            color = Color(0xFF1B5E20)
        )
        Spacer(Modifier.height(12.dp))
        Button(
            onClick = { status = "Статус: оновлено!" },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Оновити")
        }
    }
}