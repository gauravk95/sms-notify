package com.github.sms.di.factory

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.github.sms.data.source.repository.AppDataSource
import com.github.sms.ui.main.MainViewModel
import com.github.sms.utils.rx.SchedulerProvider
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

/**
 * View Model Factory Maker for [ViewModel] that require AppDataSource
 */
class AppViewModelFactory @Inject
constructor(private val dataSource: AppDataSource,
            private val schedulerProvider: SchedulerProvider,
            private val compositeDisposable: CompositeDisposable) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MainViewModel(dataSource, schedulerProvider, compositeDisposable) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}