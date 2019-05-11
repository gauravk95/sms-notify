package com.github.sms.ui.adapter

import android.support.v7.util.DiffUtil
import com.github.sms.data.models.local.SmsHolder

class SmsItemDiffCallback : DiffUtil.ItemCallback<SmsHolder>() {
    override fun areItemsTheSame(item1: SmsHolder, item2: SmsHolder): Boolean {
        return item1 == item2
    }

    override fun areContentsTheSame(item1: SmsHolder, item2: SmsHolder): Boolean {
        //if (item1.getViewType() == SmsView.HEADER.ordinal)
        //TODO: update content same
        return true
    }
}