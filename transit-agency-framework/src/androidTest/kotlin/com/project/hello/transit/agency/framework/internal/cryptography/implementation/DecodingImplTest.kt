package com.project.hello.transit.agency.framework.internal.cryptography.implementation

import androidx.test.runner.AndroidJUnit4
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
internal class DecodingImplTest {

    val tested = DecodingImpl()

    @Test
    fun test_1() {
        // given
        val input = "yZMFiXui"

        // when
        val decoded = tested.decode(input)

        // then
        Assert.assertEquals("łąka", decoded)
    }
}