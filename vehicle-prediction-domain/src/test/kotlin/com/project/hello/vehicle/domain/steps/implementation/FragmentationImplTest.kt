package com.project.hello.vehicle.domain.steps.implementation

import org.junit.Assert
import org.junit.Test

internal class FragmentationImplTest {

    val tested = FragmentationImpl()

    @Test
    fun `test 1`() {
        // given
        val input = "aa16aa"

        // when
        val fragmented = tested.fragmentedInput(input)

        Assert.assertEquals(12, fragmented.size)
    }

    @Test
    fun `test 2`() {
        // given
        val input = "abcdef"

        // when
        val fragmented = tested.fragmentedInput(input)

        Assert.assertEquals(12, fragmented.size)
    }

    @Test
    fun `test 3`() {
        // given
        val input = "abc"

        // when
        val fragmented = tested.fragmentedInput(input)

        val expected = listOf("a", "ab", "abc", "c", "bc", "abc")

        Assert.assertEquals(expected, fragmented)
    }
}