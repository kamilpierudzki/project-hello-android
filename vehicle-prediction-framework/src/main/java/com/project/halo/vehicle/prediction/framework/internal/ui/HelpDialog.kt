package com.project.halo.vehicle.prediction.framework.internal.ui

import android.content.Context
import androidx.appcompat.app.AlertDialog
import com.project.halo.vehicle.prediction.framework.R

fun showHelpDialog(context: Context) {
    val dialog = AlertDialog.Builder(context)
        .apply {
            setTitle(R.string.help_dialog_title)
            setMessage(R.string.help_dialog_message)
            setNeutralButton(R.string.help_dialog_neutral_button) { dialog, _ ->
                dialog.dismiss()
            }
        }
        .create()
    dialog.show()
}