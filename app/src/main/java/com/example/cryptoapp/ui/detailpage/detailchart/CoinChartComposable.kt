package com.example.cryptoapp.ui.detailpage.detailchart

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.cryptoapp.persistence.api.KLine

import com.patrykandpatrick.vico.compose.axis.vertical.rememberStartAxis
import com.patrykandpatrick.vico.compose.chart.CartesianChartHost
import com.patrykandpatrick.vico.compose.chart.layer.lineSpec
import com.patrykandpatrick.vico.compose.chart.layer.rememberLineCartesianLayer
import com.patrykandpatrick.vico.compose.chart.rememberCartesianChart
import com.patrykandpatrick.vico.compose.component.shape.shader.color
import com.patrykandpatrick.vico.compose.component.shape.shader.verticalGradient
import com.patrykandpatrick.vico.compose.m3.style.m3ChartStyle
import com.patrykandpatrick.vico.compose.style.ProvideChartStyle
import com.patrykandpatrick.vico.core.axis.AxisItemPlacer
import com.patrykandpatrick.vico.core.chart.values.AxisValueOverrider
import com.patrykandpatrick.vico.core.component.shape.shader.DynamicShaders
import com.patrykandpatrick.vico.core.extension.round
import com.patrykandpatrick.vico.core.model.CartesianChartModelProducer
import com.patrykandpatrick.vico.core.model.LineCartesianLayerModel
import com.patrykandpatrick.vico.core.model.lineSeries
import java.text.DateFormat
import java.util.Date
import java.util.Locale

/**
 * Composable function to display a chart for a given list of `KLine` data points.
 *
 * This composable is responsible for creating the chart model producer, setting up the chart style, configuring chart's y-axis and rendering the chart.
 *
 * @param klines The list of KLine objects representing the cryptocurrency data points to be plotted on the chart.
 */
@Composable
fun CoinChartComposable(klines: List<KLine>) {
    ProvideChartStyle(
        m3ChartStyle()
    )
    {

        val chartModelProducer = remember {
            CartesianChartModelProducer.build {
                lineSeries {
                    series(y = klines.map { it.close }, x = klines.map { it.closeTime })
                }
            }
        }


        remember {
            object : AxisValueOverrider<LineCartesianLayerModel> {
                override fun getMaxY(model: LineCartesianLayerModel) = model.maxY + model.maxY % 2
            }
        }


        val axisPercentageOverride = 0.010f //10%
        val marker = rememberMarker()
        ElevatedCard(
            shape = RoundedCornerShape(8.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer),
        ) {
            Column {
                val yellow = Color(0xFFFFAA4A)
                val pink = Color(0xFFFF4AAA)
                CartesianChartHost(
                    modelProducer = chartModelProducer,
                    chart = rememberCartesianChart(
                        rememberLineCartesianLayer(
                            listOf(
                                lineSpec(
                                    shader = DynamicShaders.color(yellow),
                                    backgroundShader =
                                    verticalGradient(
                                        arrayOf(yellow.copy(alpha = 0.5f), yellow.copy(alpha = 0f)),
                                    ),
                                ),
                                lineSpec(
                                    shader = DynamicShaders.color(pink),
                                    backgroundShader =
                                    verticalGradient(
                                        arrayOf(pink.copy(alpha = 0.5f), pink.copy(alpha = 0f)),
                                    ),
                                ),
                            ),
                            spacing = 0.3.dp,
                            axisValueOverrider = AxisValueOverrider.fixed(
                                minY = if ((klines.minOf { it.close.toFloat() } * (1 - axisPercentageOverride)).round == 0f) klines.minOf { it.close.toFloat() } else (klines.minOf { it.close.toFloat() } * (1 - axisPercentageOverride)),
                                maxY = if ((klines.maxOf { it.close.toFloat() } * (1 + axisPercentageOverride)).round == 0f) klines.maxOf { it.close.toFloat() } else (klines.maxOf { it.close.toFloat() } * (1 + axisPercentageOverride)),
                            )

                        ),

                        startAxis = rememberStartAxis(itemPlacer = remember {
                            AxisItemPlacer.Vertical.default(
                                maxItemCount = { 5 })
                        }),
                    ),
                    marker = marker
                )
            }
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "X(0): " + DateFormat.getDateTimeInstance(
                        DateFormat.SHORT,
                        DateFormat.SHORT,
                        Locale.GERMAN
                    ).format(Date(klines.minOf { it.openTime })))
                Text(
                    text = "X(Current) " + DateFormat.getDateTimeInstance(
                        DateFormat.SHORT,
                        DateFormat.SHORT,
                        Locale.GERMAN
                    ).format(Date(klines.maxOf { it.closeTime }))
                )
            }

        }
    }
}


