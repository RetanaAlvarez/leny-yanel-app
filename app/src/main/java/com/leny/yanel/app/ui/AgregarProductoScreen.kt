package com.leny.yanel.app.ui

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.leny.yanel.app.api.viewmodel.AgregarProductoViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AgregarProductoScreen(
    navController: NavHostController,
    vm: AgregarProductoViewModel = viewModel()
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    var nombre by remember { mutableStateOf("") }
    var descripcion by remember { mutableStateOf("") }
    var precio by remember { mutableStateOf("") }
    var visible by remember { mutableStateOf(true) }

    val categorias = listOf(
        1 to "Esferas de cristal",
        2 to "Mesa decorativa"
    )
    var categoriaSeleccionada by remember { mutableStateOf<Int?>(null) }
    var categoriaTexto by remember { mutableStateOf("") }

    var imagenUri by remember { mutableStateOf<Uri?>(null) }

    val isSaving by vm.isSaving.collectAsState()
    val error by vm.error.collectAsState()

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        if (uri != null) {
            imagenUri = uri
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        Text("Agregar Producto", style = MaterialTheme.typography.headlineMedium)
        Spacer(Modifier.height(16.dp))

        OutlinedTextField(
            value = nombre,
            onValueChange = { nombre = it },
            label = { Text("Nombre del Producto") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(8.dp))

        OutlinedTextField(
            value = descripcion,
            onValueChange = { descripcion = it },
            label = { Text("Descripción") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(8.dp))

        OutlinedTextField(
            value = precio,
            onValueChange = { precio = it },
            label = { Text("Precio") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(8.dp))

        Text("Categoría")
        Spacer(Modifier.height(4.dp))
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            categorias.forEach { (id, nombreCat) ->
                FilterChip(
                    selected = categoriaSeleccionada == id,
                    onClick = {
                        categoriaSeleccionada = id
                        categoriaTexto = nombreCat
                    },
                    label = { Text(nombreCat) }
                )
            }
        }

        Spacer(Modifier.height(8.dp))

        Row {
            Checkbox(checked = visible, onCheckedChange = { visible = it })
            Text("Producto visible en la tienda")
        }

        Spacer(Modifier.height(16.dp))

        Button(
            onClick = {
                launcher.launch(
                    PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                )
            }
        ) {
            Text("Seleccionar imagen")
        }

        imagenUri?.let { uri ->
            Spacer(Modifier.height(8.dp))
            AsyncImage(
                model = uri,
                contentDescription = "Vista previa",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
            )
        }

        Spacer(Modifier.height(16.dp))

        error?.let {
            Text(text = it, color = MaterialTheme.colorScheme.error)
            Spacer(Modifier.height(8.dp))
        }

        Button(
            onClick = {
                scope.launch {
                    vm.crearProducto(
                        context = context,
                        nombre = nombre,
                        descripcion = descripcion,
                        precioTexto = precio,
                        categoriaId = categoriaSeleccionada,
                        visible = visible,
                        imagenUri = imagenUri,
                        onSuccess = {
                            navController.popBackStack()
                        }
                    )
                }
            },
            enabled = !isSaving,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(if (isSaving) "Guardando..." else "Guardar Producto")
        }
    }
}
