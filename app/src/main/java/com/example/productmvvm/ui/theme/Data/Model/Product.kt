package com.example.productmvvm.ui.theme.Data.Model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "products")
data class Product (
    @PrimaryKey
    val id :Int,
    val title:String,
    val thumbnail:String,
    val description: String,
    val price: Double
)