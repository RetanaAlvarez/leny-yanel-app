package com.leny.yanel.app.api.viewmodel

import android.app.Application
import android.content.Context
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.leny.yanel.app.api.data.TokenDataStore
import com.leny.yanel.app.api.model.CreateProductoRequest
import com.leny.yanel.app.api.service.CloudinaryService
import com.leny.yanel.app.api.service.ProductosService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AgregarProductoViewModel(
    application: Application,
    private val productosService: ProductosService = ProductosService(),
    private val cloudinaryService: CloudinaryService = CloudinaryService(),
    private val tokenDataStore: TokenDataStore = TokenDataStore()
) : AndroidViewModel(application) {

    private val _isSaving = MutableStateFlow(false)
    val isSaving: StateFlow<Boolean> = _isSaving

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    fun crearProducto(
        context: Context,
        nombre: String,
        descripcion: String,
        precioTexto: String,
        categoriaId: Int?,
        visible: Boolean,
        imagenUri: Uri?,
        onSuccess: () -> Unit
    ) {
        viewModelScope.launch {
            _isSaving.value = true
            _error.value = null

            try {
                if (nombre.isBlank() || descripcion.isBlank() || precioTexto.isBlank()) {
                    _error.value = "Todos los campos son obligatorios"
                    return@launch
                }

                val precio = precioTexto.toDoubleOrNull()
                if (precio == null) {
                    _error.value = "Precio inválido"
                    return@launch
                }

                if (categoriaId == null) {
                    _error.value = "Selecciona una categoría"
                    return@launch
                }

                if (imagenUri == null) {
                    _error.value = "Selecciona una imagen"
                    return@launch
                }

                val token = tokenDataStore.getToken()   // adapta igual que en ProductosVM
                if (token.isNullOrBlank()) {
                    _error.value = "No hay token de sesión"
                    return@launch
                }

                // 1) Subir imagen a Cloudinary
                val imageUrl = cloudinaryService.uploadImage(
                    context = context,
                    imageUri = imagenUri
                )

                // 2) Crear request
                val request = CreateProductoRequest(
                    name = nombre,
                    descripcion = descripcion,
                    price = precio,
                    imageBase64 = imageUrl,
                    visible = visible,
                    categoriaId = categoriaId
                )

                // 3) Llamar API
                productosService.crearProducto(token, request)

                onSuccess()
            } catch (e: Exception) {
                _error.value = "Error al guardar: ${e.message}"
            } finally {
                _isSaving.value = false
            }
        }
    }
}
