package com.example.cryptoapp

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cryptoapp.API.CryptoEntity
import com.example.cryptoapp.API.RetrofitInstance
import kotlinx.coroutines.launch

class MainCryptoViewModel : ViewModel() {
    val cryptoList = mutableStateListOf<CryptoEntity>()

    init {
        viewModelScope.launch {
            val response = RetrofitInstance.api.getListOfCryptos()
            if (response.isSuccessful && response.body() != null) {
                cryptoList.addAll(response.body()!!)
            }
        }
    }
}