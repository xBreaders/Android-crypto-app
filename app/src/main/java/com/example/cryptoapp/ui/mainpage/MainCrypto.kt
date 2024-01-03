package com.example.cryptoapp.ui.mainpage

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
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
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.cryptoapp.persistence.api.response.CoinData
import java.text.DecimalFormat

/**
 * `CryptoListScreen` Composable function for rendering the Crypto List Screen in the application.
 *
 * This Composable function is intended to represent a list of cryptocurrencies fetched from Coin Data.
 * Each cryptocurrency within this list is represented by an `AnimatedCryptoItem` Composable.
 *
 * Parameters:
 * @param navController: An instance of `NavHostController` for controlling navigation within the app.
 * @param viewModel: Instance of `MainCryptoViewModel` (with default provided) for supplying data to this screen.
 *
 * Main Components of screen:
 * `CryptoListHeader` - A Composable for displaying the header of the Crypto List.
 * `AnimatedCryptoItem` - A Composable that stands as visual representation of each individual cryptocurrency on the list with animation capabilities.
 * `LoadingItem` - A Composable that serves a loading display when data is being fetched from Coin Data.
 * `ErrorItem` - A Composable that is displayed when there's an error fetching data.
 */
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
            when (pagedCoins.loadState.refresh) {
                is LoadState.Loading -> item { LoadingItem() }
                is LoadState.Error -> item { ErrorItem("Failed to load data") }
                else -> items(pagedCoins.itemCount) { index ->
                    pagedCoins[index]?.let {
                        AnimatedCryptoItem(crypto = it) {
                            navController.navigate("cryptoDetail/${it.id}")
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
    var isClicked by remember { mutableStateOf(false) }
    val backgroundColor by animateColorAsState(
        targetValue = if (isClicked) MaterialTheme.colorScheme.surfaceVariant else MaterialTheme.colorScheme.surface,
        animationSpec = tween(durationMillis = 300), label = ""
    )

    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                isClicked = !isClicked
                onClick()
            }
            .padding(horizontal = 8.dp, vertical = 4.dp),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.elevatedCardColors(containerColor = backgroundColor)
    ) {
        CryptoItemRow(crypto)
    }
}

@Composable
fun CryptoItemRow(crypto: CoinData) {
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

@Composable
fun LoadingItem() {
    Text(
        text = "Waiting for items to load from the database",
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentWidth(Alignment.CenterHorizontally)
    )
}

@Composable
fun ErrorItem(message: String) {
    Text(
        text = "Error: $message",
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentWidth(Alignment.CenterHorizontally),
        color = MaterialTheme.colorScheme.error
    )
}

fun Double.toPriceFormat(): String = DecimalFormat("$#,##0.00").format(this)
fun Double.toPercentageChangeFormat(): String = DecimalFormat("0.00%").format(this / 100)