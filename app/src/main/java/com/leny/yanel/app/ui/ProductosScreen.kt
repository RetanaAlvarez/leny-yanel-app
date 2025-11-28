package com.leny.yanel.app.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.leny.yanel.app.api.viewmodel.ProductosViewModel
import com.leny.yanel.app.ui.components.ProductoCard

@Composable
fun ProductosScreen(navController: NavHostController, vm: ProductosViewModel = viewModel()) {

    val productos by vm.productos.collectAsState()
    val error by vm.error.collectAsState()

    LaunchedEffect(Unit) {
        vm.cargarProductos()
    }

    Column(Modifier.fillMaxSize().padding(16.dp)) {

        Text("Productos", style = MaterialTheme.typography.headlineMedium)

        Spacer(Modifier.height(16.dp))

        if (error != null) {
            Text(text = error!!, color = MaterialTheme.colorScheme.error)
        }

        LazyColumn {
            items(productos) { producto ->
                ProductoCard(producto)
            }
        }
    }
}
