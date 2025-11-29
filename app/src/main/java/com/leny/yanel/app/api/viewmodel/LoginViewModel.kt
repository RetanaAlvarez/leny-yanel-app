package com.leny.yanel.app.api.viewmodel

import android.app.Application
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
        viewModelScope.launch {
            if (username.isBlank() || password.isBlank()) {
                _uiState.value = LoginUiState(
                    loading = false,
                    error = "Por favor llena usuario y contraseña"
                )
                return@launch
            }

            try {
                _uiState.value = LoginUiState(loading = true, error = null)

                val token = AuthService.login(username, password)

                // guardar token
                TokenDataStore.saveToken(getApplication(), token)

                _uiState.value = LoginUiState(loading = false, error = null)
                onSuccess()

            } catch (e: Exception) {
                _uiState.value = LoginUiState(
                    loading = false,
                    error = e.message ?: "Error desconocido"
                )
            }
        }
    }
}
