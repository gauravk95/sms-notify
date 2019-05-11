package com.github.sms.utils.ext

import android.view.View


/*******************************************
 * View Extension Function
 *
 * Created by gk
 ******************************************/

/**
 * Make the view visible
 */
fun View.toVisible() {
    visibility = View.VISIBLE
}

/**
 * Make the view invisible
 */
fun View.toInvisible() {
    visibility = View.INVISIBLE
}

/**
 * Make the view gone
 */
fun View.toGone() {
    visibility = View.GONE
}