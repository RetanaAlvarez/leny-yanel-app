package com.leny.yanel.app.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.leny.yanel.app.api.model.Producto        // Ajusta el paquete si es distinto
import com.leny.yanel.app.api.viewmodel.ProductosViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductosScreen(
    navController: NavHostController,
    vm: ProductosViewModel = viewModel()
) {
    val productos by vm.productos.collectAsState()
    val error by vm.error.collectAsState()
    val isLoading by vm.isLoading.collectAsState()

    LaunchedEffect(Unit) {
        vm.cargarProductos()
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navController.navigate("agregarProducto") }
            ) {
                Icon(Icons.Default.Add, contentDescription = "Agregar producto")
            }
        }
    ) { padding ->
        Column(
            Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            Text("Productos", style = MaterialTheme.typography.headlineMedium)
            Spacer(Modifier.height(16.dp))

            if (isLoading) {
                CircularProgressIndicator()
                return@Column
            }

            if (error != null) {
                Text(
                    text = error ?: "",
                    color = MaterialTheme.colorScheme.error
                )
                Spacer(Modifier.height(8.dp))
            }

            LazyColumn {
                items(productos) { producto ->
                    ProductoCard(producto)
                }
            }
        }
    }
}

@Composable
fun ProductoCard(producto: Producto) {
    Card(
        modifier = Modifier
            .padding(vertical = 8.dp)
            .fillMaxWidth(),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(Modifier.padding(16.dp)) {

            AsyncImage(
                model = producto.imageBase64,
                contentDescription = producto.name,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
            )

            Spacer(Modifier.height(8.dp))

            Text(producto.name, style = MaterialTheme.typography.titleLarge)
            Text(producto.descripcion)
            Text("Precio: $${producto.price}")
            Text("Categoría: ${producto.categoria?.nombre ?: "Sin categoría"}")
        }
    }
}
