package com.example.travel.repository

import com.example.travel.model.provinceDetail.ProvinceDetail
import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import java.util.concurrent.TimeUnit

private const val BASE_URL = "https://maps-data.p.rapidapi.com"

val client1 = OkHttpClient.Builder()
    .connectTimeout(60,TimeUnit.SECONDS)
    .readTimeout(60,TimeUnit.SECONDS)
    .addInterceptor{
        val request:Request = it.request().newBuilder()
            .addHeader("X-RapidAPI-Key", "d91bd9def0msh04d50fc151c4575p1a1799jsn51d036f911f7")
            .addHeader("X-RapidAPI-Host", "maps-data.p.rapidapi.com")
            .build()
        it.proceed(request)
    }
    .build()

private val retrofit1 = Retrofit.Builder()
    .baseUrl(BASE_URL)
    .addConverterFactory(GsonConverterFactory.create())
    .client(client1)
    .build()

interface ProvinceRepository {
    @GET("/searchmaps.php")
    suspend fun getDetail(
        @Query("query") query: String? = "hotel and restaurant",
        @Query("limit") limit: Int = 30,
        @Query("offset") offset: Int = 0,
        @Query("lang") lang: String = "en",
        @Query("country") country: String = "us",
        @Query("zoom") zoom: String = "13",
        @Query("lat") lat: String = "51.5072",
        @Query("ln") ln: String = "0.12"
    ): ProvinceDetail ? = null
}

object ProvinceApi {
    val retrofitService:ProvinceRepository by lazy {
        retrofit1.create(ProvinceRepository::class.java)
    }
}