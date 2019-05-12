package com.github.sms.ui.main

import android.arch.core.executor.testing.InstantTaskExecutorRule
import com.github.sms.utils.LiveDataTestUtil
import com.github.sms.data.models.local.SmsItem
import com.github.sms.data.source.repository.AppDataSource
import com.github.sms.utils.DataGenerator
import com.github.sms.utils.rx.TestSchedulerProvider
import io.reactivex.disposables.CompositeDisposable
import junit.framework.TestCase.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.MockitoAnnotations

import org.mockito.junit.MockitoJUnitRunner

/**
 * Unit test for MainViewModel
 *
 * Created by gk
 */
@RunWith(MockitoJUnitRunner::class)
class MainViewModelTest {

    // Executes each task synchronously using Architecture Components.
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @Mock
    internal lateinit var mockAppRepository: AppDataSource

    private lateinit var mainViewModel: MainViewModel

    private lateinit var dummyList: List<SmsItem>

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        dummyList = DataGenerator.getDummySmsList(30)
        mainViewModel = MainViewModel(mockAppRepository, TestSchedulerProvider(), CompositeDisposable())
    }

    @Test
    fun initialLoad_ItemsAvailable_showItems() {
        //TODO: Add test
        fail("Not Implemented")
    }

    @Test
    fun initialLoad_ItemsUnavailable_showEmpty() {
        //TODO: Add test
        fail("Not Implemented")
    }

    @Test
    fun reload_ItemsAvailable_showItems() {
        //TODO: Add test
        fail("Not Implemented")
    }

    @Test
    fun reload_ItemsUnavailable_showEmpty() {
        //TODO: Add test
        fail("Not Implemented")
    }

    @Test
    fun updateHighlight_MessageTimestampAvailable_showHighlight() {
        assertNull(LiveDataTestUtil.getValue(mainViewModel.highlightMessageTimestamp))
        mainViewModel.updateHighlight(Long.MAX_VALUE)
        assertEquals(LiveDataTestUtil.getValue(mainViewModel.highlightMessageTimestamp), Long.MAX_VALUE)
    }

    @Test
    fun updateHighlight_MessageTimestampUnavailable_noHighlight() {
        assertNull(LiveDataTestUtil.getValue(mainViewModel.highlightMessageTimestamp))
        mainViewModel.updateHighlight(null)
        assertNull(LiveDataTestUtil.getValue(mainViewModel.highlightMessageTimestamp))
    }
}