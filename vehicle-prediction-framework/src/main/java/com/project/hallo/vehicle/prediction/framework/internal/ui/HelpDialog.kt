package com.project.hallo.vehicle.prediction.framework.internal.ui

import android.content.Context
import androidx.annotation.StringRes
import androidx.appcompat.app.AlertDialog
import com.project.hallo.vehicle.prediction.framework.R

internal fun showHelpDialog(context: Context, @StringRes title: Int, @StringRes message: Int) {
    val dialog = AlertDialog.Builder(context)
        .apply {
            setTitle(title)
            setMessage(message)
            setNeutralButton(R.string.help_dialog_neutral_button) { dialog, _ ->
                dialog.dismiss()
            }
        }
        .create()
    dialog.show()
}