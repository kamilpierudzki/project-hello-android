package com.project.hello.welcome.internal.datasource

import android.content.SharedPreferences
import com.project.hello.commons.data.ResponseApi
import org.junit.Assert
import org.junit.Test
import org.mockito.kotlin.*

internal class PrefsFirstLaunchDataSaverImplTest {

    val sharedPreferencesEditor: SharedPreferences.Editor = mock()
    val sharedPreferences: SharedPreferences = mock {
        on { edit() } doReturn sharedPreferencesEditor
    }

    val tested = PrefsFirstLaunchDataSaverImpl(sharedPreferences)

    @Test
    fun `when saveFirstLaunch is called then verify if data is saved into prefs`() {
        // when
        tested.saveFirstLaunch()

        // then
        verify(sharedPreferences).edit()
        verify(sharedPreferencesEditor).putBoolean(any(), eq(false))
        verify(sharedPreferencesEditor).commit()
    }

    @Test
    fun `when saveFirstLaunch is called then successful response is returned`() {
        // when
        val response = tested.saveFirstLaunch()

        // then
        Assert.assertEquals(true, response is ResponseApi.Success)
    }
}