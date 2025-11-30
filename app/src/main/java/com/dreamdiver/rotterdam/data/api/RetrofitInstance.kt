package com.dreamdiver.rotterdam.data.api

import android.content.Context
import com.dreamdiver.rotterdam.data.PreferencesManager
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitInstance {
    private const val BASE_URL = "https://rotterdam.dreamdiver.nl/"
    private var context: Context? = null

    fun init(applicationContext: Context) {
        context = applicationContext
    }

    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val languageInterceptor = Interceptor { chain ->
        val originalRequest = chain.request()
        val language = context?.let { ctx ->
            runBlocking {
                PreferencesManager(ctx).language.first()
            }
        } ?: "en"

        val newRequest = originalRequest.newBuilder()
            .addHeader("Accept-Language", language)
            .build()

        chain.proceed(newRequest)
    }

    private val client = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .addInterceptor(languageInterceptor)
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .build()

    val api: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
}

