package com.example.productmvvm.ui.theme.AllProduct

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.example.productmvvm.ui.theme.Data.Local.ProductDataBase
import com.example.productmvvm.ui.theme.Data.Local.ProductLocalDataSource
import com.example.productmvvm.ui.theme.Data.Model.Product
import com.example.productmvvm.ui.theme.Data.Model.Response
import com.example.productmvvm.ui.theme.Data.Remote.ProductRemoteDataSource
import com.example.productmvvm.ui.theme.Data.Remote.RetrofitHelper
import com.example.productmvvm.ui.theme.Data.Repo.ProductRepositoryImp

class AllProductActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        setContent {
         ProductListScreen(viewModel(factory = AllProductFactory(
             ProductRepositoryImp.getInstance(
                 ProductRemoteDataSource(RetrofitHelper.apiService),
                 ProductLocalDataSource(ProductDataBase.getInstance(this@AllProductActivity)
                     .getProductDao())
             )
         )
         )
        , onProductClick = {product ->})

        }
    }
}



@Composable
fun ProductListScreen(viewModel: AllProductViewModel, onProductClick: (Product) -> Unit) {
    val uiState by viewModel.productList.collectAsState()

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        viewModel.toastEvent.collect { message ->
            snackbarHostState.showSnackbar(message)
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { contentPadding ->
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(contentPadding)
                .padding(16.dp), verticalArrangement = Arrangement.Center
        ) {
            when (uiState) {
                is Response.Loading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
                }
                is Response.Success -> {
                    val products = (uiState as Response.Success).data
                    LazyColumn {
                        items(products.size) { index ->
                            val product = products[index]
                            ProductRow(viewModel, product)
                        }
                    }
                }
                is Response.Failure -> {
                    Text(
                        text = "Error: ${(uiState as Response.Failure).error.message}",
                        color = Color.Red,
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )
                }
            }
        }
    }
}


@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun ProductRow(viewModel: AllProductViewModel, product: Product) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(5.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(5.dp)
        ) {
            GlideImage(
                model = product.thumbnail,
                contentDescription = null,
                modifier = Modifier.weight(0.4f)
            )

            Column(
                modifier = Modifier
                    .weight(0.6f)
                    .padding(8.dp)
            ) {
                Text(text = product.title, fontWeight = FontWeight.Bold)
                Text(text = "Price: $${product.price}")
                Text(
                    text = product.description,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(8.dp))

                Button(
                    onClick = { viewModel.addToFavourite(product) },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Add to Fav")
                }
            }
        }
    }
}

//@Composable
//fun ProductListScreen(viewModel: AllProductViewModel,onProductClick: (Product) -> Unit) {
//    viewModel.getProduct()
//   // val productState = viewModel.products.observeAsState()
//
//    val uiState by viewModel.productList.collectAsState()
//    val messageState = viewModel.messages.observeAsState()
//    val snackbarHostState = remember { SnackbarHostState() }
//    val scope = rememberCoroutineScope()
//    LaunchedEffect(messageState.value) {
//        messageState.value?.let { message ->
//            snackbarHostState.showSnackbar(message)
//        }
//    }
//
//    when(uiState){
//
//        is Response.Loading->{
//
//        }
//        is Response.Success->{
//
//
//        }
//        is Response.Failure ->{
//
//        }
//    }
//
//    Scaffold(
//        snackbarHost = { SnackbarHost(snackbarHostState) }
//    ) { contentPadding ->
//        Column(
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(contentPadding)
//                .padding(16.dp), verticalArrangement = Arrangement.Center
//        ) {
//            LazyColumn {
//                productState.value?.let { products ->
//                    items(products.size) { index ->
//                        val product = products[index]
//                        ProductRow  (viewModel,product)
//                    }
//                }
//            }
//        }
//
//
//    }
//
//}

//@OptIn(ExperimentalGlideComposeApi::class)
//@Composable
//fun ProductRow(viewModel: AllProductViewModel, product: Product) {
//    Card(
//        modifier = Modifier
//            .fillMaxWidth()
//            .padding(5.dp)
//    ) {
//        Row(
//            verticalAlignment = Alignment.CenterVertically,
//            modifier = Modifier.padding(5.dp)
//        ) {
//            GlideImage(
//                model = product.thumbnail,
//                contentDescription = null,
//                modifier = Modifier.weight(0.4f)
//            )
//
//            Column(
//                modifier = Modifier
//                    .weight(0.6f)
//                    .padding(8.dp)
//            ) {
//                Text(text = product.title, fontWeight = FontWeight.Bold)
//                Text(text = "Price: $${product.price}")
//                Text(
//                    text = product.description,
//                    maxLines = 2,
//                    overflow = TextOverflow.Ellipsis
//                )
//
//                Spacer(modifier = Modifier.height(8.dp))
//
//                Button(
//                     onClick = { viewModel.addToFavourite(product) },
//                    modifier = Modifier.fillMaxWidth()
//                ) {
//                    Text("Add to Fav")
//                }
//            }
//        }
//    }
//}

//@OptIn(ExperimentalGlideComposeApi::class)
//@Composable
//fun ProductRow(product: Product,actionName:String,action:()-> Unit) {
//    Card(modifier = Modifier.fillMaxWidth().padding(5.dp)) {
//
//        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(5.dp))
//        {
//            GlideImage(model = "${product.thumbnail}", contentDescription = null)
//
//            Column(modifier = Modifier.weight(1f).padding(8.dp)) {
//                Text(text = product.title, fontWeight = FontWeight.Bold)
//                Text(text = "Price: $${product.price}")
//                Text(text = product.description, maxLines = 2, overflow = TextOverflow.Ellipsis)
//
//            }
//            Button(onClick = action, Modifier.weight(0.3f)) {
//                Text(actionName)
//
//
//            }
//
//        }
//
//    }
//}

//@Composable
//fun ProductListItem(product: Product, onClick: () -> Unit) {
//    Card(
//        modifier = Modifier.fillMaxWidth(),
//        elevation = CardDefaults.cardElevation()
//    ) {
//        Column(modifier = Modifier.padding(16.dp)) {
//            AsyncImage(
//                model = product.thumbnail,
//                contentDescription = "Image",
//                modifier = Modifier.size(100.dp)
//                    .clickable { onClick() },
//            )
//            Spacer(modifier = Modifier.height(8.dp))
//            Text(text = product.title, fontSize = 20.sp, modifier = Modifier.padding(5.dp))
//            Text(text = "Price: $${product.price}", fontSize = 20.sp, modifier = Modifier.padding(5.dp))
//        }
//    }
//}
class ProductPreviewProvider : PreviewParameterProvider<Product> {
    override val values = sequenceOf(
        Product(
            id = 1,
            title = "Sample Product",
            description = "This is a sample product description.",
            price = 99.99,
            thumbnail = "https://via.placeholder.com/150"
        )
    )
}
