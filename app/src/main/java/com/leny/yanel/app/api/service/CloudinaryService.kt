package com.leny.yanel.app.api.service

import android.content.Context
import android.net.Uri
import io.ktor.client.request.forms.*
import io.ktor.client.statement.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.io.InputStream
import com.leny.yanel.app.api.ApiClient

object CloudinaryService {

    private const val CLOUD_NAME = "dr8zm4cq7"
    private const val UPLOAD_PRESET = "ml_default"

    suspend fun uploadImage(context: Context, uri: Uri): String {
        return withContext(Dispatchers.IO) {

            val input: InputStream = context.contentResolver.openInputStream(uri)
                ?: throw Exception("No se pudo leer la imagen")

            val byteArray = input.readBytes()

            val response: HttpResponse = ApiClient.client.submitFormWithBinaryData(
                url = "https://api.cloudinary.com/v1_1/$CLOUD_NAME/image/upload",
                formData = formData {
                    append("file", byteArray, Headers.build {
                        append(HttpHeaders.ContentType, "image/jpeg")
                    })
                    append("upload_preset", UPLOAD_PRESET)
                }
            )

            val json = JSONObject(response.bodyAsText())
            json.getString("secure_url")
        }
    }
}
