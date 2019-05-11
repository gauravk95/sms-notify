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

import com.github.sms.data.models.local.SmsItem
import com.github.sms.databinding.ItemSmsBinding

/**
 * Adapter that used to display [SmsItem] in a recycler view
 *
 * Created by gk
 */
class MainItemListAdapter constructor(private val onItemClicked: (SmsItem?) -> Unit) :
        ListAdapter<SmsItem, MainItemListAdapter.ViewHolder>(ItemDiffCallback()) {

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.apply {
            bind(item, onItemClicked)
            itemView.tag = item
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemSmsBinding.inflate(
                LayoutInflater.from(parent.context), parent, false))
    }

    class ViewHolder(private val binding: ItemSmsBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(curItem: SmsItem, onItemClicked: (SmsItem?) -> Unit) {
            binding.apply {
                root.setOnClickListener { onItemClicked(curItem) }
                item = curItem
                executePendingBindings()
            }
        }
    }
}