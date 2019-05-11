package com.github.sms.utils

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.support.v4.app.ActivityCompat
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat

object PermissionsUtil {

    fun checkSmsPermissions(context: Context?): Boolean {
        if (context == null) return false
        return ContextCompat.checkSelfPermission(context, Manifest.permission.READ_SMS) == PackageManager.PERMISSION_GRANTED
    }

    fun requestSmsPermissions(activity: Activity?, reqCode: Int) {
        if (activity == null) return
        ActivityCompat.requestPermissions(activity, arrayOf(Manifest.permission.READ_SMS), reqCode)
    }

    fun requestSmsPermissions(fragment: Fragment?, reqCode: Int) {
        if (fragment == null) return
        fragment.requestPermissions(arrayOf(Manifest.permission.READ_SMS), reqCode)
    }
}