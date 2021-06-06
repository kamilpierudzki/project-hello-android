package com.project.hallo.city.plan.framework.internal.datamodel

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import com.project.hallo.city.plan.domain.VehicleData
import com.project.hallo.city.plan.domain.VehicleType

@Parcelize
data class VehicleDataParcelable(override val vehicleTypes: List<VehicleType>) :
    VehicleData,
    Parcelable