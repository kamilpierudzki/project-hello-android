package com.project.hello.vehicle.domain.steps.implementation

import com.project.hello.vehicle.domain.steps.CountryCharactersProvider
import org.junit.Assert
import org.junit.Test

internal class UniversalTransformationImplTest {

    val polishCharacters = mapOf(
        "ą" to "a",
        "ę" to "e",
        "ł" to "l"
    )

    val countryCharactersProvider: CountryCharactersProvider = object : CountryCharactersProvider {
        override fun get() = polishCharacters
    }

    val tested = UniversalTransformationImpl(countryCharactersProvider)

    @Test
    fun `test 1`() {
        // given
        val text = "łąka"

        // when
        val formatted = tested.transformedText(text)

        // then
        Assert.assertEquals("laka", formatted)
    }

    @Test
    fun `test 2`() {
        // given
        val text = "ręka"

        // when
        val formatted = tested.transformedText(text)

        // then
        Assert.assertEquals("reka", formatted)
    }

    @Test
    fun `test 3`() {
        // given
        val text = "łódka"

        // when
        val formatted = tested.transformedText(text)

        // then
        Assert.assertEquals("lódka", formatted)
    }
}