package com.leny.yanel.app.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.leny.yanel.app.api.viewmodel.LoginViewModel

@Composable
fun LoginScreen(
    navController: NavHostController,
    vm: LoginViewModel = viewModel()
) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    val uiState = vm.uiState.collectAsState().value

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center
    ) {

        Text("Panel Admin Leny & Yanel", style = MaterialTheme.typography.headlineMedium)
        Spacer(Modifier.height(24.dp))

        OutlinedTextField(
            value = username,
            onValueChange = { username = it },
            label = { Text("Usuario") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(12.dp))

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Contraseña") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(24.dp))

        Button(
            onClick = {
                vm.login(username, password) {
                    navController.navigate("productos") {
                        popUpTo("login") { inclusive = true }
                    }
                }
            },
            enabled = !uiState.loading,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(if (uiState.loading) "Entrando..." else "Entrar")
        }

        uiState.error?.let { err ->
            Spacer(Modifier.height(12.dp))
            Text(text = err, color = MaterialTheme.colorScheme.error)
        }
    }
}
