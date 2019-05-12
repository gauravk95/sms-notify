/*
 * Copyright 2019 Gaurav Kumar
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.github.sms.ui.adapter

import android.content.res.ColorStateList
import android.databinding.BindingAdapter
import android.graphics.Color
import android.os.Build
import android.support.v4.content.ContextCompat
import android.support.v4.content.res.ResourcesCompat
import android.support.v4.widget.ImageViewCompat
import android.view.View
import android.support.v7.widget.RecyclerView
import android.util.TypedValue
import android.widget.ImageView
import android.widget.TextView
import com.github.sms.R
import java.text.SimpleDateFormat
import java.util.*

@BindingAdapter("isGone")
fun bindIsGone(view: View, isGone: Boolean) {
    view.visibility = if (isGone) {
        View.GONE
    } else {
        View.VISIBLE
    }
}

@BindingAdapter("setDate")
fun setDate(tv: TextView, time: Long?) {
    if (time != null) {
        tv.text = SimpleDateFormat("HH:mm  MMM dd", Locale.getDefault()).format(Date(time))
    }
}

@BindingAdapter("setBgHighlight")
fun setBgHighlight(v: View, isHighlight: Boolean?) {
    if (isHighlight != null && isHighlight)
        v.setBackgroundColor(ContextCompat.getColor(v.context, R.color.background_highlight))
    else {
        val resource = TypedValue()
        v.context.theme.resolveAttribute(android.R.attr.selectableItemBackground, resource, true)
        v.background = ContextCompat.getDrawable(v.context, resource.resourceId)
    }
}

@BindingAdapter("randomBackgroundTint")
fun setRandomBackgroundTint(view: ImageView, color: Int?) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        val rnd = Random()
        view.backgroundTintList = ColorStateList.valueOf(
                Color.argb(255,
                        rnd.nextInt(256),
                        rnd.nextInt(256),
                        rnd.nextInt(256)))
    }
}


