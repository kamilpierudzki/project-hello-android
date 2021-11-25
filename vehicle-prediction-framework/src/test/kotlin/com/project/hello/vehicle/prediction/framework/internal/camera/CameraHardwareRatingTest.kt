package com.project.hello.vehicle.prediction.framework.internal.camera

import android.hardware.camera2.CameraCharacteristics
import android.hardware.camera2.CameraManager
import org.junit.Assert
import org.junit.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

internal class CameraHardwareRatingTest {

    val cameraCharacteristics: CameraCharacteristics = mock()
    val cameraManager: CameraManager = mock {
        on { getCameraCharacteristics(any()) } doReturn cameraCharacteristics
    }

    val tested = CameraHardwareRating(cameraManager)

    @Test
    fun test_1() {
        // given
        whenever(cameraCharacteristics.get(CameraCharacteristics.INFO_SUPPORTED_HARDWARE_LEVEL))
            .thenReturn(CameraCharacteristics.INFO_SUPPORTED_HARDWARE_LEVEL_LEGACY)

        // when
        val ratingInfo = tested.getRating()

        // then
        Assert.assertEquals(Rating.LOW_RATING, ratingInfo.current)
    }

    @Test
    fun test_2() {
        // given
        whenever(cameraCharacteristics.get(CameraCharacteristics.INFO_SUPPORTED_HARDWARE_LEVEL))
            .thenReturn(CameraCharacteristics.INFO_SUPPORTED_HARDWARE_LEVEL_LIMITED)

        // when
        val ratingInfo = tested.getRating()

        // then
        Assert.assertEquals(Rating.MEDIUM_RATING, ratingInfo.current)
    }

    @Test
    fun test_3() {
        // given
        whenever(cameraCharacteristics.get(CameraCharacteristics.INFO_SUPPORTED_HARDWARE_LEVEL))
            .thenReturn(4)

        // when
        val ratingInfo = tested.getRating()

        // then
        Assert.assertEquals(Rating.MEDIUM_RATING, ratingInfo.current)
    }

    @Test
    fun test_4() {
        // given
        whenever(cameraCharacteristics.get(CameraCharacteristics.INFO_SUPPORTED_HARDWARE_LEVEL))
            .thenReturn(CameraCharacteristics.INFO_SUPPORTED_HARDWARE_LEVEL_FULL)

        // when
        val ratingInfo = tested.getRating()

        // then
        Assert.assertEquals(Rating.HIGH_RATING, ratingInfo.current)
    }

    @Test
    fun test_5() {
        // given
        whenever(cameraCharacteristics.get(CameraCharacteristics.INFO_SUPPORTED_HARDWARE_LEVEL))
            .thenReturn(3)

        // when
        val ratingInfo = tested.getRating()

        // then
        Assert.assertEquals(Rating.HIGH_RATING, ratingInfo.current)
    }

    @Test
    fun test_6() {
        // given
        whenever(cameraCharacteristics.get(CameraCharacteristics.INFO_SUPPORTED_HARDWARE_LEVEL))
            .thenReturn(-1)

        // when
        val ratingInfo = tested.getRating()

        // then
        Assert.assertEquals(Rating.LOW_RATING, ratingInfo.current)
    }

    @Test
    fun test_8() {
        // given
        whenever(cameraCharacteristics.get(CameraCharacteristics.INFO_SUPPORTED_HARDWARE_LEVEL))
            .thenReturn(CameraCharacteristics.INFO_SUPPORTED_HARDWARE_LEVEL_FULL)

        // when
        val ratingInfo = tested.getRating()

        // then
        Assert.assertEquals(3, ratingInfo.max)
    }
}