package com.project.hallo.vehicle.domain.steps.implementation

import com.project.hallo.city.plan.domain.Line
import com.project.hallo.vehicle.domain.steps.CountryCharactersProvider
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
        val match = tested.didNumberMatch("1", Line("1", "aaa"))

        // then
        Assert.assertEquals(true, match)
    }

    @Test
    fun `test 2`() {
        // when
        val match = tested.didNumberMatch("2", Line("1", "aaa"))

        // then
        Assert.assertEquals(false, match)
    }

    @Test
    fun `test 3`() {
        // when
        val match = tested.didDestinationMatch("aaa", Line("1", "aaa"))

        // then
        Assert.assertEquals(true, match)
    }

    @Test
    fun `test 4`() {
        // when
        val match = tested.didDestinationMatch("aąa", Line("1", "aąa"))

        // then
        Assert.assertEquals(true, match)
    }

    @Test
    fun `test 5`() {
        // when
        val match = tested.didDestinationMatch("ąąą", Line("1", "ąąą"))

        // then
        Assert.assertEquals(true, match)
    }

    @Test
    fun `test 6`() {
        // when
        val match = tested.didDestinationMatch("aaa", Line("1", "ąąą"))

        // then
        Assert.assertEquals(true, match)
    }

    @Test
    fun `test 7`() {
        // when
        val match = tested.didDestinationMatch("ąąą", Line("1", "aaa"))

        // then
        Assert.assertEquals(true, match)
    }

    @Test
    fun `test 8`() {
        // when
        val match = tested.didNumberContains("16", Line("169", "aaa"))

        // then
        Assert.assertEquals(true, match)
    }

    @Test
    fun `test 9`() {
        // when
        val match = tested.didNumberContains("1691", Line("169", "aaa"))

        // then
        Assert.assertEquals(false, match)
    }

    @Test
    fun `test 10`() {
        // when
        val match = tested.didDestinationContain("aa", Line("1", "aaaa"))

        // then
        Assert.assertEquals(true, match)
    }

    @Test
    fun `test 11`() {
        // when
        val match = tested.didDestinationContain("aaaaaaaaa", Line("1", "aaaa"))

        // then
        Assert.assertEquals(false, match)
    }

    @Test
    fun `test 12`() {
        // when
        val match = tested.didSliceMatch("cc", Line("1", "aaccaa"))

        // then
        Assert.assertEquals(true, match)
    }
}