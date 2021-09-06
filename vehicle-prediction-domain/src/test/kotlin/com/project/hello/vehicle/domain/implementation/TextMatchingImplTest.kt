package com.project.hello.vehicle.domain.implementation

import com.project.hello.city.plan.domain.model.Line
import com.project.hello.vehicle.domain.steps.CountryCharactersProvider
import com.project.hello.vehicle.domain.steps.implementation.TextMatchingImpl
import com.project.hello.vehicle.domain.steps.implementation.UniversalTransformationImpl
import org.junit.Assert
import org.junit.Test

internal class TextMatchingImplTest {

    val polishCharacters = mapOf("ą" to "a", "ę" to "e")

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
        val match = tested.isNumberMatching("1", Line("1", "aaa"))

        // then
        Assert.assertEquals(true, match)
    }

    @Test
    fun `test 2`() {
        // when
        val match = tested.isNumberMatching("2", Line("1", "aaa"))

        // then
        Assert.assertEquals(false, match)
    }

    @Test
    fun `test 3`() {
        // when
        val match = tested.isDestinationMatching("aaa", Line("1", "aaa"))

        // then
        Assert.assertEquals(true, match)
    }

    @Test
    fun `test 4`() {
        // when
        val match = tested.isDestinationMatching("aąa", Line("1", "aąa"))

        // then
        Assert.assertEquals(true, match)
    }

    @Test
    fun `test 5`() {
        // when
        val match = tested.isDestinationMatching("ąąą", Line("1", "ąąą"))

        // then
        Assert.assertEquals(true, match)
    }

    @Test
    fun `test 6`() {
        // when
        val match = tested.isDestinationMatching("aaa", Line("1", "ąąą"))

        // then
        Assert.assertEquals(true, match)
    }

    @Test
    fun `test 7`() {
        // when
        val match = tested.isDestinationMatching("ąąą", Line("1", "aaa"))

        // then
        Assert.assertEquals(true, match)
    }

    @Test
    fun `test 8`() {
        // when
        val match = tested.isNumberSliceMatching("16", Line("169", "aaa"))

        // then
        Assert.assertEquals(true, match)
    }

    @Test
    fun `test 9`() {
        // when
        val match = tested.isNumberSliceMatching("1691", Line("169", "aaa"))

        // then
        Assert.assertEquals(true, match)
    }

    @Test
    fun `test 10`() {
        // when
        val match = tested.isDestinationSliceMatching("aa", Line("1", "aaaa"))

        // then
        Assert.assertEquals(true, match)
    }

    @Test
    fun `test 11`() {
        // when
        val match = tested.isDestinationSliceMatching("aaaaaaaaa", Line("1", "aaaa"))

        // then
        Assert.assertEquals(true, match)
    }

    @Test
    fun `test 12`() {
        // when
        val match = tested.isDestinationSliceMatching("cc", Line("1", "aaccaa"))

        // then
        Assert.assertEquals(true, match)
    }

    @Test
    fun `test 13`() {
        // given
        val line = Line("16", "Os. Sobieskiego")

        // when
        val isSliceMatching = tested.isNumberSliceMatching("160s. Scbieskiego", line)

        // then
        Assert.assertEquals(true, isSliceMatching)
    }
}