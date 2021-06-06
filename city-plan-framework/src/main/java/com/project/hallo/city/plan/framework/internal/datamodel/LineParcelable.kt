package com.project.hallo.city.plan.framework.internal.datamodel

import android.os.Parcelable
import com.project.hallo.city.plan.domain.model.ILine
import kotlinx.parcelize.Parcelize

@Parcelize
data class LineParcelable(
    override val number: String,
    override val destination: String
) : ILine, Parcelable