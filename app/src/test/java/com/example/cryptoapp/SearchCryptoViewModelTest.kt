package com.example.cryptoapp

import com.example.cryptoapp.persistence.api.DefaultCoinRepository
import com.example.cryptoapp.ui.searchpage.SearchCryptoViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.anyString
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class SearchCryptoViewModelTest {

    @Mock
    private lateinit var mockRepository: DefaultCoinRepository

    private lateinit var viewModel: SearchCryptoViewModel
    private val testDispatcher = StandardTestDispatcher()
    private val testScope = TestScope()

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        Dispatchers.setMain(testDispatcher)
        viewModel = SearchCryptoViewModel(mockRepository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `searchCrypto handles empty results correctly`() = runTest(testDispatcher) {
        `when`(mockRepository.getCoinByQuery(anyString())).thenReturn(flowOf(emptyList()))
        val loadingStates = mutableListOf<Boolean>()


        testScope.launch {
            viewModel.isLoading.collect { loadingStates.add(it) }
        }

        viewModel.searchCrypto("emptyQuery")
        testScope.runCurrent()

        viewModel.searchResults.first() // Wait for search results
        assertFalse(loadingStates.last())

        // Verify that search results are empty
        assertTrue(viewModel.searchResults.first().isEmpty())
    }

    @Test
    fun `searchCrypto handles exceptions correctly`() = runTest(testDispatcher) {
        `when`(mockRepository.getCoinByQuery(anyString())).thenReturn(
            flowOf(
                emptyList()
            )
        )
        `when`(mockRepository.requestCoinBySymbol(anyString())).thenThrow(RuntimeException("Error"))

        val loadingStates = mutableListOf<Boolean>()

        testScope.launch {
            viewModel.isLoading.collect { loadingStates.add(it) }
        }

        viewModel.searchCrypto("bitcoin")
        testScope.runCurrent()

        viewModel.searchResults.first() // Wait for search results
        assertFalse(loadingStates.last())


        // Verify that search results are empty after an exception
        assertTrue(viewModel.searchResults.first().isEmpty())
    }
}