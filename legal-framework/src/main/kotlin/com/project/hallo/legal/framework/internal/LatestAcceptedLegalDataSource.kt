package com.project.hallo.legal.framework.internal

import com.project.hallo.commons.domain.repository.Response

interface LatestAcceptedLegalDataSource {
    fun fetchLatestAcceptedLegal(): Response<LegalApi>
}