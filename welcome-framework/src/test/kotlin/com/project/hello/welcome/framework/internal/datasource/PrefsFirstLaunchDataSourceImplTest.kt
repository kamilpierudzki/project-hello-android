package com.project.hello.welcome.framework.internal.datasource

import android.content.SharedPreferences
import com.project.hello.commons.domain.data.ResponseApi
import org.junit.Assert
import org.junit.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

internal class PrefsFirstLaunchDataSourceImplTest {

    val sharedPreferences: SharedPreferences = mock()

    val tested = PrefsFirstLaunchDataSourceImpl(sharedPreferences)

    @Test
    fun `given there is not data about first launch when isFirstLaunch is called then error response is returned`() {
        // given
        whenever(sharedPreferences.contains(any())).thenReturn(false)

        // when
        val response = tested.isFirstLaunch()

        // then
        Assert.assertEquals(true, response is ResponseApi.Error)
    }

    @Test
    fun `given there is data about first launch and it already was first launch when isFirstLaunch is called then successful response is returned`() {
        // given
        whenever(sharedPreferences.contains(any())).thenReturn(true)
        whenever(sharedPreferences.getBoolean(any(), any())).thenReturn(true)

        // when
        val response = tested.isFirstLaunch()

        // then
        Assert.assertEquals(true, response is ResponseApi.Success)
        Assert.assertEquals(true, (response as ResponseApi.Success).successData)
    }

    @Test
    fun `given there is data about first launch and it was not already first launch when isFirstLaunch is called then successful response is returned`() {
        // given
        whenever(sharedPreferences.contains(any())).thenReturn(true)
        whenever(sharedPreferences.getBoolean(any(), any())).thenReturn(false)

        // when
        val response = tested.isFirstLaunch()

        // then
        Assert.assertEquals(true, response is ResponseApi.Success)
        Assert.assertEquals(false, (response as ResponseApi.Success).successData)
    }
}