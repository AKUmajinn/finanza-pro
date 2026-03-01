package com.example.finanzapro.adapter

import retrofit2.Response
import retrofit2.http.GET

interface ApiService {

    @GET("/categorias")
    suspend fun getCategories(): Response<List<String>>
}