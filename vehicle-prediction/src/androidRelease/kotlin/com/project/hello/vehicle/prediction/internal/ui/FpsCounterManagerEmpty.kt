package com.project.hello.vehicle.prediction.internal.ui

import android.widget.TextView
import androidx.lifecycle.LifecycleOwner
import javax.inject.Inject

internal class FpsCounterManagerEmpty @Inject constructor() : FpsCounterManager {
    override fun newFrameProcessed(timestampInMillis: Long) {
    }

    override fun observeFpsCounterUiChanges(viewLifecycleOwner: LifecycleOwner, label: TextView) {
    }
}