package com.project.hello.commons.ui

import android.content.Context
import androidx.annotation.StringRes
import androidx.appcompat.app.AlertDialog
import com.project.hello.commons.R

fun showInformationDialog(
    context: Context,
    title: String,
    message: String,
    dialogDismissed: () -> Unit = {}
) {
    val dialog = AlertDialog.Builder(context)
        .apply {
            setTitle(title)
            setMessage(message)
            setNeutralButton(R.string.dialog_neutral_button) { dialog, _ ->
                dialog.dismiss()
            }
            setOnDismissListener { dialogDismissed.invoke() }
        }
        .create()
    dialog.show()
}

fun showInformationDialog(
    context: Context,
    @StringRes title: Int,
    @StringRes message: Int,
    dialogDismissed: () -> Unit = {}
) {
    showInformationDialog(
        context,
        context.getString(title),
        context.getString(message),
        dialogDismissed
    )
}

fun showBinaryDialog(
    context: Context,
    title: String,
    message: String,
    positiveAction: () -> Unit,
    negativeAction: () -> Unit
) {
    val dialog = AlertDialog.Builder(context)
        .apply {
            setTitle(title)
            setMessage(message)
            setPositiveButton(R.string.dialog_positive_button) { dialog, _ ->
                dialog.dismiss()
                positiveAction.invoke()
            }
            setNegativeButton(R.string.dialog_negative_button) { dialog, _ ->
                dialog.dismiss()
                negativeAction.invoke()
            }
            setCancelable(false)
        }
        .create()
    dialog.show()
}

fun showBinaryDialog(
    context: Context,
    @StringRes title: Int,
    @StringRes message: Int,
    positiveAction: () -> Unit,
    negativeAction: () -> Unit
) {
    showBinaryDialog(
        context,
        context.getString(title),
        context.getString(message),
        positiveAction,
        negativeAction
    )
}

fun showErrorDialog() {

}