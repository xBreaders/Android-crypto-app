package com.example.cryptoapp.coin


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.cryptoapp.persistence.api.CoinData

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CryptoDetailScreen(
    navController: NavHostController,
    cryptoId: Int,
    vm: CoinDetailsViewModel = viewModel(factory = CoinDetailsViewModel.Factory(cryptoId))
) {

    val uiState by vm.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    if (!uiState.isLoading && uiState.coinDetails != null) {
                        Text(uiState.coinDetails!!.symbol, fontWeight = FontWeight.Bold)
                    } else {
                        Text("Loading...", fontWeight = FontWeight.Bold)
                    }
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = MaterialTheme.colorScheme.onSurface
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
                .padding(horizontal = 14.dp, vertical = 10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (!uiState.isLoading && uiState.coinDetails != null) {
                PriceIndicator(uiState.coinDetails!!)
            }
            Row(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) {
                if (uiState.isLoading) {
                    CircularProgressIndicator()
                } else if (uiState.error != null) {
                    Text("Error: ${uiState.error}", color = Color.Red)
                } else if (uiState.historicalData.isNotEmpty()) {
                    CoinChartComposable(uiState.historicalData)
                }
            }

            Row {
                uiState.coinDetails?.let { CryptoDetailCard(it) }
            }
        }
    }
}

@Composable
fun PriceIndicator(coinData: CoinData) {
    val coinDetails = coinData.quote["USD"]
    val priceChange = coinDetails?.percent_change_24h ?: 0.0
    val arrowIcon =
        if (priceChange >= 0) Icons.Filled.KeyboardArrowUp else Icons.Filled.KeyboardArrowDown
    val arrowColor = if (priceChange >= 0) Color.Green else Color.Red

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "$${coinDetails?.price}",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = "${priceChange}%",
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Medium,
            color = arrowColor
        )

        Icon(
            imageVector = arrowIcon,
            contentDescription = if (priceChange >= 0) "Up" else "Down",
            tint = arrowColor
        )
    }
    Spacer(modifier = Modifier.height(16.dp))
}

@Composable
fun CryptoDetailCard(coinData: CoinData) {
    val coinDetails = coinData.quote["USD"]
    var selectedTabIndex by remember { mutableIntStateOf(0) }

    val tabTitles = listOf("General Info", "Market Stats", "Supply Details")

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 20.dp),
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer)
    ) {
        Column {
            // TabRow for tab titles
            TabRow(selectedTabIndex = selectedTabIndex) {
                tabTitles.forEachIndexed { index, title ->
                    Tab(
                        selected = index == selectedTabIndex,
                        onClick = { selectedTabIndex = index },
                        text = { Text(title) }
                    )
                }
            }

            // LazyColumn content changes based on selected tab
            LazyColumn(modifier = Modifier.padding(10.dp)) {
                when (selectedTabIndex) {
                    0 -> { // General Info
                        item { DetailItem(label = "Symbol", value = coinData.symbol) }
                        item {
                            DetailItem(
                                label = "Last Updated",
                                value = coinData.last_updated.split("T")[1]
                            )
                        }
                        item {
                            DetailItem(
                                label = "Date Created",
                                value = coinData.date_added.split("T")[0]
                            )
                        }
                    }

                    1 -> { // Market Stats
                        item {
                            DetailItem(
                                label = "Current Price",
                                value = "${coinDetails?.price} USD"
                            )
                        }
                        item {
                            DetailItem(
                                label = "24h Change",
                                value = "${coinDetails?.percent_change_24h}%",
                                color = getColorForChange(coinDetails?.percent_change_24h)
                            )
                        }
                        item {
                            DetailItem(
                                label = "Market Cap",
                                value = "${coinDetails?.market_cap} USD"
                            )
                        }
                        item {
                            DetailItem(
                                label = "Volume",
                                value = "${coinDetails?.volume_24h} USD"
                            )
                        }
                    }

                    2 -> { // Supply Details
                        item {
                            DetailItem(
                                label = "Circulating Supply",
                                value = "${coinData.circulating_supply}"
                            )
                        }
                        item {
                            DetailItem(
                                label = "Total Supply",
                                value = "${coinData.total_supply}"
                            )
                        }
                        item {
                            DetailItem(
                                label = "Max Supply",
                                value = "${coinData.max_supply ?: "-"}"
                            )
                        }
                        item {
                            DetailItem(
                                label = "Number of Market Pairs",
                                value = "${coinData.num_market_pairs}"
                            )
                        }
                    }
                }
            }
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
