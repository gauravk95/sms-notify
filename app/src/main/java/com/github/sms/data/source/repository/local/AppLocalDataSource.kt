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

import com.github.sms.data.models.local.SmsHeader
import com.github.sms.data.models.local.SmsHolder
import com.github.sms.data.source.db.SmsDao
import com.github.sms.data.source.repository.AppDataSource
import com.github.sms.utils.time.TimeGroup
import com.github.sms.utils.time.TimeUtil

import javax.inject.Inject
import javax.inject.Singleton

import io.reactivex.Flowable

/**
 * Concrete implementation of a data source local
 */
@Singleton
class AppLocalDataSource @Inject
constructor(private val smsDao: SmsDao) : AppDataSource {

    override fun getItemList(): Flowable<List<SmsHolder>> {
        return smsDao.getSms().map {
            val newList = mutableListOf<SmsHolder>()
            val groupMap = LinkedHashMap<TimeGroup, MutableList<SmsHolder>>()

            //group map list
            groupMap[TimeGroup.ZERO] = mutableListOf()
            groupMap[TimeGroup.HOUR] = mutableListOf()
            groupMap[TimeGroup.TWO_HOUR] = mutableListOf()
            groupMap[TimeGroup.THREE_HOUR] = mutableListOf()
            groupMap[TimeGroup.SIX_HOUR] = mutableListOf()
            groupMap[TimeGroup.TWELVE_HOUR] = mutableListOf()
            groupMap[TimeGroup.ONE_DAY] = mutableListOf()
            groupMap[TimeGroup.OTHERS] = mutableListOf()

            //go through the list of sms and group them
            for (item in it) {
                val timeGroup = TimeUtil.getTimeGroupFromTime(item.time)
                groupMap[timeGroup]?.add(item)
            }

            //create a singe list from the group
            for (key in groupMap.keys) {
                if (!groupMap[key].isNullOrEmpty()) {
                    //create the header
                    newList.add(SmsHeader("h_" + key.ordinal, TimeUtil.getTextIdFromTimeGroup(key)))
                    //add the list
                    newList.addAll(groupMap[key]!!)
                }
            }

            return@map newList
        }
    }

}