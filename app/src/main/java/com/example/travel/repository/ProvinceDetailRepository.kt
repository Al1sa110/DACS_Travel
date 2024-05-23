package com.example.travel.repository

import com.example.travel.model.provinceResult.provinceDetail
import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import java.util.concurrent.TimeUnit

private const val BASE_URL = "https://maps-data.p.rapidapi.com"

val client = OkHttpClient.Builder()
    .connectTimeout(60,TimeUnit.SECONDS)
    .readTimeout(60,TimeUnit.SECONDS)
    .addInterceptor{
        val request:Request = it.request().newBuilder()
            .addHeader("X-RapidAPI-Key", "54b12c1bd0mshc107b6188b97781p199c35jsncfb73a628b1e")
            .addHeader("X-RapidAPI-Host", "maps-data.p.rapidapi.com")
            .build()
        it.proceed(request)
    }
    .build()

private val retrofit = Retrofit.Builder()
    .baseUrl(BASE_URL)
    .addConverterFactory(GsonConverterFactory.create())
    .client(client)
    .build()

interface ProvinceDetailRepository {
    @GET("/place.php")
    suspend fun getDetail(
        @Query("business_id") business_id: String? = "0x47f4eb87e91f866d:0x9629fabb993eb66"
    ): provinceDetail ? = null
}

object ProvinceDetailApi {
    val retrofitService:ProvinceDetailRepository by lazy {
        retrofit.create(ProvinceDetailRepository::class.java)
    }
}