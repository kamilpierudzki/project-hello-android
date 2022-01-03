package com.project.hello.vehicle.prediction.steps.implementation

import com.project.hello.vehicle.prediction.steps.CountryCharactersProvider
import kotlin.test.Test
import kotlin.test.assertEquals

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
    fun test_1() {
        // given
        val text = "łąka"

        // when
        val formatted = tested.transformedText(text)

        // then
        assertEquals("laka", formatted)
    }

    @Test
    fun test_2() {
        // given
        val text = "ręka"

        // when
        val formatted = tested.transformedText(text)

        // then
        assertEquals("reka", formatted)
    }

    @Test
    fun test_3() {
        // given
        val text = "łódka"

        // when
        val formatted = tested.transformedText(text)

        // then
        assertEquals("ldka", formatted)
    }

    @Test
    fun test_4() {
        // given
        val text = ".0."

        // when
        val formatted = tested.transformedText(text)

        // then
        assertEquals("0", formatted)
    }

    @Test
    fun test_5() {
        // given
        val text = ".6."

        // when
        val formatted = tested.transformedText(text)

        // then
        assertEquals("6", formatted)
    }

    @Test
    fun test_6() {
        // given
        val text = ".9."

        // when
        val formatted = tested.transformedText(text)

        // then
        assertEquals("9", formatted)
    }

    @Test
    fun test_7() {
        // given
        val text = ".12."

        // when
        val formatted = tested.transformedText(text)

        // then
        assertEquals("12", formatted)
    }

    @Test
    fun test_8() {
        // given
        val text = ".a."

        // when
        val formatted = tested.transformedText(text)

        // then
        assertEquals("a", formatted)
    }

    @Test
    fun test_9() {
        // given
        val text = ".g."

        // when
        val formatted = tested.transformedText(text)

        // then
        assertEquals("g", formatted)
    }

    @Test
    fun test_10() {
        // given
        val text = ".z."

        // when
        val formatted = tested.transformedText(text)

        // then
        assertEquals("z", formatted)
    }

    @Test
    fun test_11() {
        // given
        val text = ".dj."

        // when
        val formatted = tested.transformedText(text)

        // then
        assertEquals("dj", formatted)
    }

    @Test
    fun test_12() {
        // given
        val text = "(abc123)"

        // when
        val formatted = tested.transformedText(text)

        // then
        assertEquals("abc123", formatted)
    }

    @Test
    fun test_13() {
        // given
        val text = "!@#$%^&*()_+{}[]:\"\\|,./<>?`~"

        // when
        val formatted = tested.transformedText(text)

        // then
        assertEquals("", formatted)
    }
}