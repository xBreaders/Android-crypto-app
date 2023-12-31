package com.example.cryptoapp

import android.content.Context
import android.net.ConnectivityManager
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.cryptoapp.persistence.api.DefaultCoinRepository
import com.example.cryptoapp.persistence.api.response.CoinData
import com.example.cryptoapp.persistence.api.response.CryptoQuote
import com.example.cryptoapp.ui.detailpage.CoinDetailsState
import com.example.cryptoapp.ui.detailpage.CoinDetailsViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.ArgumentMatchers.anyInt
import org.mockito.Mock
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations


/**
 * A test class for [CoinDetailsViewModel] to ensure that the ViewModel behaves correctly
 * under different scenarios such as successful data fetching, failure to fetch data, and
 * special case handling (e.g., for USDT).
 *
 * The class tests the [CoinDetailsViewModel] by mocking dependencies like [DefaultCoinRepository]
 * and [Context], and uses a [TestScope] with a [UnconfinedTestDispatcher] to execute coroutines
 * in the tests. It verifies the state changes in the ViewModel based on different responses
 * from the repository.
 *
 * Annotations:
 * - [ExperimentalCoroutinesApi]: Marks the usage of experimental API in Kotlin coroutines.
 * - [Rule]: JUnit rule for managing threading and architecture components.
 *
 * @property instantExecutorRule Ensures that the architecture components execute tasks synchronously.
 * @property mockRepository Mocked instance of [DefaultCoinRepository].
 * @property viewModel Instance of [CoinDetailsViewModel] under test.
 * @property testDispatcher Dispatcher used for coroutine execution in test scope.
 * @property context Mocked instance of [Context] for the ViewModel.
 * @property testScope Coroutine scope for launching jobs in tests.
 */
@ExperimentalCoroutinesApi
class CoinDetailsViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var mockRepository: DefaultCoinRepository

    private lateinit var viewModel: CoinDetailsViewModel
    private val testDispatcher = UnconfinedTestDispatcher()
    private lateinit var context: Context
    private val testScope = TestScope()


    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        context = mock(Context::class.java)
        val connectivityManager = mock(ConnectivityManager::class.java)
        `when`(context.getSystemService(Context.CONNECTIVITY_SERVICE)).thenReturn(
            connectivityManager
        )
        Dispatchers.setMain(testDispatcher)
        // viewModel = viewModel(factory = CoinDetailsViewModel.Factory(1))
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    /**
     * Tests the scenario where the ViewModel successfully fetches coin details from the repository.
     * It verifies that the ViewModel's state correctly reflects the successful data retrieval by mocking the roomDB repository.
     */
    @Test
    fun `fetch coin details success`() {

        val coinobject = CoinData(
            id = 1,
            name = "Bitcoin",
            symbol = "BTC",
            slug = "bitcoin",
            cmc_rank = 1,
            num_market_pairs = 1,
            circulating_supply = 1000000.0,
            total_supply = 1000000.0,
            max_supply = 1000000.0,
            percent_change_24h = 0.0,
            infinite_supply = false,
            last_updated = "2021-01-01",
            date_added = "2021-01-01",
            tags = listOf("tag1", "tag2"),
            quote = mapOf(
                "USD" to CryptoQuote(
                    price = 10000.0,
                    volume_24h = 1000000.0,
                    volume_change_24h = 1000000.0,
                    percent_change_24h = 0.0,
                    market_cap = 100000000.0,
                    fully_diluted_market_cap = 100000000.0,
                    market_cap_dominance = 100000000.0,
                    last_updated = "2021-01-01"
                )
            )
        )

        `when`(mockRepository.getCoinById(anyInt())).thenReturn(
            flowOf(
                coinobject
            )
        )
        viewModel = CoinDetailsViewModel(mockRepository, 1, context)

        val state = mutableListOf<CoinDetailsState>()
        testScope.launch {
            viewModel.uiState.toList(state)
        }

        testScope.runCurrent() // Advance the coroutine

        val latestState = state.last()
        assertNotNull(latestState.coinDetails)
        assertEquals(coinobject, latestState.coinDetails)
    }

    /**
     * Tests the scenario where the ViewModel fails to fetch coin details from the repository (non existing coin or empty list).
     * It verifies that the ViewModel's state correctly reflects the failure and contains an appropriate error message.
     */
    @Test
    fun `fetch coin details failure results in appropriate error state`() {

        `when`(mockRepository.getCoinById(anyInt())).thenReturn(flowOf())
        viewModel = CoinDetailsViewModel(mockRepository, 1, context)

        val state = mutableListOf<CoinDetailsState>()
        testScope.launch {
            viewModel.uiState.toList(state)
        }

        testScope.runCurrent() // Advance the coroutine

        val latestState = state.last()
        assertNull(latestState.coinDetails)
        assertEquals("Failed to fetch coin details", latestState.error)
    }

    /**
     * Tests the specific case of fetching coin details for USDT. The ViewModel should recognize
     * this special case and update its state with an error message indicating that charting is not available for USDT.
     */
    @Test
    fun `fetch coin details for USDT results in no charting available error`() {
        val usdtCoinData = CoinData(
            id = 1,
            name = "Bitcoin",
            symbol = "USDT",
            slug = "bitcoin",
            cmc_rank = 1,
            num_market_pairs = 1,
            circulating_supply = 1000000.0,
            total_supply = 1000000.0,
            max_supply = 1000000.0,
            percent_change_24h = 0.0,
            infinite_supply = false,
            last_updated = "2021-01-01",
            date_added = "2021-01-01",
            tags = listOf("tag1", "tag2"),
            quote = mapOf(
                "USD" to CryptoQuote(
                    price = 10000.0,
                    volume_24h = 1000000.0,
                    volume_change_24h = 1000000.0,
                    percent_change_24h = 0.0,
                    market_cap = 100000000.0,
                    fully_diluted_market_cap = 100000000.0,
                    market_cap_dominance = 100000000.0,
                    last_updated = "2021-01-01"
                )
            )
        )
        `when`(mockRepository.getCoinById(anyInt())).thenReturn(flowOf(usdtCoinData))
        viewModel = CoinDetailsViewModel(mockRepository, 1, context)

        val state = mutableListOf<CoinDetailsState>()
        testScope.launch {
            viewModel.uiState.toList(state)
        }

        testScope.runCurrent() // Advance the coroutine

        val latestState = state.last()
        assertEquals("No charting available for USDT", latestState.error)
    }
}
