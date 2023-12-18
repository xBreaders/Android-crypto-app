package com.example.cryptoapp.API

data class CryptoEntity (
    val id: String,
    val name: String,
    val symbol: String,
    val quote: Map<String, Quote>
    )

data class Quote(
    val price: Double,
    val volume_24h: Double,
    // Other fields...
)