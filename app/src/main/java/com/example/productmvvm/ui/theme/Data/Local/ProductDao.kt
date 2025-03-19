package com.example.productmvvm.ui.theme.Data.Local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.productmvvm.ui.theme.Data.Model.Product
import kotlinx.coroutines.flow.Flow

@Dao
interface ProductDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertProduct(product:Product):Long
    @Delete
    suspend fun deleteProduct(product:Product):Int

    @Query("SELECT *FROM products")
     fun getAllFavouriteProduct():Flow<List<Product>>



}