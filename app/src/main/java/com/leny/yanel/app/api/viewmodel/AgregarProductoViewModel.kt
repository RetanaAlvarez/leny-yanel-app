package com.leny.yanel.app.api.viewmodel

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.leny.yanel.app.api.data.TokenDataStore
import com.leny.yanel.app.api.model.CreateProductoRequest
import com.leny.yanel.app.api.service.CloudinaryService
import com.leny.yanel.app.api.service.ProductosService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AgregarProductoViewModel : ViewModel() {

    private val productosService = ProductosService()
    private val cloudinaryService = CloudinaryService()

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
            try {
                _isSaving.value = true
                _error.value = null

                val precio = precioTexto.toDoubleOrNull()
                    ?: throw Exception("Precio inválido")

                if (imagenUri == null) throw Exception("Selecciona una imagen")

                // 🔵 SUBIR A CLOUDINARY
                val imageUrl = cloudinaryService.uploadImage(context, imagenUri)

                val token = TokenDataStore.token ?: ""

                val request = CreateProductoRequest(
                    name = nombre,
                    descripcion = descripcion,
                    price = precio,
                    imageBase64 = imageUrl,
                    visible = visible,
                    categoriaId = categoriaId
                )

                productosService.crearProducto(token, request)
                onSuccess()

            } catch (e: Exception) {
                _error.value = e.message
            } finally {
                _isSaving.value = false
            }
        }
    }
}
