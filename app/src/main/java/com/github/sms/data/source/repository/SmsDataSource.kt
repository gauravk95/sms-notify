/*
    Copyright 2019 Gaurav Kumar

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
*/
package com.github.sms.data.source.repository

import android.arch.lifecycle.MutableLiveData
import android.arch.paging.PageKeyedDataSource
import com.github.sms.data.models.local.SmsHolder
import com.github.sms.data.source.state.LoadingState

import io.reactivex.disposables.CompositeDisposable

/**
 * Sms Data Source for Paged Loading
 *
 * Created by gk
 */
class SmsDataSource constructor(
        private val appDataSource: AppDataSource,
        private val compositeDisposable: CompositeDisposable) : PageKeyedDataSource<Int, SmsHolder>() {

    val loadingState: MutableLiveData<LoadingState> = MutableLiveData()

    override fun loadInitial(params: LoadInitialParams<Int>, callback: LoadInitialCallback<Int, SmsHolder>) {
        loadingState.postValue(LoadingState.FIRST_LOADING)
        val disposable = appDataSource.getPagedSmsItemList(1, params.requestedLoadSize)
                .subscribe({
                    if (!it.isNullOrEmpty()) {
                        loadingState.postValue(LoadingState.FIRST_LOADED)
                        callback.onResult(it, null, 2)
                    } else
                        loadingState.postValue(LoadingState.FIRST_EMPTY)
                }, {
                    loadingState.postValue(LoadingState.FIRST_FAILED)
                })
        compositeDisposable.add(disposable)
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, SmsHolder>) {
        loadingState.postValue(LoadingState.LOADING)
        val disposable = appDataSource.getPagedSmsItemList(params.key, params.requestedLoadSize)
                .subscribe({
                    loadingState.postValue(LoadingState.LOADED)

                    //if, calculated page and current page are same,return null to stop fetching data
                    //else, get the next page
                    val isPaginationEnd = it.isNullOrEmpty()
                    val nextKey = (if (isPaginationEnd) null else params.key + 1)

                    callback.onResult(it, nextKey)
                }, {
                    loadingState.postValue(LoadingState.FAILED)
                })
        compositeDisposable.add(disposable)
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, SmsHolder>) {
        //do something
    }

    fun reset() {
        (appDataSource as AppDataRepository).invalidateCache()
        loadingState.postValue(LoadingState.RESET)
        invalidate()
    }

}
