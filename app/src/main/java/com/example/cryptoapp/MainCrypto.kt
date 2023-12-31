package com.example.cryptoapp

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.cryptoapp.persistence.api.CoinData
import java.text.DecimalFormat


@Composable
fun CryptoListScreen(
    navController: NavHostController,
    viewModel: MainCryptoViewModel = viewModel(factory = MainCryptoViewModel.Factory)
) {
    val pagedCoins = viewModel.pagedCoins.collectAsLazyPagingItems()

    Column {
        CryptoListHeader()
        Divider()

        LazyColumn {
            if (pagedCoins.loadState.refresh == LoadState.Loading) {
                item {
                    Text(
                        text = "Waiting for items to load from the database",
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentWidth(Alignment.CenterHorizontally)
                    )
                }
            }

            items(count = pagedCoins.itemCount) { index ->
                val cryptoData = pagedCoins[index]
                cryptoData?.let {
                    AnimatedCryptoItem(crypto = it) {
                        navController.navigate("cryptoDetail/${it.id}")
                    }
                    Divider()
                }

            }
        }

    }
}


@Composable
fun CryptoListHeader() {
    Surface(
        color = MaterialTheme.colorScheme.primary,
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("Name", style = MaterialTheme.typography.titleMedium)
            Text("Price (USD)", style = MaterialTheme.typography.titleMedium)
            Text("Change (24H)", style = MaterialTheme.typography.titleMedium)
        }
    }
}

@Composable
fun AnimatedCryptoItem(crypto: CoinData, onClick: () -> Unit) {
    val clicked by remember { mutableStateOf(false) }
    val backgroundColor by animateColorAsState(
        targetValue = if (clicked) MaterialTheme.colorScheme.surfaceVariant else MaterialTheme.colorScheme.surface,
        animationSpec = tween(durationMillis = 300), label = ""
    )

    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .background(backgroundColor)
            .padding(horizontal = 8.dp, vertical = 4.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 4.dp)

    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(all = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1.3f)) {
                Text(
                    text = crypto.name,
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    crypto.symbol,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
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
