package com.task.network.model

import okhttp3.OkHttpClient
import okhttp3.internal.connection.ConnectInterceptor
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RestClient {

    private const val BASE_URL = "https://demo.ezetap.com/mobileapps/"

    private val mRetroFit =
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(
                getOkHttp()
            )
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    fun getOkHttp(): OkHttpClient {
        val client = OkHttpClient
            .Builder()
            .connectTimeout(2, TimeUnit.MINUTES)
            .writeTimeout(2, TimeUnit.MINUTES)
            .readTimeout(2, TimeUnit.MINUTES)
        val interceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
        client.addInterceptor(interceptor= interceptor)
        return client.build()
    }


    fun getAssignmentService(): AssignmentService = mRetroFit.create(AssignmentService::class.java)
}