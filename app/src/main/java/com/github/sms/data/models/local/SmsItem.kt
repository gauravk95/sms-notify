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
package com.github.sms.data.models.local

import java.io.Serializable

/**
 * Created by gk
 */
data class SmsItem(
        val id: Int,
        val address: String?,
        val message: String?,
        val time: Long,
        val timeSent: Long,
        val readState: Int = 0, //"0" for have not read sms and "1" for have read sms
        var isHighlighted: Boolean = false) : SmsHolder, Serializable {

    override fun getViewType(): Int {
        return SmsView.MAIN.ordinal
    }
}