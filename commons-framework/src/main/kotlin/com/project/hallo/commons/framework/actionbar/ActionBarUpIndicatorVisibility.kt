package com.project.hallo.commons.framework.actionbar

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import javax.inject.Inject

class ActionBarUpIndicatorVisibility @Inject constructor() {

    fun disableUpButtonIfPossible(activity: Activity?) {
        val appCompatActivity = (activity as? AppCompatActivity)
        val supportActionBar = appCompatActivity?.supportActionBar
        supportActionBar?.setHomeAsUpIndicator(null)
    }
}