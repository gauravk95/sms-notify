package com.github.sms.ui.main

import android.arch.lifecycle.MutableLiveData
import com.github.sms.R
import com.github.sms.base.BaseViewModel
import com.github.sms.data.models.local.SmsItem
import com.github.sms.data.source.repository.AppDataSource
import com.github.sms.utils.rx.SchedulerProvider
import io.reactivex.disposables.CompositeDisposable

/**
 * ViewModel for the [MainActivity] screen.
 * The ViewModel works with the [AppDataSource] to get the data.
 */
class MainViewModel constructor(appRepository: AppDataSource,
                                schedulerProvider: SchedulerProvider,
                                compositeDisposable: CompositeDisposable) :
        BaseViewModel(appRepository, schedulerProvider, compositeDisposable) {

    val itemList: MutableLiveData<List<SmsItem>> = MutableLiveData()

    /**
     * Loads the sms
     */
    fun loadSmsList() {
        isLoading().value = true

        val disposable = dataSource.getItemList()
                .subscribeOn(schedulerProvider.io)
                .observeOn(schedulerProvider.ui)
                .subscribe({
                    isLoading().value = false
                    itemList.value = it
                }, {
                    isLoading().value = false
                    getErrorMsg().value = R.string.default_error_message
                })

        compositeDisposable.add(disposable)
    }

}