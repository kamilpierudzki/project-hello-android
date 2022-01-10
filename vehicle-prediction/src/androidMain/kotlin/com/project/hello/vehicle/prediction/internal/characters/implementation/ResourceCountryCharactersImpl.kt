package com.project.hello.vehicle.prediction.internal.characters.implementation

import android.content.res.Resources
import com.project.hello.vehicle.prediction.R
import com.project.hello.vehicle.prediction.internal.characters.ResourceCountryCharacters

private const val SPLIT_CHARACTER = "-"

internal class ResourceCountryCharactersImpl(
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