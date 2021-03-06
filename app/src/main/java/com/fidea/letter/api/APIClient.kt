package com.fidea.letter.api

import com.fidea.letter.models.Token
import com.google.gson.GsonBuilder
import okhttp3.Cache
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.io.File
import java.util.concurrent.TimeUnit

class APIClient {
    companion object {
        private const val BASE_URL = "http://armanexplorer.pythonanywhere.com/api/"
        private val REWRITE_CACHE_CONTROL_INTERCEPTOR =
            Interceptor { chain: Interceptor.Chain ->
                val originalResponse = chain.proceed(chain.request())
                if (true) {
                    val maxAge = 60 // read from cache for 1 minute
                    originalResponse.newBuilder()
                        .header("Cache-Control", "public, max-age=$maxAge")
                        .build()
                } else {
                    val maxStale = 60 * 60 * 24 * 28 // tolerate 4-weeks stale
                    originalResponse.newBuilder()
                        .header("Cache-Control", "public, only-if-cached, max-stale=$maxStale")
                        .build()
                }
            }
        private lateinit var retrofit: Retrofit
        val cacheSize = 5 * 1024 * 1024L
        private lateinit var cache: Cache
        private var token: String = ""
        private val client = OkHttpClient.Builder()
            .addInterceptor { chain: Interceptor.Chain ->
                val newRequest = chain.request().newBuilder()
                    .addHeader("Authorization", "Bearer $token")
                    .build()
                chain.proceed(newRequest)
            }.connectTimeout(100, TimeUnit.SECONDS)
            .readTimeout(100, TimeUnit.SECONDS)
            .writeTimeout(100, TimeUnit.SECONDS).addInterceptor(REWRITE_CACHE_CONTROL_INTERCEPTOR)
            .cache(cache).build()


        fun getRetrofit(): Retrofit {
            synchronized(retrofit) {
                if (token == "" || !this::retrofit.isInitialized) {
                    val gson = GsonBuilder()
                        .setLenient()
                        .create()
                    return Retrofit.Builder().client(client)
                        .addConverterFactory(ScalarsConverterFactory.create()).baseUrl(BASE_URL)
                        .addConverterFactory(GsonConverterFactory.create(gson)).build()
                }
                return retrofit
            }
        }

        fun getRetrofit(token: String?, cacheDirectory: File): Retrofit {
            synchronized(retrofit) {
                if (!this::retrofit.isInitialized) {
                    cache = Cache(cacheDirectory, cacheSize)
                    val gson = GsonBuilder()
                        .setLenient()
                        .create()
                    return Retrofit.Builder().client(client)
                        .addConverterFactory(ScalarsConverterFactory.create()).baseUrl(BASE_URL)
                        .addConverterFactory(GsonConverterFactory.create(gson)).build()
                }
                return retrofit
            }
        }
    }
}