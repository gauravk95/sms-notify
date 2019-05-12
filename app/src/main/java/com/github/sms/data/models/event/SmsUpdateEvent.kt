package com.github.sms.data.models.event

import com.github.sms.data.models.local.SmsItem
import java.io.Serializable

data class SmsUpdateEvent constructor(
        val action: SmsUpdateAction,
        val message: SmsItem? = null) : Serializable