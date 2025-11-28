package com.leny.yanel.app.api.service

import com.leny.yanel.app.api.ApiClient
import com.leny.yanel.app.api.model.ProductosResponse
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.header

class ProductosService {

    suspend fun obtenerProductos(token: String): ProductosResponse {
        return ApiClient.client.get("${ApiClient.BASE_URL}api/admin/productos") {
            header("Authorization", "Bearer $token")
        }.body()
    }
}
