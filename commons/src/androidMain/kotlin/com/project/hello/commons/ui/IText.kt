package com.project.hello.commons.ui

import android.content.res.Resources

interface IText {
    fun get(resources: Resources): CharSequence = ""
    fun getArray(resources: Resources): Array<CharSequence> = emptyArray()
}