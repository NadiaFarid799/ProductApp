package com.example.productmvvm.ui.theme.Data.Local

import com.example.productmvvm.ui.theme.Data.Model.Product
import kotlinx.coroutines.flow.Flow

class ProductLocalDataSource(private val dao: ProductDao) : LocalDataSource {
    override suspend fun InsertProduct(product: Product): Long {
        return dao.insertProduct(product)

    }

    override suspend fun DeleteProduct(product: Product): Int {
        return if (product != null)
            dao.deleteProduct(product)
        else -1


    }

    override suspend fun getAllProduct(): Flow <List<Product>> {

        return dao.getAllFavouriteProduct()
    }


}

