package com.example.productmvvm.ui.theme.AllProduct

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.productmvvm.ui.theme.Data.Model.Product
import com.example.productmvvm.ui.theme.Data.Model.Response
import com.example.productmvvm.ui.theme.Data.Repo.ProductRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch

class AllProductViewModel(private  val repo:ProductRepository) :ViewModel() {

//    private val mutableProduct:MutableLiveData<List<Product>> =MutableLiveData()
//            val products:LiveData<List<Product>> =mutableProduct
    private val mutableMessage:MutableLiveData<String> =MutableLiveData()
    val messages:LiveData<String> =mutableMessage


    private val _productList = MutableStateFlow<Response>(Response.Loading)
    val productList = _productList.asStateFlow()


    private val _toastEvent = MutableSharedFlow<String>()
    val toastEvent = _toastEvent.asSharedFlow()

    init {
        getProduct()
    }
//    fun getProduct() {
//        viewModelScope.launch {
//            repo.getAllProduct(true)
//                ?.flowOn(Dispatchers.IO)
//                ?.catch { ex -> mutableMessage.postValue("An error occurred: ${ex.message}") }
//                ?.collect { productList -> mutableProduct.postValue(productList) }
//        }
//    }

    fun getProduct() {
        viewModelScope.launch() {
try {


            val products =
                repo.getAllProduct(true)
            products
                ?.catch { ex ->
                    _productList.value = Response.Failure(ex)
                    _toastEvent.emit("Error from api:${ex.message}")
                }

                ?.collect { _productList.value = Response.Success(it) }

        }catch(ex :Exception){
            _productList.value=Response.Failure(ex)
    _toastEvent.emit("error:${ex.message}")

        }
    }
    }


//      fun  getProduct(){
//
//          viewModelScope.launch (){
//
//              try {
//                  repo.getAllProduct(true)?.collect{productList->
//                      mutableProduct.postValue(productList)
//                  }
//
//
//              }catch (ex:Exception){
//
//                  mutableMessage.postValue("an error,${ex.message}")
//              }
//          }
//      }

    fun addToFavourite(product: Product?){
        if (product!=null){
            viewModelScope.launch() {
                try {
                    val result=repo.addProduct(product)
                    if (result>0){
                        mutableMessage.postValue("AddedSuccefully")
                    }else{
                        mutableMessage.postValue("product already added")
                    }
                }catch (ex:Exception){
                    mutableMessage.postValue("coudny added ,${ex.message}")
                }
            }
        }else{
            mutableMessage.postValue("coudnt  added")

        }
    }

}
class AllProductFactory(private  val repo: ProductRepository):ViewModelProvider.Factory{

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return AllProductViewModel(repo) as T
    }
}