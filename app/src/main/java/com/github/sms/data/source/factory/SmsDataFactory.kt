package com.github.sms.data.source.factory

import android.arch.lifecycle.MutableLiveData
import android.arch.paging.DataSource
import com.github.sms.data.models.local.SmsHolder
import com.github.sms.data.source.repository.AppDataSource
import com.github.sms.data.source.repository.SmsDataSource
import io.reactivex.disposables.CompositeDisposable

class SmsDataFactory(private val appDataSource: AppDataSource,
                     private val compositeDisposable: CompositeDisposable) : DataSource.Factory<Int, SmsHolder>() {

    val mutableLiveData: MutableLiveData<SmsDataSource> = MutableLiveData()

    override fun create(): DataSource<Int, SmsHolder> {
        val dataSource = SmsDataSource(appDataSource, compositeDisposable)
        mutableLiveData.postValue(dataSource)
        return dataSource
    }

    fun reset() {
        mutableLiveData.value?.reset()
    }
}