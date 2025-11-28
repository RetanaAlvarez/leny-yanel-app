package com.leny.yanel.app.api.model

import kotlinx.serialization.Serializable

@Serializable
data class ProductosResponse(
    val content: List<Producto>,
    val pageable: Pageable,
    val totalPages: Int,
    val totalElements: Int,
    val last: Boolean,
    val first: Boolean,
    val numberOfElements: Int,
    val size: Int,
    val number: Int,
    val sort: Sort,
    val empty: Boolean
)

@Serializable
data class Pageable(
    val sort: Sort,
    val pageNumber: Int,
    val pageSize: Int,
    val offset: Int,
    val paged: Boolean,
    val unpaged: Boolean
)

@Serializable
data class Sort(
    val sorted: Boolean,
    val unsorted: Boolean,
    val empty: Boolean
)
