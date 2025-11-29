package com.leny.yanel.app.api.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.leny.yanel.app.api.data.TokenDataStore
import com.leny.yanel.app.api.model.Producto
import com.leny.yanel.app.api.service.ProductosService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class ProductosViewModel(app: Application) : AndroidViewModel(app) {

    private val _productos = MutableStateFlow<List<Producto>>(emptyList())
    val productos: StateFlow<List<Producto>> = _productos

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    fun cargarProductos() {
        viewModelScope.launch {
            try {
                val ctx = getApplication<Application>()
                val token = TokenDataStore.getTokenFlow(ctx).first()
                    ?: throw Exception("No hay token guardado")

                val response = ProductosService.obtenerProductos(token)
                _productos.value = response.content
                _error.value = null

            } catch (e: Exception) {
                _error.value = "Error al cargar productos: ${e.message}"
            }
        }
    }
}
