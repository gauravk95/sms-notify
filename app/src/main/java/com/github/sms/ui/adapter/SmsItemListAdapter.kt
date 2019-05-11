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
package com.github.sms.ui.adapter

import android.support.v7.recyclerview.extensions.ListAdapter
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.github.sms.data.models.local.SmsHeader
import com.github.sms.data.models.local.SmsHolder

import com.github.sms.data.models.local.SmsItem
import com.github.sms.data.models.local.SmsView
import com.github.sms.databinding.ItemSmsBinding
import com.github.sms.databinding.ItemSmsHeaderBinding

/**
 * Adapter that used to display [SmsHolder] in a recycler view
 *
 * Created by gk
 */
class SmsItemListAdapter constructor(private val onItemClicked: (SmsItem?) -> Unit) :
        ListAdapter<SmsHolder, RecyclerView.ViewHolder>(SmsItemDiffCallback()) {

    override fun onBindViewHolder(view: RecyclerView.ViewHolder, position: Int) {
        val item = getItem(position)
        when (item.getViewType()) {
            SmsView.HEADER.ordinal -> {
                val holderHeader = view as HeaderViewHolder
                holderHeader.apply {
                    bind(item as SmsHeader)
                    itemView.tag = item
                }
            }
            SmsView.MAIN.ordinal -> {
                val holderMain = view as MainViewHolder
                holderMain.apply {
                    bind(item as SmsItem, onItemClicked)
                    itemView.tag = item
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (viewType == SmsView.HEADER.ordinal) {
            return HeaderViewHolder(ItemSmsHeaderBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false))
        }
        return MainViewHolder(ItemSmsBinding.inflate(
                LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemViewType(position: Int): Int {
        return getItem(position).getViewType()
    }

    /**
    View holder for header
     */
    class HeaderViewHolder(private val binding: com.github.sms.databinding.ItemSmsHeaderBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(curItem: SmsHeader) {
            binding.apply {
                item = curItem
                executePendingBindings()
            }
        }
    }

    /**
     * View Holder for main content
     */
    class MainViewHolder(private val binding: ItemSmsBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(curItem: SmsItem, onItemClicked: (SmsItem?) -> Unit) {
            binding.apply {
                root.setOnClickListener { onItemClicked(curItem) }
                item = curItem
                executePendingBindings()
            }
        }
    }

}