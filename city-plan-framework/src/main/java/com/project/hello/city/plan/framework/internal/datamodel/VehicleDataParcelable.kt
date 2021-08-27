package com.project.hello.city.plan.framework.internal.datamodel

import android.os.Parcelable
import com.project.hello.city.plan.domain.VehicleData
import com.project.hello.city.plan.domain.VehicleType
import kotlinx.parcelize.Parcelize

@Parcelize
data class VehicleDataParcelable(override val vehicleTypes: List<VehicleType>) :
    VehicleData,
    Parcelable