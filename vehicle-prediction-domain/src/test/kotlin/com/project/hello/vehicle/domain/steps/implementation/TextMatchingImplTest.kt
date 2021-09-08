package com.project.hello.vehicle.domain.steps.implementation

import com.project.hello.city.plan.domain.model.Line
import com.project.hello.vehicle.domain.steps.CountryCharactersProvider
import com.project.hello.vehicle.domain.steps.TextMatchingResult
import org.junit.Assert
import org.junit.Test

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
    fun `test 1`() {
        // when
        val result = tested.isNumberMatching("1", Line("1", "aaa"))

        // then
        Assert.assertEquals(true, result.isPositive)
    }

    @Test
    fun `test 2`() {
        // when
        val result = tested.isNumberMatching("2", Line("1", "aaa"))

        // then
        Assert.assertEquals(false, result.isPositive)
    }

    @Test
    fun `test 3`() {
        // when
        val result = tested.isDestinationMatching("aaa", Line("1", "aaa"))

        // then
        Assert.assertEquals(true, result.isPositive)
    }

    @Test
    fun `test 4`() {
        // when
        val result = tested.isDestinationMatching("aąa", Line("1", "aąa"))

        // then
        Assert.assertEquals(true, result.isPositive)
    }

    @Test
    fun `test 5`() {
        // when
        val result = tested.isDestinationMatching("ąąą", Line("1", "ąąą"))

        // then
        Assert.assertEquals(true, result.isPositive)
    }

    @Test
    fun `test 6`() {
        // when
        val result = tested.isDestinationMatching("aaa", Line("1", "ąąą"))

        // then
        Assert.assertEquals(true, result.isPositive)
    }

    @Test
    fun `test 7`() {
        // when
        val result = tested.isDestinationMatching("ąąą", Line("1", "aaa"))

        // then
        Assert.assertEquals(true, result.isPositive)
        val percentage = (result as TextMatchingResult.Positive).percentage
        Assert.assertEquals(100, percentage)
    }

    @Test
    fun `test 8`() {
        // when
        val result = tested.isNumberSliceMatching("16", Line("169", "aaa"))

        // then
        Assert.assertEquals(true, result.isPositive)
        val percentage = (result as TextMatchingResult.Positive).percentage
        Assert.assertEquals(66, percentage)
    }

    @Test
    fun `test 9`() {
        // when
        val result = tested.isNumberSliceMatching("1691", Line("169", "aaa"))

        // then
        Assert.assertEquals(true, result.isPositive)
        val percentage = (result as TextMatchingResult.Positive).percentage
        Assert.assertEquals(75, percentage)
    }

    @Test
    fun `test 10`() {
        // when
        val result = tested.isDestinationSliceMatching("aa", Line("1", "aaaa"))

        // then
        Assert.assertEquals(true, result.isPositive)
        val percentage = (result as TextMatchingResult.Positive).percentage
        Assert.assertEquals(50, percentage)
    }

    @Test
    fun `test 11`() {
        // when
        val result = tested.isDestinationSliceMatching("aaaaaaaaa", Line("1", "aaaa"))

        // then
        Assert.assertEquals(true, result.isPositive)
        val percentage = (result as TextMatchingResult.Positive).percentage
        Assert.assertEquals(44, percentage)
    }

    @Test
    fun `test 12`() {
        // when
        val result = tested.isDestinationSliceMatching("cc", Line("1", "aaccaa"))

        // then
        Assert.assertEquals(true, result.isPositive)
        val percentage = (result as TextMatchingResult.Positive).percentage
        Assert.assertEquals(33, percentage)
    }

    @Test
    fun `test 13`() {
        // given
        val line = Line("16", "Os. Sobieskiego")

        // when
        val result = tested.isNumberSliceMatching("160s. Scbieskiego", line)

        // then
        Assert.assertEquals(true, result.isPositive)
        val percentage = (result as TextMatchingResult.Positive).percentage
        Assert.assertEquals(12, percentage)
    }

    @Test
    fun `test 14`() {
        // given
        val line = Line("3", "Włodarska")
        val input = "wloda"

        // when
        val result = tested.isDestinationSliceMatching(input, line)

        // then
        Assert.assertEquals(true, result.isPositive)
        val percentage = (result as TextMatchingResult.Positive).percentage
        Assert.assertEquals(55, percentage)
    }
}