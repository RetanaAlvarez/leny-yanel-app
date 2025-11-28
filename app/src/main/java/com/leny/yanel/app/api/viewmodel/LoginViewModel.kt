package com.leny.yanel.app.api.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.leny.yanel.app.api.data.TokenDataStore
import com.leny.yanel.app.api.service.AuthService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class LoginUiState(
    val loading: Boolean = false,
    val error: String? = null
)

class LoginViewModel(app: Application) : AndroidViewModel(app) {

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState

    fun login(username: String, password: String, onSuccess: () -> Unit) {

        if (username.isBlank() || password.isBlank()) {
            _uiState.value = LoginUiState(
                loading = false,
                error = "Usuario y contraseña son obligatorios"
            )
            return
        }

        viewModelScope.launch {
            _uiState.value = LoginUiState(loading = true)

            try {

                val token = AuthService.login(username, password)
                TokenDataStore.saveToken(getApplication(), token)

                onSuccess()

            } catch (e: Exception) {
                Log.e("LoginViewModel", "Error en login", e)
                _uiState.value = LoginUiState(
                    loading = false,
                    error = "Credenciales incorrectas o error de conexión"
                )
            }
        }
    }
}
