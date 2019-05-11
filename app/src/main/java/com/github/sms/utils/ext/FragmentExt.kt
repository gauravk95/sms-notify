package com.github.sms.utils.ext

import android.app.Dialog
import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.support.v4.app.Fragment
import com.github.sms.R

/********************************************
 * Fragment Extension Functions
 *
 * Created by gk
 *********************************************/

/**
 * Extension function to create a view model
 */
fun <T : ViewModel> Fragment.getViewModel(modelClass: Class<T>, viewModelFactory: ViewModelProvider.Factory? = null): T {
    return viewModelFactory?.let { ViewModelProviders.of(this, it).get(modelClass) } ?: ViewModelProviders.of(this).get(modelClass)
}

/**
 * Creates a Progress Dialog
 * @return
 */
fun Fragment.createProgressDialog(): Dialog {
    val progressDialog = Dialog(context!!)
    progressDialog.show()
    progressDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    progressDialog.setContentView(R.layout.progress_dialog)
    progressDialog.setCancelable(false)
    progressDialog.setCanceledOnTouchOutside(false)
    return progressDialog
}