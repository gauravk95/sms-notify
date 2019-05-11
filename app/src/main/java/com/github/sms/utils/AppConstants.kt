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
package com.github.sms.utils

/**
 * Constants that will be used through the app
 *
 * NOTE: use @JvmField] if using java with kotlin to avoid [AppConstants.INSTANCE.<name_access>]
 *
 * Created by gk
 */
object AppConstants {
    const val PREF_NAME = "app_pref"

    const val NAMED_APPLICATION = "application"
    const val NAMED_ACTIVITY = "activity"
    const val NAMED_PREFERENCE = "preference"
    const val NAMED_LOCAL = "local"

    const val PERM_SMS_REQUEST_CODE = 45
}
