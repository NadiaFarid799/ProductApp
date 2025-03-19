package com.example.productmvvm.ui.theme.Data.Repo

import com.example.productmvvm.ui.theme.Data.Model.Product
import kotlinx.coroutines.flow.Flow

interface ProductRepository {
    suspend fun getAllProduct(isOnline:Boolean):Flow< List<Product>>?
    suspend fun  addProduct(product: Product):Long
    suspend fun removeProduct(product: Product):Int
}