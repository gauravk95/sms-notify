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
package com.github.sms.data.source.db

import android.content.ContentResolver
import android.net.Uri
import com.github.sms.data.models.local.SmsItem
import io.reactivex.Flowable

import javax.inject.Inject

/**
 * Access SMS Data
 */

class SmsDao @Inject
constructor(private val contentResolver: ContentResolver) : SmsSource {

    override fun getSms(): Flowable<List<SmsItem>> {
        return Flowable.just(getAllSms())
    }

    private fun getAllSms(): List<SmsItem> {
        val lstSms = ArrayList<SmsItem>()
        try {
            val message = Uri.parse("content://sms/inbox") //get inbox messages
            //val message = Uri.parse("content://sms/") //get all messages
            val cursor = contentResolver.query(message, null, null, null, null)
            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    for (i in 0 until cursor.count) {
                        val id = cursor.getString(cursor.getColumnIndexOrThrow("_id"))
                        val address = cursor.getString(cursor.getColumnIndexOrThrow("address"))
                        val msg = cursor.getString(cursor.getColumnIndexOrThrow("body"))
                        val readState = cursor.getInt(cursor.getColumnIndex("read"))
                        val time = cursor.getLong(cursor.getColumnIndexOrThrow("date"))

                        lstSms.add(SmsItem(id, address, msg, readState, time))
                        cursor.moveToNext()
                    }
                }
                cursor.close()
            } else {
                //no message, do something
            }
        } catch (e: SecurityException) {
            //occur if read sms permission is not available, handle here
        }

        return lstSms
    }
}