package com.project.hello.country.internal

import android.content.res.Resources
import com.project.hello.country.R
import com.project.hello.country.api.ResourceCountryCharacters
import dagger.hilt.android.scopes.FragmentScoped
import javax.inject.Inject

private const val SPLIT_CHARACTER = "-"

@FragmentScoped
internal class ResourceCountryCharactersImpl @Inject constructor(
    private val resources: Resources
) : ResourceCountryCharacters {

    override fun get(): Map<String, String> =
        resources.getStringArray(R.array.country_characters)
            .filter { element ->
                val numberOfSeparators = element.count { c -> c.toString() == SPLIT_CHARACTER }
                numberOfSeparators == 1
            }
            .map { element ->
                val pos = element.indexOf(SPLIT_CHARACTER, ignoreCase = true)
                val key = element.slice(IntRange(0, pos - 1))
                val value = element.slice(IntRange(pos + 1, element.length - 1))
                key to value
            }
            .toMap()
}