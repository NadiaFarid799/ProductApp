package com.example.productmvvm.ui.theme.Data.Local

import com.example.productmvvm.ui.theme.Data.Model.Product
import kotlinx.coroutines.flow.Flow

interface LocalDataSource {


    suspend fun InsertProduct(product: Product):Long

    suspend fun DeleteProduct(product: Product):Int

    suspend fun getAllProduct():Flow<List<Product>>


}