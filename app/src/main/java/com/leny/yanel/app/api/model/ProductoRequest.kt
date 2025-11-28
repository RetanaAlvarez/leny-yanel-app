package com.leny.yanel.app.api.model

import kotlinx.serialization.Serializable

@Serializable
data class ProductosResponse(
    val content: List<Producto>,
    val pageable: Pageable? = null,
    val totalPages: Int = 1,
    val totalElements: Int = 0,
    val last: Boolean = true,
    val first: Boolean = true,
    val numberOfElements: Int = 0,
    val size: Int = 0,
    val number: Int = 0,
    val sort: Sort? = null,
    val empty: Boolean = false
)

@Serializable
data class Pageable(
    val pageNumber: Int,
    val pageSize: Int
)

@Serializable
data class Sort(
    val sorted: Boolean,
    val unsorted: Boolean,
    val empty: Boolean
)
