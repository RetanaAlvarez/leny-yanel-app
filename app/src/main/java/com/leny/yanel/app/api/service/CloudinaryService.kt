package com.leny.yanel.app.api.service

import android.content.Context
import android.net.Uri
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.logging.HttpLoggingInterceptor
import java.io.File
import java.io.FileOutputStream

class CloudinaryService {

    private val cloudName = "dr8zm4cq7"
    private val uploadPreset = "ml_default"

    private val client: OkHttpClient by lazy {
        OkHttpClient.Builder()
            .addInterceptor(
                HttpLoggingInterceptor().apply {
                    level = HttpLoggingInterceptor.Level.BODY
                }
            )
            .build()
    }

    suspend fun uploadImage(context: Context, imageUri: Uri): String =
        withContext(Dispatchers.IO) {

            val file = createTempFile(context, imageUri)

            val requestBody = MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart(
                    "file",
                    file.name,
                    file.asRequestBody("image/*".toMediaType())
                )
                .addFormDataPart("upload_preset", uploadPreset)
                .build()

            val request = Request.Builder()
                .url("https://api.cloudinary.com/v1_1/$cloudName/image/upload")
                .post(requestBody)
                .build()

            val response = client.newCall(request).execute()

            if (!response.isSuccessful) {
                throw Exception("Cloudinary error: ${response.message}")
            }

            val body = response.body?.string()
                ?: throw Exception("Cloudinary empty response")

            val secureUrlRegex = """"secure_url":"(.*?)""""
                .toRegex()

            val match = secureUrlRegex.find(body)
                ?: throw Exception("No secure_url found")

            match.groupValues[1]
        }

    private fun createTempFile(context: Context, uri: Uri): File {
        val input = context.contentResolver.openInputStream(uri)
            ?: throw Exception("Cannot open image")

        val temp = File.createTempFile("upload", ".jpg", context.cacheDir)

        FileOutputStream(temp).use { output ->
            input.copyTo(output)
        }

        return temp
    }
}
