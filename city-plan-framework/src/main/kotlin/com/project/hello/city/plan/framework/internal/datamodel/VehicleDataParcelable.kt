package com.project.hello.city.plan.framework.internal.datamodel

import android.os.Parcelable
import androidx.annotation.Keep
import com.project.hello.city.plan.domain.VehicleData
import com.project.hello.city.plan.domain.VehicleType
import kotlinx.parcelize.Parcelize

@Parcelize
@Keep
data class VehicleDataParcelable(override val vehicleTypes: List<VehicleType>) :
    VehicleData,
    Parcelable