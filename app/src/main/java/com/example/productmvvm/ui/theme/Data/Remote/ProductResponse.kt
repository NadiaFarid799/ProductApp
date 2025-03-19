package com.example.productmvvm.ui.theme.Data.Remote

import com.example.productmvvm.ui.theme.Data.Model.Product
import kotlinx.coroutines.flow.Flow

data class ProductResponse (

    val products:List<Product>
)
