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
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class AgregarProductoViewModel(app: Application) : AndroidViewModel(app) {

    private val cloudinary = CloudinaryService()

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

                // 1) subir a Cloudinary
                val imageUrl = cloudinary.uploadImage(context, imagenUri)

                // 2) obtener token
                val ctx = getApplication<Application>()
                val token = TokenDataStore.getTokenFlow(ctx).first()
                    ?: throw Exception("No hay token guardado")

                // 3) crear request
                val req = CreateProductoRequest(
                    name = nombre,
                    descripcion = descripcion,
                    price = precio,
                    imageBase64 = imageUrl,   // realmente es URL
                    visible = visible,
                    categoriaId = categoriaId
                )

                // 4) llamar servicio
                ProductosService.crearProducto(token, req)

                onSuccess()

            } catch (e: Exception) {
                _error.value = e.message ?: "Error guardando producto"
            } finally {
                _isSaving.value = false
            }
        }
    }
}
