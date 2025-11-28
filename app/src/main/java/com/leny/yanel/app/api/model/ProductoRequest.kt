package com.leny.yanel.app.api.model

import kotlinx.serialization.Serializable

@Serializable
data class ProductoRequest(
    val name: String,
    val descripcion: String,
    val price: Double,
    val imageBase64: String,     // URL de Cloudinary
    val categoriaId: Long,
    val visible: Boolean
)
