package com.project.hello.vehicle.domain.implementation

import com.project.hello.vehicle.domain.steps.implementation.ReductionImpl
import org.junit.Assert
import org.junit.Test

class ReductionImplTest {

    val tested = ReductionImpl()

    @Test
    fun `test 1`() {
        // given
        val input = listOf("16", "ab", "abc", "abcd")

        // when
        val reduced = tested.reducedInputs(input)

        // then
        Assert.assertEquals(2, reduced.size)
        Assert.assertEquals("16", reduced[0])
        Assert.assertEquals("abcd", reduced[1])
    }

    @Test
    fun `test 3`() {
        // given
        val input = listOf("aaa", "bbbb", "cccc", "ddd")
        val specs = mutableListOf("xxx", "yyy")

        // when
        val reduced = tested.reducedInputs(input, specs)

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
        val reduced = tested.reducedInputs(input)

        // then
        Assert.assertEquals(2, reduced.size)
        Assert.assertEquals("bbbb", reduced[0])
        Assert.assertEquals("cccc", reduced[1])
    }

    @Test
    fun `test 5`() {
        // given
        val input = listOf("aaa", "bbbb", "cccc", "ddd", "")

        // when
        val reduced = tested.reducedInputs(input)

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
        val reduced = tested.reducedInputs(input)

        // then
        Assert.assertEquals(0, reduced.size)
    }

    @Test
    fun `test 7`() {
        // given
        val input = listOf("169", "16", "1", "loadx", "bies", "1xao", "1xe", "abc")

        // when
        val reduced = tested.reducedInputs(input)

        // then
        Assert.assertEquals(7, reduced.size)
    }
}