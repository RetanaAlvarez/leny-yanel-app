package com.leny.yanel.app.api.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.leny.yanel.app.api.data.TokenDataStore
import com.leny.yanel.app.api.model.Producto
import com.leny.yanel.app.api.service.ProductosService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ProductosViewModel : ViewModel() {

    private val service = ProductosService()

    private val _productos = MutableStateFlow<List<Producto>>(emptyList())
    val productos: StateFlow<List<Producto>> = _productos

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    fun cargarProductos() {
        viewModelScope.launch {
            try {
                val token = TokenDataStore.token ?: ""
                val response = service.obtenerProductos(token)
                _productos.value = response.content
            } catch (e: Exception) {
                _error.value = "Error al cargar productos: ${e.message}"
            }
        }
    }
}
