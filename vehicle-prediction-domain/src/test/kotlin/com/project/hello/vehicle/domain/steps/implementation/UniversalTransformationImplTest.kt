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
        Assert.assertEquals("ldka", formatted)
    }

    @Test
    fun `test 4`() {
        // given
        val text = ".0."

        // when
        val formatted = tested.transformedText(text)

        // then
        Assert.assertEquals("0", formatted)
    }

    @Test
    fun `test 5`() {
        // given
        val text = ".6."

        // when
        val formatted = tested.transformedText(text)

        // then
        Assert.assertEquals("6", formatted)
    }

    @Test
    fun `test 6`() {
        // given
        val text = ".9."

        // when
        val formatted = tested.transformedText(text)

        // then
        Assert.assertEquals("9", formatted)
    }

    @Test
    fun `test 7`() {
        // given
        val text = ".12."

        // when
        val formatted = tested.transformedText(text)

        // then
        Assert.assertEquals("12", formatted)
    }

    @Test
    fun `test 8`() {
        // given
        val text = ".a."

        // when
        val formatted = tested.transformedText(text)

        // then
        Assert.assertEquals("a", formatted)
    }

    @Test
    fun `test 9`() {
        // given
        val text = ".g."

        // when
        val formatted = tested.transformedText(text)

        // then
        Assert.assertEquals("g", formatted)
    }

    @Test
    fun `test 10`() {
        // given
        val text = ".z."

        // when
        val formatted = tested.transformedText(text)

        // then
        Assert.assertEquals("z", formatted)
    }

    @Test
    fun `test 11`() {
        // given
        val text = ".dj."

        // when
        val formatted = tested.transformedText(text)

        // then
        Assert.assertEquals("dj", formatted)
    }

    @Test
    fun `test 12`() {
        // given
        val text = "(abc123)"

        // when
        val formatted = tested.transformedText(text)

        // then
        Assert.assertEquals("abc123", formatted)
    }

    @Test
    fun `test 13`() {
        // given
        val text = "!@#$%^&*()_+{}[]:\"\\|,./<>?`~"

        // when
        val formatted = tested.transformedText(text)

        // then
        Assert.assertEquals("", formatted)
    }
}