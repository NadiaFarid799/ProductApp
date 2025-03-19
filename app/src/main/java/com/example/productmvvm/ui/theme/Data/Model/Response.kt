package com.example.productmvvm.ui.theme.Data.Model

sealed class Response {

    data object Loading :Response()
    data class Success(val data:List<Product>):Response()
    data class Failure(val error:Throwable):Response()
}