package com.leny.yanel.app.api.service

import com.leny.yanel.app.api.ApiClient
import com.leny.yanel.app.api.model.CreateProductoRequest
import com.leny.yanel.app.api.model.ProductosResponse
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*

object ProductosService {

    suspend fun obtenerProductos(token: String): ProductosResponse {
        return ApiClient.client.get("${ApiClient.BASE_URL}/api/admin/productos") {
            header("Authorization", "Bearer $token")
        }.body()
    }

    suspend fun crearProducto(
        token: String,
        nombre: String,
        descripcion: String,
        precio: Double,
        categoriaId: Int,
        visible: Boolean,
        imageUrl: String
    ) {
        val body = CreateProductoRequest(
            name = nombre,
            descripcion = descripcion,
            price = precio,
            categoriaId = categoriaId,
            visible = visible,
            imageUrl = imageUrl
        )

        ApiClient.client.post("${ApiClient.BASE_URL}/api/admin/productos") {
            header("Authorization", "Bearer $token")
            contentType(ContentType.Application.Json)
            setBody(body)
        }
    }
}
