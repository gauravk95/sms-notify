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
package com.github.sms.base

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.support.annotation.StringRes
import android.support.design.widget.Snackbar
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.TextView
import android.widget.Toast

import com.github.sms.R
import com.github.sms.di.component.ActivityComponent
import com.github.sms.di.component.DaggerActivityComponent
import com.github.sms.di.module.ActivityModule
import com.github.sms.utils.ext.createProgressDialog

/**
 * Acts a Base Activity class for all other [AppCompatActivity]
 *
 * Created by gk
 */

abstract class BaseActivity : AppCompatActivity() {

    private var progressDialog: Dialog? = null
    lateinit var activityComponent: ActivityComponent

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityComponent = DaggerActivityComponent.builder()
                .activityModule(ActivityModule(this))
                .applicationComponent((application as MainApplication).component)
                .build()

    }

    public override fun onNewIntent(intent: Intent) {
        this.intent = intent
    }

    /**
     * Custom Progress Dialog
     */
    protected fun showProgressDialog() {
        progressDialog = createProgressDialog()
    }

    protected fun dismissProgressDialog() {
        progressDialog?.dismiss()
    }

    protected fun showToastMessage(message: String?) {
        if (!message.isNullOrEmpty())
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    protected fun showToastMessage(@StringRes stringResourceId: Int) {
        showToastMessage(getString(stringResourceId))
    }

    protected fun showSnackBarMessage(message: String?) {
        if (!message.isNullOrEmpty())
            showSnackBar(message)
    }

    protected fun showSnackBarMessage(@StringRes stringResourceId: Int) {
        showSnackBarMessage(getString(stringResourceId))
    }

    /**
     * Creates a SnackBar for message display
     */
    protected fun showSnackBar(message: String?) {
        val snackBar = Snackbar.make(findViewById(android.R.id.content),
                message as CharSequence, Snackbar.LENGTH_SHORT)
        val sbView = snackBar.view
        val textView = sbView
                .findViewById<View>(android.support.design.R.id.snackbar_text) as TextView
        textView.setTextColor(ContextCompat.getColor(this, R.color.white))
        snackBar.show()
    }

    protected fun onError(message: String?) {
        if (message != null) {
            showSnackBar(message)
        } else {
            showSnackBar(getString(R.string.default_error_message))
        }
    }

    protected fun onError(@StringRes resId: Int) {
        onError(getString(resId))
    }

    override fun onStop() {
        super.onStop()
        dismissProgressDialog()
    }

    override fun onDestroy() {
        super.onDestroy()
        dismissProgressDialog()
    }

}
