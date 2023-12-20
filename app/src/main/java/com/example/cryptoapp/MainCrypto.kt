package com.example.cryptoapp

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.cryptoapp.persistence.cache.CoinEntity
import java.text.DecimalFormat
import kotlin.math.abs


@Composable
fun CryptoListScreen(viewModel: MainCryptoViewModel) {
    val cryptoList by viewModel.cryptoList.collectAsState()
    Column {
        CryptoListHeader()
        Divider()
        LazyColumn {
            items(cryptoList) { cryptoData ->
                CryptoItem(crypto = cryptoData)
                Divider()
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
        Spacer(modifier = Modifier.width(8.dp)) // For spacing before the 'Detail' and 'Trade' buttons
    }
}


@Composable
fun CryptoItem(crypto: CoinEntity) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {

        Column(modifier = Modifier.weight(1.3f)) {
            Text(crypto.name)
            Text(crypto.symbol, style = MaterialTheme.typography.labelSmall)
        }
        Text(
            "${crypto.price?.toPriceFormat()}",
            modifier = Modifier.weight(1.5f),
            textAlign = TextAlign.End
        )
        Text(
            "${crypto.percentChange24h?.toPercentageChangeFormat()}",
            modifier = Modifier.weight(1.1f),
            textAlign = TextAlign.End,
            color = if (crypto.percentChange24h ?: 0.0 >= 0) Color.Green else Color.Red
        )

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

@Composable
fun Double.toAbbreviatedNumber(): String {
    return when {
        abs(this) >= 1_000_000_000 -> DecimalFormat("$#,##0.0B").format(this / 1_000_000_000.0)
        abs(this) >= 1_000_000 -> DecimalFormat("$#,##0.0M").format(this / 1_000_000.0)
        abs(this) >= 1_000 -> DecimalFormat("$#,##0.0K").format(this / 1_000.0)
        else -> DecimalFormat("$#,##0.00").format(this)
    }
}


