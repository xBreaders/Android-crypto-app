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

/**
 * Test class for [SearchCryptoViewModel] to verify search functionalities.
 *
 * This class tests the [SearchCryptoViewModel] by mocking the [DefaultCoinRepository] dependency.
 * It uses Kotlin coroutines for testing asynchronous operations and verifies different search scenarios.
 *
 * Annotations:
 * - [ExperimentalCoroutinesApi]: Marks the usage of experimental API in Kotlin coroutines.
 * - [RunWith]: MockitoJUnitRunner to enable Mockito's annotations.
 *
 * @property mockRepository Mocked instance of [DefaultCoinRepository].
 * @property viewModel Instance of [SearchCryptoViewModel] under test.
 * @property testDispatcher Dispatcher used for coroutine execution in test scope.
 * @property testScope Coroutine scope for launching jobs in tests.
 */
@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class SearchCryptoViewModelTest {

    @Mock
    private lateinit var mockRepository: DefaultCoinRepository

    private lateinit var viewModel: SearchCryptoViewModel
    private val testDispatcher = StandardTestDispatcher()
    private val testScope = TestScope()

    /**
     * Sets up the environment for each test. This includes initializing mocks and setting up
     * the test dispatcher and ViewModel.
     */
    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        Dispatchers.setMain(testDispatcher)
        viewModel = SearchCryptoViewModel(mockRepository)
    }

    /**
     * Cleans up after each test. This typically involves resetting the main coroutine dispatcher.
     */
    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    /**
     * Tests that the ViewModel handles empty search results correctly. Verifies that the loading
     * state is handled and the search results are empty.
     */
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

        assertTrue(viewModel.searchResults.first().isEmpty())
    }

    /**
     * Tests that the ViewModel handles exceptions during search correctly. Verifies that the loading
     * state is updated and the search results are empty when an exception occurs.
     */
    @Test
    fun `searchCrypto handles exceptions correctly`() = runTest(testDispatcher) {
        `when`(mockRepository.getCoinByQuery(anyString())).thenReturn(flowOf(emptyList()))
        `when`(mockRepository.requestCoinBySymbol(anyString())).thenThrow(RuntimeException("Error"))

        val loadingStates = mutableListOf<Boolean>()

        testScope.launch {
            viewModel.isLoading.collect { loadingStates.add(it) }
        }

        viewModel.searchCrypto("bitcoin")
        testScope.runCurrent()

        viewModel.searchResults.first() // Wait for search results
        assertFalse(loadingStates.last())

        assertTrue(viewModel.searchResults.first().isEmpty())
    }
}