package com.example.cryptoapp

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Divider
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.cryptoapp.persistence.api.CoinData
import java.text.DecimalFormat

@Composable
fun CryptoListScreen(navController: NavHostController, viewModel: MainCryptoViewModel) {
    val uiState by viewModel.uiState.collectAsState()

    val sortedCryptoList = uiState.coinData.sortedBy { it.cmc_rank }

    Column {
        CryptoListHeader()
        Divider()
        LazyColumn {
            when (viewModel.apiState) {
                is MainCryptoStatus.Loading -> {
                    item {
                        Text(
                            "Loading...",
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            textAlign = TextAlign.Center
                        )
                    }
                }

                is MainCryptoStatus.Error -> {
                    item {
                        Text(
                            "Error: ${(viewModel.apiState as MainCryptoStatus.Error).message}",
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            textAlign = TextAlign.Center
                        )
                    }
                }

                else -> {
                    items(sortedCryptoList, key = { it.id }) { cryptoData ->
                        AnimatedCryptoItem(crypto = cryptoData) {
                            navController.navigate("cryptoDetail/${cryptoData.name}")
                        }
                        Divider()
                    }
                }
            }

        }
    }
}

@Composable
fun CryptoListHeader() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Text("Name", modifier = Modifier.weight(2f))
        Text("Price (USD)", modifier = Modifier.weight(1f), textAlign = TextAlign.End)
        Text("Change (24H)", modifier = Modifier.weight(1.1f), textAlign = TextAlign.End)
        Spacer(modifier = Modifier.width(8.dp))
    }
}

@Composable
fun AnimatedCryptoItem(crypto: CoinData, onClick: () -> Unit) {
    var clicked by remember { mutableStateOf(false) }
    // val backgroundColor by animateColorAsState(
    //    targetValue = if (clicked) Color.LightGray else MaterialTheme.colorScheme.surface,
    //    animationSpec = tween(durationMillis = 100))

    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            // .background(backgroundColor)
            .clickable {
                clicked = true
                onClick()
            }
            .padding(horizontal = 16.dp, vertical = 8.dp),
        // colors = CardDefaults.cardColors(containerColor = backgroundColor)
    ) {
        // Use LaunchedEffect here


        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1.3f)) {
                Text(crypto.name)
                Text(crypto.symbol, style = MaterialTheme.typography.labelSmall)
            }
            Text(
                crypto.quote["USD"]?.price!!.toPriceFormat(),
                modifier = Modifier.weight(1.5f),
                textAlign = TextAlign.End
            )
            Text(
                crypto.quote["USD"]?.percent_change_24h!!.toPercentageChangeFormat(),
                modifier = Modifier.weight(1.1f),
                textAlign = TextAlign.End,
                color = if (crypto.quote["USD"]!!.percent_change_24h >= 0) Color.Green else Color.Red
            )
        }
    }
}


@Composable
fun Double.toPriceFormat(): String {
    return DecimalFormat("$#,##0.00").format(this)
}

@Composable
fun Double.toPercentageChangeFormat(): String {
    return DecimalFormat("0.00%").format(this / 100)
}
