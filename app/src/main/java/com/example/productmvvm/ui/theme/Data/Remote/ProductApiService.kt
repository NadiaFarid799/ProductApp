package com.example.productmvvm.ui.theme.Data.Remote

import retrofit2.Response
import retrofit2.http.GET

interface ProductApiService {

@GET("products")
suspend fun getProducts():Response<ProductResponse>
}