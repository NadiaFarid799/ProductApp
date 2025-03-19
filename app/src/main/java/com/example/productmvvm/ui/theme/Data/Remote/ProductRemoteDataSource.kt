package com.example.productmvvm.ui.theme.Data.Remote

import com.example.productmvvm.ui.theme.Data.Model.Product
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class ProductRemoteDataSource(private val service: ProductApiService) :RemoteDataSource{

    override suspend fun getAllProduct():Flow<List<Product>> = flow{
        val response= service.getProducts()
        if (response.isSuccessful) {
            response.body()?.let { emit(it.products) } // ✅ Emit the List<Product> inside Flow
        } else {
            throw Exception("Error fetching products: ${response.message()}")
        }
    }.flowOn(Dispatchers.IO)


}