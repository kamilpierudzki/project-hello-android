package com.project.hello.vehicle.prediction.steps.implementation

import com.project.hello.transit.agency.model.Line
import com.project.hello.vehicle.prediction.steps.CountryCharactersProvider
import com.project.hello.vehicle.prediction.steps.TextMatchingResult
import kotlin.test.Test
import kotlin.test.assertEquals

internal class TextMatchingImplTest {

    val polishCharacters = mapOf("ą" to "a", "ę" to "e", "ł" to "l")

    val countryCharactersProvider: CountryCharactersProvider = object : CountryCharactersProvider {
        override fun get(): Map<String, String> {
            return polishCharacters
        }
    }
    val universalTransformation = UniversalTransformationImpl(countryCharactersProvider)

    val tested = TextMatchingImpl(universalTransformation)

    @Test
    fun test_1() {
        // when
        val result = tested.isNumberMatching("1", Line("1", "aaa"))

        // then
        assertEquals(true, result.isPositive)
    }

    @Test
    fun test_2() {
        // when
        val result = tested.isNumberMatching("2", Line("1", "aaa"))

        // then
        assertEquals(false, result.isPositive)
    }

    @Test
    fun test_3() {
        // when
        val result = tested.isDestinationMatching("aaa", Line("1", "aaa"))

        // then
        assertEquals(true, result.isPositive)
    }

    @Test
    fun test_4() {
        // when
        val result = tested.isDestinationMatching("aaa", Line("1", "aąa"))

        // then
        assertEquals(true, result.isPositive)
    }

    @Test
    fun test_5() {
        // when
        val result = tested.isDestinationMatching("aaa", Line("1", "ąąą"))

        // then
        assertEquals(true, result.isPositive)
    }

    @Test
    fun test_6() {
        // when
        val result = tested.isDestinationMatching("aaa", Line("1", "ąąą"))

        // then
        assertEquals(true, result.isPositive)
    }

    @Test
    fun test_7() {
        // when
        val result = tested.isDestinationMatching("aaa", Line("1", "AAA"))

        // then
        assertEquals(true, result.isPositive)
        val percentage = (result as TextMatchingResult.Positive).percentage
        assertEquals(100, percentage)
    }

    @Test
    fun test_8() {
        // when
        val result = tested.isNumberSliceMatching("16", Line("169", "aaa"))

        // then
        assertEquals(true, result.isPositive)
        val percentage = (result as TextMatchingResult.Positive).percentage
        assertEquals(66, percentage)
    }

    @Test
    fun test_9() {
        // when
        val result = tested.isNumberSliceMatching("1691", Line("169", "aaa"))

        // then
        assertEquals(true, result.isPositive)
        val percentage = (result as TextMatchingResult.Positive).percentage
        assertEquals(75, percentage)
    }

    @Test
    fun test_10() {
        // when
        val result = tested.isDestinationSliceMatching("aa", Line("1", "aaaa"))

        // then
        assertEquals(true, result.isPositive)
        val percentage = (result as TextMatchingResult.Positive).percentage
        assertEquals(50, percentage)
    }

    @Test
    fun test_11() {
        // when
        val result = tested.isDestinationSliceMatching("aaaaaaaaa", Line("1", "aaaa"))

        // then
        assertEquals(true, result.isPositive)
        val percentage = (result as TextMatchingResult.Positive).percentage
        assertEquals(44, percentage)
    }

    @Test
    fun test_12() {
        // when
        val result = tested.isDestinationSliceMatching("cc", Line("1", "aaccaa"))

        // then
        assertEquals(true, result.isPositive)
        val percentage = (result as TextMatchingResult.Positive).percentage
        assertEquals(33, percentage)
    }

    @Test
    fun test_13() {
        // given
        val line = Line("16", "Os. Sobieskiego")

        // when
        val result = tested.isNumberSliceMatching("160sScbieskiego", line)

        // then
        assertEquals(true, result.isPositive)
        val percentage = (result as TextMatchingResult.Positive).percentage
        assertEquals(13, percentage)
    }

    @Test
    fun test_14() {
        // given
        val line = Line("3", "Włodarska")
        val input = "wloda"

        // when
        val result = tested.isDestinationSliceMatching(input, line)

        // then
        assertEquals(true, result.isPositive)
        val percentage = (result as TextMatchingResult.Positive).percentage
        assertEquals(55, percentage)
    }
}