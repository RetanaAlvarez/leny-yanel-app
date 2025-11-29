package com.leny.yanel.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
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
                Surface(color = MaterialTheme.colorScheme.background) {
                    val navController = rememberNavController()

                    NavHost(
                        navController = navController,
                        startDestination = "login"
                    ) {

                        composable("login") {
                            LoginScreen(navController)
                        }

                        composable("productos") {
                            ProductosScreen(navController)
                        }

                        composable("agregarProducto") {
                            AgregarProductoScreen(navController)
                        }
                    }
                }
            }
        }
    }
}
