package com.project.hello.city.plan.framework.internal.usecase

import com.project.hello.city.plan.domain.model.TransitAgency
import com.project.hello.commons.domain.data.Response
import kotlinx.coroutines.flow.Flow

interface TransitAgencySelectionUseCase {
    fun execute(transitAgency: TransitAgency): Flow<Response<TransitAgency>>
}