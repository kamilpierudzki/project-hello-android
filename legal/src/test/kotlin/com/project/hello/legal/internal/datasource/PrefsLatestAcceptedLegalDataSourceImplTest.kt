package com.project.hello.legal.internal.datasource

import android.content.SharedPreferences
import com.project.hello.commons.data.ResponseApi
import org.junit.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

internal class PrefsLatestAcceptedLegalDataSourceImplTest {

    val sharedPreferences: SharedPreferences = mock()

    val tested = PrefsLatestAcceptedLegalDataSourceImpl(sharedPreferences)

    @Test
    fun `given latest accepted legal version is saved when fetchLatestAcceptedLegalVersion is called then successful response is returned`() {
        // given
        whenever(sharedPreferences.getInt(any(), any())).thenReturn(1)

        // when
        val response = tested.fetchLatestAcceptedLegalVersion()

        // then
        assert(response is ResponseApi.Success)
    }

    @Test
    fun `given latest accepted legal version is NOT saved when fetchLatestAcceptedLegalVersion is called then error resposen is returned`() {
        // given
        whenever(sharedPreferences.getInt(any(), any())).thenReturn(-1)

        // when
        val response = tested.fetchLatestAcceptedLegalVersion()

        // then
        assert(response is ResponseApi.Error)
    }
}