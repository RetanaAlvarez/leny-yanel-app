package com.leny.yanel.app.api.service

import com.leny.yanel.app.api.ApiClient
import com.leny.yanel.app.api.model.CreateProductoRequest
import com.leny.yanel.app.api.model.ProductosResponse
import io.ktor.client.call.body
import io.ktor.client.request.*
import io.ktor.http.*

object ProductosService {

    suspend fun obtenerProductos(token: String): ProductosResponse {
        return ApiClient.client.get("${ApiClient.BASE_URL}/api/admin/productos") {
            header(HttpHeaders.Authorization, "Bearer $token")
        }.body()
    }

    suspend fun crearProducto(token: String, request: CreateProductoRequest) {
        val response = ApiClient.client.post("${ApiClient.BASE_URL}/api/admin/productos") {
            header(HttpHeaders.Authorization, "Bearer $token")
            contentType(ContentType.Application.Json)
            setBody(request)
        }

        if (!response.status.isSuccess()) {
            throw Exception("Error creando producto: ${response.status.value}")
        }
    }
}
