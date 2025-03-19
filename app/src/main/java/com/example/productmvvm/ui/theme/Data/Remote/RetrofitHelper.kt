package com.example.productmvvm.ui.theme.Data.Remote

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitHelper {
    private val retrofitInstance= Retrofit.Builder().
    baseUrl("https://dummyjson.com/").
    addConverterFactory(GsonConverterFactory.create())
        .build()


    val apiService= retrofitInstance.create(ProductApiService::class.java)
}