package com.project.hello.transit.agency.internal.datamodel

import android.os.Parcelable
import androidx.annotation.Keep
import com.project.hello.transit.agency.VehicleData
import com.project.hello.transit.agency.VehicleType
import kotlinx.parcelize.Parcelize

@Parcelize
@Keep
data class VehicleDataParcelable(override val vehicleTypes: List<VehicleType>) :
    VehicleData,
    Parcelable