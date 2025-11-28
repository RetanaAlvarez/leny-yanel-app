package com.leny.yanel.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.leny.yanel.app.ui.LoginScreen
import com.leny.yanel.app.ui.ProductosScreen
import com.leny.yanel.app.ui.AgregarProductoScreen
import com.leny.yanel.app.ui.theme.AppLenyyanellTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            AppLenyyanellTheme {
                val navController = rememberNavController()

                Surface(color = MaterialTheme.colorScheme.background) {
                    AppNavHost(navController)
                }
            }
        }
    }
}

@Composable
fun AppNavHost(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = "login"
    ) {
        // Pantalla de login
        composable("login") {
            LoginScreen(navController)
        }

        // Listado de productos
        composable("productos") {
            ProductosScreen(navController)
        }

        // Pantalla para agregar producto
        composable("agregarProducto") {
            AgregarProductoScreen(navController)
        }
    }
}
