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
package com.github.sms.data.source.repository.local

import com.github.sms.data.models.local.SmsItem
import com.github.sms.data.source.db.SmsDao
import com.github.sms.data.source.repository.AppDataSource

import javax.inject.Inject
import javax.inject.Singleton

import io.reactivex.Flowable

/**
 * Concrete implementation of a data source local
 */
@Singleton
class AppLocalDataSource @Inject
constructor(private val smsDao: SmsDao) : AppDataSource {

    override fun getItemList(): Flowable<List<SmsItem>> {
        return smsDao.getSms()
    }

}