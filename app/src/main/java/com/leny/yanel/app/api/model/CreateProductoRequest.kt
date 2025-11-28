package com.leny.yanel.app.api.model

import kotlinx.serialization.Serializable

@Serializable
data class CreateProductoRequest(
    val name: String,
    val descripcion: String,
    val price: Double,
    val imageBase64: String,   // URL de Cloudinary
    val visible: Boolean,
    val categoriaId: Int
)
