package com.fidea.letter.api

import android.content.Context
import com.google.gson.GsonBuilder
import okhttp3.Cache
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory

class APIClient {

    companion object {
        private val BASE_URL = "http://127.0.0.1:8000/api/"
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
        private val retrofit: Retrofit? = null
        private var cache: Cache? = null
        private var token: String? = ""
        private val client = OkHttpClient.Builder()
            .addInterceptor { chain: Interceptor.Chain ->
                val newRequest = chain.request().newBuilder()
                    .addHeader("Authorization", "Bearer $token")
                    .build()
                chain.proceed(newRequest)
            }.addInterceptor(REWRITE_CACHE_CONTROL_INTERCEPTOR).cache(cache).build()


        fun getRetrofit(context: Context): Retrofit? {
            if (token == null || retrofit == null) {
                val gson = GsonBuilder()
                    .setLenient()
                    .create()
                token = context.getSharedPreferences("pref", Context.MODE_PRIVATE)
                    .getString("token", "nothing")
                val cacheSize = 5 * 1024 * 1024L
                cache = Cache(context.cacheDir, cacheSize)
                return Retrofit.Builder().client(client)
                    .addConverterFactory(ScalarsConverterFactory.create()).baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create(gson)).build()
            }
            return retrofit
        }
    }
}