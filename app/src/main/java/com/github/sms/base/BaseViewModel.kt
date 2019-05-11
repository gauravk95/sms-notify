package com.github.sms.base

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.github.sms.data.source.repository.AppDataSource
import com.github.sms.utils.rx.SchedulerProvider
import io.reactivex.disposables.CompositeDisposable

abstract class BaseViewModel(protected val dataSource: AppDataSource,
                             protected val schedulerProvider: SchedulerProvider,
                             protected val compositeDisposable: CompositeDisposable) : ViewModel() {

    private val isLoading: MutableLiveData<Boolean> = MutableLiveData()
    private val errorMsg: MutableLiveData<Int> = MutableLiveData()

    override fun onCleared() {
        compositeDisposable.clear()
        compositeDisposable.dispose()
        super.onCleared()
    }

    fun isLoading() = isLoading

    fun getErrorMsg() = errorMsg

}