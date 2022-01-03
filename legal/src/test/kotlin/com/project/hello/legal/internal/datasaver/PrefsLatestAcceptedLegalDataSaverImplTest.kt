package com.project.hello.legal.internal.datasaver

import android.content.SharedPreferences
import com.project.hello.commons.data.ResponseApi
import com.project.hello.legal.model.api.LatestAvailableLegalApi
import org.junit.Test
import org.mockito.kotlin.*

internal class PrefsLatestAcceptedLegalDataSaverImplTest {

    val sharedPreferencesEdit: SharedPreferences.Editor = mock()
    val sharedPreferences: SharedPreferences = mock {
        on { edit() } doReturn sharedPreferencesEdit
    }

    val tested = PrefsLatestAcceptedLegalDataSaverImpl(sharedPreferences)

    @Test
    fun `given legal api when saveLatestAcceptedLegal is called then data is saved`() {
        // given
        val version = 0
        val legalApi = LatestAvailableLegalApi(version, "")

        // when
        tested.saveLatestAcceptedLegal(legalApi)

        // then
        verify(sharedPreferencesEdit).putInt(any(), eq(version))
        verify(sharedPreferencesEdit).commit()
    }

    @Test
    fun `given legal api when saveLatestAcceptedLegal is called then successful response is returned`() {
        // given
        val legalApi = LatestAvailableLegalApi(0, "")

        // when
        val result = tested.saveLatestAcceptedLegal(legalApi)

        // then
        assert(result is ResponseApi.Success)
    }
}