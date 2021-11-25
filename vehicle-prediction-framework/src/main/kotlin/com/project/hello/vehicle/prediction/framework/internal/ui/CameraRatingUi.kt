package com.project.hello.vehicle.prediction.framework.internal.ui

import android.content.res.Resources
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.project.hello.vehicle.prediction.framework.R
import com.project.hello.vehicle.prediction.framework.internal.camera.CameraHardwareRating
import com.project.hello.vehicle.prediction.framework.internal.camera.Rating
import com.project.hello.vehicle.prediction.framework.internal.camera.RatingInfo
import javax.inject.Inject

private const val RED_COLOR = android.R.color.holo_red_dark
private const val ORANGE_COLOR = android.R.color.holo_orange_dark
private const val GREEN_COLOR = android.R.color.holo_green_dark

internal class CameraRatingUi @Inject constructor(
    private val cameraHardwareRating: CameraHardwareRating,
) {

    fun updateCameraHardwareRatingUi(textView: TextView) {
        val ratingInfo = cameraHardwareRating.getRating()
        textView.apply {
            setTextColor(ContextCompat.getColor(textView.context, getTextColor(ratingInfo)))
            text = getText(ratingInfo)
            contentDescription = getContentDescription(textView.resources, ratingInfo)
        }
    }

    private fun getTextColor(ratingInfo: RatingInfo): Int =
        when (ratingInfo.current) {
            Rating.HIGH_RATING -> GREEN_COLOR
            Rating.MEDIUM_RATING -> ORANGE_COLOR
            Rating.LOW_RATING -> RED_COLOR
        }

    private fun getText(ratingInfo: RatingInfo): String =
        "${ratingInfo.current.score}/${ratingInfo.max}"

    private fun getContentDescription(resources: Resources, ratingInfo: RatingInfo): String =
        resources.getString(
            R.string.vehicle_prediction_camera_rating_content_description,
            ratingInfo.current.score,
            ratingInfo.max
        )
}