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

import com.github.sms.di.PerActivity
import com.github.sms.di.module.ActivityModule
import com.github.sms.ui.main.MainActivity
import com.github.sms.ui.main.MainFragment

import dagger.Component

/**
 * Activity component connecting modules that have Activity scope
 *
 * Created by gk.
 */

@PerActivity
@Component(dependencies = [ApplicationComponent::class], modules = [ActivityModule::class])
interface ActivityComponent {

    fun inject(activity: MainActivity)

    fun inject(fragment: MainFragment)

}
