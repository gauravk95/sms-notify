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

import android.os.Bundle
import android.support.v7.widget.Toolbar
import android.view.MenuItem

import com.github.sms.R
import com.github.sms.base.BaseActivity
import com.github.sms.utils.ext.findFragmentById
import com.github.sms.utils.ext.setFragment
import android.content.Intent
import com.github.sms.data.models.event.SmsUpdateAction
import com.github.sms.data.models.event.SmsUpdateEvent
import com.github.sms.data.models.local.SmsItem
import com.github.sms.utils.AppConstants
import com.github.sms.utils.rx.RxEventBus

/**
 * The main activity of the application
 *
 * Created by gk
 */
class MainActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupToolbar()

        //get the target sms messageId if available
        val message = intent?.extras?.getSerializable(AppConstants.INTENT_ARGS_SMS) as? SmsItem

        val mainFragment: MainFragment? = findFragmentById(R.id.content_frame)
        if (mainFragment == null)
            setFragment(MainFragment.newInstance(message), R.id.content_frame)

    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        val bundle = intent.extras
        if (bundle != null) {
            val message = bundle.getSerializable(AppConstants.INTENT_ARGS_SMS) as? SmsItem
            RxEventBus.publish(SmsUpdateEvent(SmsUpdateAction.HIGHLIGHT, message))
        }
    }

    private fun setupToolbar() {
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_close_white)
        supportActionBar?.title = getText(R.string.toolbar_title)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}
