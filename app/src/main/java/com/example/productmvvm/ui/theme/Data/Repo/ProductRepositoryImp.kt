package com.example.productmvvm.ui.theme.Data.Repo

import com.example.productmvvm.ui.theme.Data.Local.LocalDataSource
import com.example.productmvvm.ui.theme.Data.Local.ProductLocalDataSource
import com.example.productmvvm.ui.theme.Data.Model.Product
import com.example.productmvvm.ui.theme.Data.Remote.RemoteDataSource
import kotlinx.coroutines.flow.Flow

class ProductRepositoryImp private constructor(

    private val localDataSource:  LocalDataSource,
    private val remoteDataSource: RemoteDataSource
):ProductRepository {
    override suspend fun getAllProduct(isOnline:Boolean): Flow<List<Product>>? {
           return if (isOnline) {
        remoteDataSource.getAllProduct()
           }else{
       localDataSource.getAllProduct()
           }


    }

    override suspend fun addProduct(product: Product):Long {

      return  localDataSource.InsertProduct(product)
    }

    override suspend fun removeProduct(product: Product) :Int{
        return localDataSource.DeleteProduct(product)
    }
    companion object {
        @Volatile
        private var INSTANCE: ProductRepositoryImp? = null

        fun getInstance(remoteDataSource: RemoteDataSource, localDataSource: LocalDataSource): ProductRepositoryImp {
            return INSTANCE ?: synchronized(this) {
                val temp = ProductRepositoryImp( localDataSource ,remoteDataSource)
                INSTANCE = temp
                temp
            }
        }
    }

}