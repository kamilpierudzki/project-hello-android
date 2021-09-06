package com.project.hello.vehicle.domain.implementation

import com.project.hello.vehicle.domain.steps.implementation.FragmentationImpl
import org.junit.Assert
import org.junit.Test

internal class FragmentationImplTest {

    val tested = FragmentationImpl()

    @Test
    fun `test 1`() {
        // given
        val input = listOf("aa16aa")

        // when
        val divided = tested.fragmentedInput(input)

        // given
        Assert.assertEquals(2, divided.size)
        Assert.assertEquals("aa", divided[0])
        Assert.assertEquals("16aa", divided[1])
    }

    @Test
    fun `test 2`() {
        // given
        val input = listOf("16aa")

        // when
        val divided = tested.fragmentedInput(input)

        // given
        Assert.assertEquals(2, divided.size)
        Assert.assertEquals("16", divided[0])
        Assert.assertEquals("aa", divided[1])
    }

    @Test
    fun `test 3`() {
        // given
        val input = listOf("aaa16")

        // when
        val divided = tested.fragmentedInput(input)

        // given
        Assert.assertEquals(2, divided.size)
        Assert.assertEquals("aa", divided[0])
        Assert.assertEquals("a16", divided[1])
    }

    @Test
    fun `test 4`() {
        // given
        val input = listOf("os.sobieskiego")

        // when
        val divided = tested.fragmentedInput(input)

        // given
        Assert.assertEquals(2, divided.size)
        Assert.assertEquals("os.sobi", divided[0])
        Assert.assertEquals("eskiego", divided[1])
    }

    @Test
    fun `test 5`() {
        // given
        val input = listOf("16")

        // when
        val divided = tested.fragmentedInput(input)

        // given
        Assert.assertEquals(1, divided.size)
        Assert.assertEquals("16", divided[0])
    }

    @Test
    fun `test 6`() {
        // given
        val input = listOf("169")

        // when
        val divided = tested.fragmentedInput(input)

        // given
        Assert.assertEquals(1, divided.size)
        Assert.assertEquals("169", divided[0])
    }

    @Test
    fun `test 7`() {
        // given
        val input = listOf("1")

        // when
        val fragmented = tested.fragmentedInput(input)

        // given
        Assert.assertEquals(1, fragmented.size)
        Assert.assertEquals("1", fragmented[0])
    }

    @Test
    fun `test 8`() {
        // given
        val input = listOf("o")

        // when
        val divided = tested.fragmentedInput(input)

        // given
        Assert.assertEquals(1, divided.size)
        Assert.assertEquals("o", divided[0])
    }

    @Test
    fun `test 9`() {
        // given
        val input = listOf("16o")

        // when
        val divided = tested.fragmentedInput(input)

        // given
        Assert.assertEquals(2, divided.size)
        Assert.assertEquals("16", divided[0])
        Assert.assertEquals("o", divided[1])
    }

    @Test
    fun `test 10`() {
        // given
        val input = listOf("aa16aa")

        // when
        val fragmented = tested.fragmentedInput(input)

        // given
        Assert.assertEquals(2, fragmented.size)
        Assert.assertEquals("aa", fragmented[0])
        Assert.assertEquals("16aa", fragmented[1])
    }

    @Test
    fun `test 11`() {
        // given
        val input = emptyList<String>()

        // when
        val divided = tested.fragmentedInput(input)

        // given
        Assert.assertEquals(0, divided.size)
    }

    @Test
    fun `test 12`() {
        // given
        val input = listOf("16os.sobieskiego")

        // when
        val f1 = tested.fragmentedInput(input)
        val f2 = tested.fragmentedInput(f1)
        val f3 = tested.fragmentedInput(f2)

        // then
        Assert.assertEquals(8, f3.size)
        Assert.assertEquals("16", f3[0])
    }
}