package com.example.cryptoapp.coin

import androidx.compose.runtime.Composable

@Composable
fun SearchCryptoScreen(viewModel: SearchCryptoViewModel = viewModel()) {
    var searchText by remember { mutableStateOf("") }

    Column {
        SearchInputField(searchText) { newText ->
            searchText = newText
            viewModel.searchCrypto(searchText)
        }

        val filteredCryptos = viewModel.searchResults.collectAsState(initial = emptyList())

        LazyColumn {
            items(filteredCryptos.value) { crypto ->
                CryptoItem(crypto)
            }
        }
    }
}

@Composable
fun SearchInputField(value: String, onValueChange: (String) -> Unit) {
    TextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text("Search Crypto") },
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    )
}

@Composable
fun CryptoItem(crypto: CoinEntity) {
    Text(text = "${crypto.symbol} - ${crypto.name}", modifier = Modifier.padding(16.dp))
}


@Composable
fun SearchInputField(value: String, onValueChange: (String) -> Unit) {
    TextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text("Search Crypto") },
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    )
}

@Composable
fun CryptoItem(crypto: CryptoCurrency) {
    Text(text = "${crypto.symbol} - ${crypto.name}", modifier = Modifier.padding(16.dp))
}