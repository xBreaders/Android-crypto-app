package com.example.cryptoapp

import android.app.Application
import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.paging.PagingData
import androidx.test.core.app.ApplicationProvider
import androidx.work.Configuration
import androidx.work.WorkInfo
import androidx.work.WorkManager
import androidx.work.testing.WorkManagerTestInitHelper
import com.example.cryptoapp.persistence.api.DefaultCoinRepository
import com.example.cryptoapp.ui.mainpage.MainCryptoViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import org.robolectric.RobolectricTestRunner


/**
 * Test class for [MainCryptoViewModel] to verify the correct scheduling of work and
 * other ViewModel functionalities.
 *
 * This class tests the [MainCryptoViewModel] by mocking its dependencies and using
 * Robolectric to simulate Android framework components.
 *
 * Annotations:
 * - [RunWith]: RobolectricTestRunner to enable testing of Android framework code.
 * - [ExperimentalCoroutinesApi]: Marks the usage of experimental API in Kotlin coroutines.
 * - [Rule]: JUnit rule for managing threading and architecture components.
 *
 * @property instantExecutorRule Ensures that architecture components execute tasks synchronously.
 * @property mockRepository Mocked instance of [DefaultCoinRepository].
 * @property context Mocked instance of [Context] for the ViewModel.
 * @property viewModel Instance of [MainCryptoViewModel] under test.
 */
@RunWith(RobolectricTestRunner::class)
@ExperimentalCoroutinesApi
class MainCryptoViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var mockRepository: DefaultCoinRepository

    @Mock
    private lateinit var context: Context

    private lateinit var viewModel: MainCryptoViewModel

    /**
     * Sets up the environment for each test. This includes initializing mocks and the WorkManager
     * for testing purposes, and creating a new instance of the ViewModel.
     */
    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        context = ApplicationProvider.getApplicationContext()

        val config = Configuration.Builder()
            .setMinimumLoggingLevel(android.util.Log.DEBUG)
            .build()

        WorkManagerTestInitHelper.initializeTestWorkManager(context, config)
    }

    /**
     * Verifies that the ViewModel schedules work correctly using WorkManager. It asserts that
     * the work is enqueued and in the correct state.
     */
    @Test
    fun `scheduleAndObserveWorker schedules work correctly`() {
        Mockito.`when`(mockRepository.getPagedCoins()).thenReturn(
            flowOf(PagingData.empty())
        )

        viewModel = MainCryptoViewModel(mockRepository, context.applicationContext as Application)

        val workManager = WorkManager.getInstance(ApplicationProvider.getApplicationContext())

        val workInfos = workManager.getWorkInfosForUniqueWork("crypto-sync").get()

        assertTrue(workInfos.isNotEmpty())

        val workInfo = workInfos[0]
        assertThat(workInfo.state, `is`(WorkInfo.State.ENQUEUED))
    }
}