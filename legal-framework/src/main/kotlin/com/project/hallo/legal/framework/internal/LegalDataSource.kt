package com.project.hallo.legal.framework.internal

import com.project.hallo.commons.domain.repository.Response

interface LegalDataSource {
    fun fetchAvailableLegal(): Response<LegalApi>
}