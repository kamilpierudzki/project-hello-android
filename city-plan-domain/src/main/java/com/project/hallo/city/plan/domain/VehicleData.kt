package com.project.hallo.city.plan.domain

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class VehicleData(val vehicleTypes: List<VehicleType>) : Parcelable