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

import android.support.annotation.VisibleForTesting
import com.github.sms.data.models.local.SmsHolder

import com.github.sms.data.source.prefs.Preferences
import com.github.sms.utils.AppConstants

import javax.inject.Inject
import javax.inject.Singleton

import io.reactivex.Flowable
import javax.inject.Named

/**
 * The central point to communicate to different data sources like DB, SERVER, SHARED PREFS
 *
 * Created by gk
 */

@Singleton
class AppDataRepository @Inject
constructor(@Named(AppConstants.NAMED_LOCAL) private val localAppDataSource: AppDataSource,
            private val preference: Preferences) : AppDataSource {

    @VisibleForTesting
    internal var cachedItemList: List<SmsHolder>? = null

    /**
     * Marks the cache as invalid, to force an update the next time data is requested. This variable
     * has package local visibility so it can be accessed from tests.
     */
    @VisibleForTesting
    internal var cacheIsDirty = false

    //get the elements from local db, and if empty get it from sever
    private fun getSmsFromLocalSource(): Flowable<List<SmsHolder>> {
        return localAppDataSource
                .getSmsItemList(false)
                .doOnNext {
                    cachedItemList = it
                    cacheIsDirty = false
                }
    }

    override fun getSmsItemList(forceRefresh: Boolean): Flowable<List<SmsHolder>> {
        //force cache invalid
        if (forceRefresh) cacheIsDirty = true

        // Respond immediately with cache if available and not dirty
        if (cachedItemList != null && !cacheIsDirty) {
            return Flowable.just(cachedItemList)
        }

        //if cache is dirty, get the data from local
        return getSmsFromLocalSource()
    }

    override fun getPagedSmsItemList(page: Int, pageSize: Int): Flowable<List<SmsHolder>> {
        return getSmsItemList(false)
                .map {
                    val fromIndex = (page - 1) * pageSize
                    val endIndex = (page) * pageSize
                    it.subList(fromIndex, endIndex)
                }
    }

    fun invalidateCache() {
        cacheIsDirty = true
    }
}
