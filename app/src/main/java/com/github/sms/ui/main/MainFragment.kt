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
package com.github.sms.ui.main

import android.arch.lifecycle.Observer
import android.content.pm.PackageManager
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.github.sms.R
import com.github.sms.base.BaseFragment
import com.github.sms.data.models.event.ListUpdateAction
import com.github.sms.data.models.local.SmsItem
import com.github.sms.databinding.FragmentMainBinding
import com.github.sms.di.factory.AppViewModelFactory
import com.github.sms.ui.adapter.SmsItemListAdapter
import com.github.sms.utils.AppConstants
import com.github.sms.utils.PermissionsUtil
import com.github.sms.utils.ext.getViewModel

import javax.inject.Inject

/**
 * Main Fragment where most of the UI stuff happens
 * Extends functionality of [BaseFragment]
 *
 * Created by gk
 */
class MainFragment : BaseFragment() {

    private lateinit var inflatedView: View

    @Inject
    lateinit var viewModelFactory: AppViewModelFactory

    private lateinit var binding: FragmentMainBinding
    private lateinit var mainViewModel: MainViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_main, container, false)
        inflatedView = binding.root

        //inject dependencies
        activityComponent.inject(this)

        //create the adapter
        val adapter = SmsItemListAdapter {
            if (it != null) {
                showToastMessage(String.format(getString(R.string.sms_item_clicked), it.address))
                //TODO: call the mainViewModel to perform some useful action, like show full sms in other screen
            }
        }
        binding.itemRecyclerView.layoutManager = LinearLayoutManager(binding.root.context)
        binding.itemRecyclerView.adapter = adapter

        //subscribe ui
        subscribeUi(adapter)

        return inflatedView
    }

    override fun onStart() {
        super.onStart()
        if (!PermissionsUtil.checkSmsPermissions(context))
            PermissionsUtil.requestSmsPermissions(this, AppConstants.PERM_SMS_REQUEST_CODE)
    }

    private fun subscribeUi(adapter: SmsItemListAdapter) {
        mainViewModel = getViewModel(MainViewModel::class.java, viewModelFactory)
        binding.hasItems = true

        mainViewModel.isLoading().observe(viewLifecycleOwner, Observer { setProgress(it) })
        mainViewModel.getErrorMsg().observe(viewLifecycleOwner, Observer { errorMessage ->
            if (errorMessage != null) onError(errorMessage)
        })
        mainViewModel.itemList.observe(viewLifecycleOwner, Observer { items ->
            val hasItems = (items != null && items.isNotEmpty())
            binding.hasItems = hasItems
            if (hasItems)
                adapter.submitList(items)
        })
        mainViewModel.listUpdateAction.observe(viewLifecycleOwner, Observer {
            if (it != null)
                when (it) {
                    ListUpdateAction.REFRESH_DATA -> adapter.notifyDataSetChanged()
                    ListUpdateAction.SCROLL_TO_TOP -> binding.itemRecyclerView.scrollToPosition(0)
                }
        })

        //try and load initially
        arguments?.getSerializable(AppConstants.INTENT_ARGS_SMS)?.let {
            mainViewModel.highlightTargetMessage = it as? SmsItem
        }

        mainViewModel.loadSmsList(false)

    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == AppConstants.PERM_SMS_REQUEST_CODE) {
            if (grantResults.size == 2
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED
                    && grantResults[1] == PackageManager.PERMISSION_GRANTED)
                mainViewModel.loadSmsList(false)
            else
                showToastMessage(getString(R.string.permission_denied))
        }
    }

    companion object {
        fun newInstance(message: SmsItem?) = MainFragment().apply {
            arguments = Bundle().apply {
                putSerializable(AppConstants.INTENT_ARGS_SMS, message)
            }
        }
    }
}
