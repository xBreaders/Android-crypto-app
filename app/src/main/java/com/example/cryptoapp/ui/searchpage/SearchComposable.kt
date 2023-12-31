package com.example.cryptoapp.ui.searchpage

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.cryptoapp.persistence.api.CoinData

@Composable
fun SearchCryptoScreen(
    navController: NavController,
    viewModel: SearchCryptoViewModel = viewModel(factory = SearchCryptoViewModel.Factory())
) {
    var searchText by remember { mutableStateOf("") }

    Column {
        SearchInputField(searchText, { newText ->
            searchText = newText
        }, {
            viewModel.searchCrypto(searchText)
        })

        val filteredCryptos = viewModel.searchResults.collectAsState(initial = emptyList())
        val isLoading = viewModel.isLoading.collectAsState(initial = false).value

        if (isLoading) {
            Text(
                text = "Loading...",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            )
        }
        LazyColumn {
            items(filteredCryptos.value) { crypto ->
                CryptoItem(crypto, navController)
            }
        }
    }

}

@Composable
fun SearchInputField(value: String, onValueChange: (String) -> Unit, onSearchClick: () -> Unit) {
    Row {
        TextField(
            value = value,
            onValueChange = onValueChange,
            label = { Text("Search Crypto") },
            modifier = Modifier
                .fillMaxWidth(0.75f)
                .padding(16.dp)
        )
        Button(
            onClick = onSearchClick,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Icon(Icons.Filled.Search, contentDescription = "Search")
        }
    }
}

@Composable
fun CryptoItem(crypto: CoinData, navController: NavController) {
    Text(
        text = "${crypto.symbol} - ${crypto.name}",
        modifier = Modifier
            .padding(16.dp)
            .clickable {
                navController.navigate("cryptoDetail/${crypto.id}")
            }
    )
}


