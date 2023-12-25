package com.example.cryptoapp

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.cryptoapp.persistence.api.CoinData

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CryptoDetailScreen(
    navController: NavHostController,
    cryptoId: Int,
    vm: CryptoDetailsViewModel
) {
    // Fetch details when the screen is first displayed
    LaunchedEffect(cryptoId) {
        vm.fetchCoinDetails(cryptoId)
    }

    val uiState by vm.uiState.collectAsState()


    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(uiState.coinDetails.name, fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                colors = topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CryptoDetailCard(uiState.coinDetails)
        }
    }
}

@Composable
fun CryptoDetailCard(coinData: CoinData) {
    val coinDetails = coinData.quote["USD"]

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        LazyColumn(modifier = Modifier.padding(16.dp)) {
            item {
                Text(
                    text = "${coinData.name} Details",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp)) // Reduced height for less clutter
            }
            item { DetailItem(label = "Symbol", value = coinData.symbol) }
            item { DetailItem(label = "Current Price", value = "${coinDetails?.price} USD") }
            item {
                DetailItem(
                    label = "24h Change",
                    value = "${coinDetails?.volume_change_24h}%",
                    color = getColorForChange(coinDetails?.volume_change_24h)
                )
            }
            item { DetailItem(label = "Market Cap", value = "${coinDetails?.market_cap} USD") }
            item { DetailItem(label = "Volume", value = "${coinDetails?.volume_24h} USD") }
            item {
                DetailItem(
                    label = "FD Market Cap",
                    value = "${coinDetails?.fully_diluted_market_cap} USD"
                )
            }
            item {
                DetailItem(
                    label = "Market Cap Dominance",
                    value = "${coinDetails?.market_cap_dominance}%",
                    color = getColorForChange(coinDetails?.market_cap_dominance)
                )
            }
            item {
                DetailItem(
                    label = "Circulating Supply",
                    value = "${coinData.circulating_supply}"
                )
            }
            item { DetailItem(label = "Total Supply", value = "${coinData.total_supply}") }
            item { DetailItem(label = "Max Supply", value = "${coinData.max_supply ?: "-"}") }
            item {
                DetailItem(
                    label = "Number of Market Pairs",
                    value = "${coinData.num_market_pairs}"
                )
            }
            item { DetailItem(label = "Last Updated", value = coinData.last_updated) }
            item { DetailItem(label = "Date Added", value = coinData.date_added) }
        }
    }
}

@Composable
fun DetailItem(label: String, value: String, color: Color = Color.Unspecified) {


    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Medium,
            color = color
        )
    }
    Spacer(modifier = Modifier.height(8.dp))
}

@Composable
fun getColorForChange(change: Double?): Color {
    return when {
        change == null -> Color.Unspecified
        change >= 0 -> Color.Green
        else -> Color.Red
    }
}
