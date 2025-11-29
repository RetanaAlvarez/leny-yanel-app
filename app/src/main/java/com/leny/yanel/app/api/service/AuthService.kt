package com.leny.yanel.app.api.service

import com.leny.yanel.app.api.ApiClient
import com.leny.yanel.app.api.model.LoginRequest
import com.leny.yanel.app.api.model.LoginResponse
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType

object AuthService {

    suspend fun login(username: String, password: String): String {
        val response: LoginResponse = ApiClient.client.post("${ApiClient.BASE_URL}/auth/login") {
            contentType(ContentType.Application.Json)
            setBody(LoginRequest(username, password))
        }.body()

        return response.token
    }
}
