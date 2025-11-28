package com.leny.yanel.app.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.leny.yanel.app.api.model.Producto

@Composable
fun ProductoCard(producto: Producto) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
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
