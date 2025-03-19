package com.example.productmvvm.ui.theme.AllFavourite

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.productmvvm.ui.theme.Data.Model.Product
import com.example.productmvvm.ui.theme.Data.Repo.ProductRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AllFavouritesViewModel(private val repo:ProductRepository) :ViewModel(){


    private val mutableFav:MutableLiveData <List<Product>> =MutableLiveData()
    val favourites:LiveData<List<Product>> =mutableFav

    private val mutableMessage:MutableLiveData<String> =MutableLiveData("")
    val messages:LiveData<String> =mutableMessage


    fun deleteProduct(product: Product){
        viewModelScope.launch ( Dispatchers.IO){
        repo.removeProduct(product)
       // getProducts()
        }
    }
    fun getProducts(){
        viewModelScope.launch (Dispatchers.IO){
            repo.getAllProduct(false)?.collect{
                productList->mutableFav.postValue(productList)
            }
        }
    }
}

class FavProductsViewModelFactory(private  val _repo:ProductRepository):ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(AllFavouritesViewModel::class.java)){

            AllFavouritesViewModel(_repo) as T
        }else{
            throw IllegalArgumentException("view model class not found")
        }
    }
}