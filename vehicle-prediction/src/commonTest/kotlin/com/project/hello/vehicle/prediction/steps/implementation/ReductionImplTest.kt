package com.project.hello.vehicle.prediction.steps.implementation

import kotlin.test.Test
import kotlin.test.assertEquals

class ReductionImplTest {

    val tested = ReductionImpl()

    @Test
    fun test_1() {
        // given
        val input = listOf("16", "ab", "abc", "abcd")

        // when
        val reduced = tested.reducedInput(input)

        // then
        assertEquals(2, reduced.size)
        assertEquals("16", reduced[0])
        assertEquals("abcd", reduced[1])
    }

    @Test
    fun test_2() {
        // given
        val input = listOf("aaa", "bbbb", "cccc", "ddd")

        // when
        val reduced = tested.reducedInput(input)

        // then
        assertEquals(2, reduced.size)
        assertEquals("bbbb", reduced[0])
        assertEquals("cccc", reduced[1])
    }

    @Test
    fun test_3() {
        // given
        val input = listOf("aaa", "bbbb", "cccc", "ddd")

        // when
        val reduced = tested.reducedInput(input)

        // then
        assertEquals(2, reduced.size)
        assertEquals("bbbb", reduced[0])
        assertEquals("cccc", reduced[1])
    }

    @Test
    fun test_4() {
        // given
        val input = listOf("aaa", "bbbb", "cccc", "ddd", "")

        // when
        val reduced = tested.reducedInput(input)

        // then
        assertEquals(2, reduced.size)
        assertEquals("bbbb", reduced[0])
        assertEquals("cccc", reduced[1])
    }

    @Test
    fun test_5() {
        // given
        val input = emptyList<String>()

        // when
        val reduced = tested.reducedInput(input)

        // then
        assertEquals(0, reduced.size)
    }

    @Test
    fun test_6() {
        // given
        val input = listOf("169", "16", "1", "loadx", "bies", "1xao", "1xe", "abc")

        // when
        val reduced = tested.reducedInput(input)

        // then
        assertEquals(7, reduced.size)
    }
}
