package com.leny.yanel.app.api.service

import android.content.Context
import android.net.Uri
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.logging.HttpLoggingInterceptor
import java.io.File
import java.io.FileOutputStream

class CloudinaryService {

    private val cloudName = "dr8zm4cq7"   // CAMBIAR POR TU CLOUD NAME
    private val uploadPreset = "ml_default" // CAMBIAR POR TU UPLOAD PRESET

    private val client: OkHttpClient by lazy {
        OkHttpClient.Builder()
            .addInterceptor(
                HttpLoggingInterceptor().apply {
                    level = HttpLoggingInterceptor.Level.BODY
                }
            ).build()
    }

    suspend fun uploadImage(context: Context, imageUri: Uri): String {
        val file = createTempFileFromUri(context, imageUri)

        val requestBody = MultipartBody.Builder()
            .setType(MultipartBody.FORM)
            .addFormDataPart(
                "file",
                file.name,
                RequestBody.create("image/*".toMediaTypeOrNull(), file)
            )
            .addFormDataPart("upload_preset", uploadPreset)
            .build()

        val request = Request.Builder()
            .url("https://api.cloudinary.com/v1_1/$cloudName/image/upload")
            .post(requestBody)
            .build()

        val response = client.newCall(request).execute()

        if (!response.isSuccessful) {
            throw Exception("Error al subir imagen: ${response.code}")
        }

        val json = response.body?.string() ?: ""
        val url = extractSecureUrl(json)

        if (url == null) {
            throw Exception("No se pudo obtener la URL segura")
        }

        return url
    }

    private fun extractSecureUrl(json: String): String? {
        val regex = """"secure_url"\s*:\s*"([^"]+)"""".toRegex()
        return regex.find(json)?.groupValues?.get(1)
    }

    private fun createTempFileFromUri(context: Context, uri: Uri): File {
        val input = context.contentResolver.openInputStream(uri)
            ?: throw Exception("No se pudo leer la imagen")

        val tempFile = File.createTempFile("upload", ".jpg", context.cacheDir)
        val output = FileOutputStream(tempFile)

        input.copyTo(output)
        input.close()
        output.close()

        return tempFile
    }
}
