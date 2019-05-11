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

import android.content.Context
import android.support.v7.app.AppCompatActivity
import com.github.sms.data.source.repository.AppDataSource

import com.github.sms.di.factory.AppViewModelFactory
import com.github.sms.utils.AppConstants
import com.github.sms.utils.rx.AppSchedulerProvider
import com.github.sms.utils.rx.SchedulerProvider

import dagger.Module
import dagger.Provides
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Named

/**
 * Modules related to activity
 *
 * Created by gk.
 */

@Module
class ActivityModule(private val activity: AppCompatActivity) {

    @Provides
    @Named(AppConstants.NAMED_ACTIVITY)
    internal fun provideContext(): Context {
        return activity
    }

    @Provides
    internal fun provideActivity(): AppCompatActivity {
        return activity
    }

    @Provides
    internal fun provideCompositeDisposable(): CompositeDisposable {
        return CompositeDisposable()
    }

    @Provides
    internal fun provideSchedulerProvider(): SchedulerProvider {
        return AppSchedulerProvider()
    }

    @Provides
    internal fun provideAppViewModelFactory(appDataSource: AppDataSource,
                                   schedulerProvider: SchedulerProvider,
                                   compositeDisposable: CompositeDisposable): AppViewModelFactory {
        return AppViewModelFactory(appDataSource, schedulerProvider, compositeDisposable)
    }

}
