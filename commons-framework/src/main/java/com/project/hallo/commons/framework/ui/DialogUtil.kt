package com.project.hallo.vehicle.prediction.framework.internal.ui

import android.content.Context
import androidx.annotation.StringRes
import androidx.appcompat.app.AlertDialog
import com.project.hallo.commons.R

fun showNeutralDialog(context: Context, title: String, message: String) {
    val dialog = AlertDialog.Builder(context)
        .apply {
            setTitle(title)
            setMessage(message)
            setNeutralButton(R.string.dialog_neutral_button) { dialog, _ ->
                dialog.dismiss()
            }
        }
        .create()
    dialog.show()
}

fun showNeutralDialog(context: Context, @StringRes title: Int, @StringRes message: Int) {
    showNeutralDialog(context, context.getString(title), context.getString(message))
}