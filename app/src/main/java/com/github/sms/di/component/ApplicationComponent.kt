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
package com.github.sms.di.component

import android.app.Application
import android.content.Context

import com.github.sms.base.MainApplication
import com.github.sms.data.source.repository.AppDataSource
import com.github.sms.di.module.ApplicationModule
import com.github.sms.di.module.DataModule
import com.github.sms.utils.AppConstants

import javax.inject.Singleton

import dagger.Component
import javax.inject.Named

/**
 * Application component connecting modules that have application scope
 *
 * Created by gk
 */

@Singleton
@Component(modules = [ApplicationModule::class, DataModule::class])
interface ApplicationComponent {

    fun getAppRepository(): AppDataSource

    fun inject(app: MainApplication)

    @Named(AppConstants.NAMED_APPLICATION)
    fun context(): Context

    fun application(): Application

}
