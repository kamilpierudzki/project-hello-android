package com.project.hello.vehicle.prediction.internal.ui

import android.widget.TextView
import androidx.lifecycle.LifecycleOwner

internal interface FpsCounterManager {
    fun newFrameProcessed(timestampInMillis: Long)
    fun observeFpsCounterUiChanges(viewLifecycleOwner: LifecycleOwner, label: TextView)
}