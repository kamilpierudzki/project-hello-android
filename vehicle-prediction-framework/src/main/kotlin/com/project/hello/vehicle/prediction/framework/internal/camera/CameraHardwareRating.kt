package com.project.hello.vehicle.prediction.framework.internal.camera

import android.hardware.camera2.CameraCharacteristics
import android.hardware.camera2.CameraManager
import javax.inject.Inject

private const val CAMERA_ID = "0"

internal class CameraHardwareRating @Inject constructor(
    private val cameraManager: CameraManager,
) {

    private val ranking: Map<Int, Rating> = hashMapOf(
        CameraCharacteristics.INFO_SUPPORTED_HARDWARE_LEVEL_LEGACY to Rating.LOW_RATING,
        CameraCharacteristics.INFO_SUPPORTED_HARDWARE_LEVEL_LIMITED to Rating.MEDIUM_RATING,
        4 to Rating.MEDIUM_RATING,/*INFO_SUPPORTED_HARDWARE_LEVEL_EXTERNAL*/
        CameraCharacteristics.INFO_SUPPORTED_HARDWARE_LEVEL_FULL to Rating.HIGH_RATING,
        3 to Rating.HIGH_RATING,/*INFO_SUPPORTED_HARDWARE_LEVEL_3*/
    )
    private val cameraCharacteristics: CameraCharacteristics
        get() = cameraManager.getCameraCharacteristics(CAMERA_ID)

    fun getRating(): RatingInfo {
        val level = cameraCharacteristics[CameraCharacteristics.INFO_SUPPORTED_HARDWARE_LEVEL]
        val currentRating = if (level != null) {
            ranking.getOrElse(level) { Rating.LOW_RATING }
        } else {
            Rating.LOW_RATING
        }
        return RatingInfo(currentRating)
    }
}

internal data class RatingInfo(val current: Rating) {

    val max: Int = Rating.values().size
}

internal enum class Rating(val score: Int) {
    HIGH_RATING(3),
    MEDIUM_RATING(2),
    LOW_RATING(1),
}