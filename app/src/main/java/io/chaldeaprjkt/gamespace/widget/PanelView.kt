/*
 * Copyright (C) 2021 Chaldeaprjkt
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.chaldeaprjkt.gamespace.widget

import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.BatteryManager
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.TextView
import android.view.View
import android.view.WindowInsets
import android.widget.LinearLayout
import androidx.core.view.doOnLayout
import io.chaldeaprjkt.gamespace.R
import io.chaldeaprjkt.gamespace.utils.dp

class PanelView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : LinearLayout(context, attrs) {

    private var defaultY: Float? = null
    var relativeY = 0

    init {
        LayoutInflater.from(context).inflate(R.layout.panel_view, this, true)
        isClickable = true
        isFocusable = true
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        applyRelativeLocation()
        batteryTemperature()
    }

    private fun batteryTemperature() {
        val intent: Intent =
            context.registerReceiver(null, IntentFilter(Intent.ACTION_BATTERY_CHANGED))!!
        val temp = intent.getIntExtra(BatteryManager.EXTRA_TEMPERATURE, 0).toInt() / 10
        val degree = "\u2103"
        val batteryTemp:TextView = requireViewById(R.id.batteryTemp)
        batteryTemp.text = "$temp$degree"
    }

    private fun applyRelativeLocation() {
        doOnLayout {
            if (defaultY == null)
                defaultY = y

            val safeArea = rootWindowInsets.getInsets(WindowInsets.Type.systemBars())
            val minY = safeArea.top + 16.dp
            val maxY = safeArea.top + (parent as View).height - safeArea.bottom - height - 16.dp
            y = relativeY.coerceIn(minY, maxY).toFloat()
        }
    }
}
