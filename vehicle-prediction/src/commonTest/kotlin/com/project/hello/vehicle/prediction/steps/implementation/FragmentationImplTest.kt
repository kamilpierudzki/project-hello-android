package com.project.hello.vehicle.prediction.steps.implementation

import kotlin.test.Test
import kotlin.test.assertEquals

internal class FragmentationImplTest {

    val tested = FragmentationImpl()

    @Test
    fun test_1() {
        // given
        val input = "aa16aa"

        // when
        val fragmented = tested.fragmentedInput(input)

        assertEquals(12, fragmented.size)
    }

    @Test
    fun test_2() {
        // given
        val input = "abcdef"

        // when
        val fragmented = tested.fragmentedInput(input)

        assertEquals(12, fragmented.size)
    }

    @Test
    fun test_3() {
        // given
        val input = "abc"

        // when
        val fragmented = tested.fragmentedInput(input)

        val expected = listOf("a", "ab", "abc", "c", "bc", "abc")

        assertEquals(expected, fragmented)
    }
}