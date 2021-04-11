package com.project.halo.commons.viewmodel.ui

import android.content.res.Resources

interface IText {
    fun get(resources: Resources): CharSequence = ""
    fun getArray(resources: Resources): Array<CharSequence> = emptyArray()
}