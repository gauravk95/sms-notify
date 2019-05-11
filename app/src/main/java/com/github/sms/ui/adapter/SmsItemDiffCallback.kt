package com.github.sms.ui.adapter

import android.support.v7.util.DiffUtil
import com.github.sms.data.models.local.SmsHeader
import com.github.sms.data.models.local.SmsHolder
import com.github.sms.data.models.local.SmsItem
import com.github.sms.data.models.local.SmsView

class SmsItemDiffCallback : DiffUtil.ItemCallback<SmsHolder>() {
    override fun areItemsTheSame(item1: SmsHolder, item2: SmsHolder): Boolean {
        return item1 == item2
    }

    override fun areContentsTheSame(item1: SmsHolder, item2: SmsHolder): Boolean {
        if (item1.getViewType() == SmsView.MAIN.ordinal
                && (item1.getViewType() == item2.getViewType())) {
            val i1 = item1 as SmsItem
            val i2 = item2 as SmsItem
            return i1.id == i2.id
        }

        if (item1.getViewType() == SmsView.HEADER.ordinal
                && (item1.getViewType() == item2.getViewType())) {
            val i1 = item1 as SmsHeader
            val i2 = item2 as SmsHeader
            return i1.id == i2.id
        }

        //return true for other condition
        return true
    }
}