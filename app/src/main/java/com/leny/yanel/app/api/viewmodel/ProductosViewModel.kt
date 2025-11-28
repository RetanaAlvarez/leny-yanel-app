package com.leny.yanel.app.api.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.leny.yanel.app.api.data.TokenDataStore
import com.leny.yanel.app.api.model.Producto
import com.leny.yanel.app.api.service.ProductosService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ProductosViewModel(
    private val productosService: ProductosService = ProductosService(),
    private val tokenDataStore: TokenDataStore = TokenDataStore()
) : ViewModel() {

    private val _productos = MutableStateFlow<List<Producto>>(emptyList())
    val productos: StateFlow<List<Producto>> = _productos

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    fun cargarProductos() {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null

            try {
                val token = tokenDataStore.getToken()   // adapta si tu método necesita contexto
                if (token.isNullOrBlank()) {
                    _error.value = "No hay token de sesión"
                } else {
                    val response = productosService.obtenerProductos(token)
                    _productos.value = response.content
                }
            } catch (e: Exception) {
                _error.value = "Error al cargar productos: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }
}
