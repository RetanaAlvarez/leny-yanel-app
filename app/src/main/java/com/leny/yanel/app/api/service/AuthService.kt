package com.leny.yanel.app.api.service

import com.leny.yanel.app.api.ApiClient
import com.leny.yanel.app.api.model.LoginRequest
import com.leny.yanel.app.api.model.LoginResponse
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*

object AuthService {

    suspend fun login(username: String, password: String): String {

        return try {

            val response: LoginResponse = ApiClient.client.post("${ApiClient.BASE_URL}auth/login") {
                contentType(ContentType.Application.Json)
                setBody(LoginRequest(username, password))
            }.body()


            response.token

        } catch (e: Exception) {
            throw Exception("Usuario o contraseña incorrectos")
        }
    }
}
