package com.github.sms.ui.adapter

import android.support.v7.util.DiffUtil
import com.github.sms.data.models.local.SmsItem

class ItemDiffCallback : DiffUtil.ItemCallback<SmsItem>() {
    override fun areItemsTheSame(item1: SmsItem, item2: SmsItem): Boolean {
        return item1 == item2
    }

    override fun areContentsTheSame(item1: SmsItem, item2: SmsItem): Boolean {
        return item1.id == item2.id
    }
}