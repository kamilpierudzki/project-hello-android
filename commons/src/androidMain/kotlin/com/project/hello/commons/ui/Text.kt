package com.project.hello.commons.ui

import android.content.res.Resources
import androidx.annotation.StringRes

sealed class Text : IText {

    companion object {
        @JvmStatic
        fun of(text: CharSequence): IText = StringText(text)
        fun of(@StringRes textId: Int): IText = ResourceText(textId)
        fun of(texts: List<IText>, separator: String = ""): IText = ListText(texts, separator)

        private fun of(value: Any): IText =
            when (value) {
                is IText -> of(value)
                is CharSequence -> of(value)
                is Int -> of(value)
                else -> throw IllegalArgumentException("Unexpected value type incompatible with IText")
            }

        fun mapOf(vararg pairs: Pair<String, Any>): Map<String, IText> =
            pairs.associate { (k, v) ->
                when (v) {
                    is IText -> Pair(k, v)
                    is CharSequence -> Pair(k, of(v))
                    is Int -> Pair(k, of(v))
                    else -> throw IllegalArgumentException("Unexpected value type incompatible with IText")
                }
            }

        fun empty(): IText = of(text = "")
    }

    private data class ResourceText(@StringRes private val textId: Int) : Text() {
        override fun get(resources: Resources): CharSequence = resources.getText(textId)
    }

    private data class StringText(private val text: CharSequence) : Text() {
        override fun get(resources: Resources): CharSequence = text
    }

    private data class ListText(
        private val texts: List<IText>,
        private val separator: String = ""
    ) : Text() {
        override fun get(resources: Resources): CharSequence =
            texts.joinToString(separator = separator) { it.get(resources) }
    }
}