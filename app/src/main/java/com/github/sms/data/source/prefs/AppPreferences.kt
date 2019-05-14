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
package com.github.sms.data.source.prefs

import android.content.Context
import android.content.SharedPreferences

import com.github.sms.utils.AppConstants

import com.github.sms.utils.ext.getBooleanSharedPref
import com.github.sms.utils.ext.getIntSharedPref
import com.github.sms.utils.ext.getStringSharedPref
import com.github.sms.utils.ext.getLongSharedPref
import com.github.sms.utils.ext.setBooleanSharedPref
import com.github.sms.utils.ext.setIntSharedPref
import com.github.sms.utils.ext.setStringSharedPref
import com.github.sms.utils.ext.setLongSharedPref

import javax.inject.Inject
import javax.inject.Named
import javax.inject.Singleton

/**
 * A Helper class for Shared Preferences
 *
 * Created by gk.
 */

@Singleton
class AppPreferences @Inject
constructor(@Named(AppConstants.NAMED_APPLICATION) context: Context,
            @Named(AppConstants.NAMED_PREFERENCE) prefFileName: String) : Preferences {

    private val spPrefs: SharedPreferences = context.getSharedPreferences(prefFileName, Context.MODE_PRIVATE)

    override fun getBoolean(key: String): Boolean {
        return spPrefs.getBooleanSharedPref(key)
    }

    override fun getLong(key: String): Long {
        return spPrefs.getLongSharedPref(key)
    }

    override fun getInt(key: String): Int {
        return spPrefs.getIntSharedPref(key)
    }

    override fun getString(key: String): String? {
        return spPrefs.getStringSharedPref(key)
    }

    override fun setBoolean(key: String, value: Boolean) {
        spPrefs.setBooleanSharedPref(key, value)
    }

    override fun setLong(key: String, value: Long) {
        spPrefs.setLongSharedPref(key, value)
    }

    override fun setInt(key: String, value: Int) {
        spPrefs.setIntSharedPref(key, value)
    }

    override fun getString(key: String, value: String) {
        spPrefs.setStringSharedPref(key, value)
    }

}
