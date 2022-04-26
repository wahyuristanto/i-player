package com.i.player.config

import android.content.Context
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


object ApiClient {
    private const val BASE_URL: String =
        "https://itunes.apple.com/"
    var gson: Gson = GsonBuilder()
        .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
        .create()

    private fun okhttpClient(context: Context): OkHttpClient {
        val logging = HttpLoggingInterceptor()
        logging.setLevel(HttpLoggingInterceptor.Level.BASIC)

        return OkHttpClient.Builder()
            .addInterceptor(AuthInterceptor(context))
            .addInterceptor(logging)
//            .connectTimeout(10, TimeUnit.SECONDS)
            .build()
    }
    fun buildService(context: Context): ApiService {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(okhttpClient(context))
            .build()
        return retrofit.create(ApiService::class.java)
    }
}