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
package com.github.sms.di.module

import android.content.ContentResolver
import android.content.Context
import com.github.sms.data.source.db.SmsDao
import com.github.sms.data.source.db.SmsSource
import com.github.sms.data.source.repository.local.AppLocalDataSource
import com.github.sms.data.source.prefs.AppPreferences
import com.github.sms.data.source.prefs.Preferences
import com.github.sms.data.source.repository.AppDataRepository
import com.github.sms.data.source.repository.AppDataSource
import com.github.sms.utils.AppConstants

import javax.inject.Singleton

import dagger.Module
import dagger.Provides
import javax.inject.Named

/**
 * Modules related to data and repository
 *
 * Created by gk.
 */

@Module
class DataModule {

    @Provides
    @Singleton
    @Named(AppConstants.NAMED_PREFERENCE)
    internal fun providePreferenceName(): String {
        return AppConstants.PREF_NAME
    }

    @Provides
    @Singleton
    internal fun provideSmsSource(contentResolver: ContentResolver): SmsSource {
        return SmsDao(contentResolver)
    }

    @Provides
    @Singleton
    internal fun providePreferencesHelper(appPreferences: AppPreferences): Preferences {
        return appPreferences
    }

    @Provides
    @Singleton
    @Named(AppConstants.NAMED_LOCAL)
    internal fun provideAppLocalDataSource(smsDao: SmsDao): AppDataSource {
        return AppLocalDataSource(smsDao)
    }

    @Provides
    @Singleton
    internal fun provideAppRepository(@Named(AppConstants.NAMED_LOCAL) dataRepository: AppDataSource, preferences: Preferences): AppDataSource {
        return AppDataRepository(dataRepository, preferences)
    }

    @Provides
    @Singleton
    internal fun provideContentResolver(@Named(AppConstants.NAMED_APPLICATION) context: Context): ContentResolver {
        return context.contentResolver
    }

}
