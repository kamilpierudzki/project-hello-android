package com.project.hello.transit.agency.framework.internal.datamodel

import android.os.Parcelable
import androidx.annotation.Keep
import com.project.hello.transit.agency.domain.VehicleData
import com.project.hello.transit.agency.domain.VehicleType
import kotlinx.parcelize.Parcelize

@Parcelize
@Keep
data class VehicleDataParcelable(override val vehicleTypes: List<VehicleType>) :
    VehicleData,
    Parcelable