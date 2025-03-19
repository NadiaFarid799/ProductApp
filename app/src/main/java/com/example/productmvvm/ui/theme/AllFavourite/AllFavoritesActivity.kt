package com.example.productmvvm.ui.theme.AllFavourite

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.example.productmvvm.ui.theme.AllFavourite.ui.theme.ProductMVVMTheme
import com.example.productmvvm.ui.theme.AllProduct.ProductRow
import com.example.productmvvm.ui.theme.Data.Local.ProductDataBase
import com.example.productmvvm.ui.theme.Data.Local.ProductLocalDataSource
import com.example.productmvvm.ui.theme.Data.Model.Product
import com.example.productmvvm.ui.theme.Data.Remote.ProductRemoteDataSource
import com.example.productmvvm.ui.theme.Data.Remote.RemoteDataSource
import com.example.productmvvm.ui.theme.Data.Remote.RetrofitHelper
import com.example.productmvvm.ui.theme.Data.Repo.ProductRepositoryImp

class AllFavoritesActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FavoriteProductsScreen(viewModel(
                factory = FavProductsViewModelFactory(
                    ProductRepositoryImp.getInstance(
               ProductRemoteDataSource (RetrofitHelper.apiService),
                        ProductLocalDataSource( dao = ProductDataBase.getInstance(this).getProductDao())
            ))))

        }
    }
}
@Composable
fun FavoriteProductsScreen(viewModel: AllFavouritesViewModel) {
    viewModel.getProducts()
    val favState = viewModel.favourites.observeAsState()
    val messageState = viewModel.messages.observeAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    LaunchedEffect(messageState.value) {
        messageState.value?.let { message ->
            snackbarHostState.showSnackbar(message)
        }
    }

    Scaffold { contentPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(contentPadding)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "Favorite Products", fontWeight = FontWeight.Bold)

            if (favState.value.isNullOrEmpty()) {
                Text("No favorite products yet", Modifier.padding(16.dp))
            } else {
                LazyColumn {
                    items(favState.value!!) { product ->
                        FavoriteProductRow(viewModel, product)
                    }
                }
            }
        }
    }
}
@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun FavoriteProductRow(viewModel: AllFavouritesViewModel, product: Product) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(5.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                GlideImage(
                    model = product.thumbnail,
                    contentDescription = null,
                    modifier = Modifier.weight(0.3f)
                )

                Column(
                    modifier = Modifier
                        .weight(0.7f)
                        .padding(start = 8.dp)
                ) {
                    Text(text = product.title, fontWeight = FontWeight.Bold)
                    Text(text = "Price: $${product.price}")
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = { viewModel.deleteProduct(product) },
                modifier = Modifier.align(Alignment.End)
            ) {
                Text("Remove")
            }
        }
    }
}
