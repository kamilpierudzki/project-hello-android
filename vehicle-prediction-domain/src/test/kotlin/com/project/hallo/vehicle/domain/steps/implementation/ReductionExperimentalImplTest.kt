package com.project.hallo.vehicle.domain.steps.implementation

import com.project.hallo.vehicle.domain.steps.CountryCharactersProvider
import org.junit.Assert
import org.junit.Test

class ReductionExperimentalImplTest {

    val countryCharactersProvider = object : CountryCharactersProvider {
        override fun get(): Map<String, String> = emptyMap()
    }
    val universalTransformation = UniversalTransformationImpl(countryCharactersProvider)

    val tested = ReductionExperimentalImpl(universalTransformation)

    @Test
    fun `test 1`() {
        // given
        val input = listOf("16", "ab", "abc", "abcd")

        // when
        val reduced = tested.reduceInput(input, emptyList())

        // then
        Assert.assertEquals(2, reduced.size)
        Assert.assertEquals("16", reduced[0])
        Assert.assertEquals("abcd", reduced[1])
    }

    @Test
    fun `test 2`() {
        // given
        val input = listOf("aaaa", "bbbb", "cccc", "dddd")
        val foundSpecs = listOf("bbbb", "cccc")

        // when
        val reduced = tested.reduceInput(input, foundSpecs)

        // then
        Assert.assertEquals(2, reduced.size)
        Assert.assertEquals("aaaa", reduced[0])
        Assert.assertEquals("dddd", reduced[1])
    }

    @Test
    fun `test 3`() {
        // given
        val input = listOf("aaa", "bbbb", "cccc", "ddd")
        val specs = listOf("xxx", "yyy")

        // when
        val reduced = tested.reduceInput(input, specs)

        // then
        Assert.assertEquals(2, reduced.size)
        Assert.assertEquals("bbbb", reduced[0])
        Assert.assertEquals("cccc", reduced[1])
    }

    @Test
    fun `test 4`() {
        // given
        val input = listOf("aaa", "bbbb", "cccc", "ddd")

        // when
        val reduced = tested.reduceInput(input, emptyList())

        // then
        Assert.assertEquals(2, reduced.size)
        Assert.assertEquals("bbbb", reduced[0])
        Assert.assertEquals("cccc", reduced[1])
    }

    @Test
    fun `test 5`() {
        // given
        val input = listOf("a aa", "  bbbb", "cccc  ", "ddd", "   ")

        // when
        val reduced = tested.reduceInput(input, emptyList())

        // then
        Assert.assertEquals(2, reduced.size)
        Assert.assertEquals("bbbb", reduced[0])
        Assert.assertEquals("cccc", reduced[1])
    }

    @Test
    fun `test 6`() {
        // given
        val input = emptyList<String>()

        // when
        val reduced = tested.reduceInput(input, emptyList())

        // then
        Assert.assertEquals(0, reduced.size)
    }

    @Test
    fun `test 7`() {
        // given
        val input = listOf("169", "16", "1", "loadx", "bies", "1xao", "1xe", "abc")

        // when
        val reduced = tested.reduceInput(input, emptyList())

        // then
        Assert.assertEquals(7, reduced.size)
    }
}