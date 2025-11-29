package com.leny.yanel.app.api.model

import kotlinx.serialization.Serializable

@Serializable
data class Categoria(
    val id: Int,
    val nombre: String
)

@Serializable
data class Producto(
    val id: Int,
    val name: String,
    val descripcion: String,
    val price: Double,
    val imageBase64: String,   // ⚠️ aquí viene la URL de Cloudinary
    val visible: Boolean,
    val categoria: Categoria?
)
