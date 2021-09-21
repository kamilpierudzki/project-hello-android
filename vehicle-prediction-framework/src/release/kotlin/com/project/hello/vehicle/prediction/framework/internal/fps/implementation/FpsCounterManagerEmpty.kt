package com.project.hello.vehicle.prediction.framework.internal.fps.implementation

import android.widget.TextView
import androidx.lifecycle.LifecycleOwner
import com.project.hello.vehicle.prediction.framework.internal.fps.FpsCounterManager
import javax.inject.Inject

internal class FpsCounterManagerEmpty @Inject constructor() : FpsCounterManager {
    override fun newFrameProcessed(timestampInMillis: Long) {
    }

    override fun observeFpsCounterUiChanges(viewLifecycleOwner: LifecycleOwner, label: TextView) {
    }
}