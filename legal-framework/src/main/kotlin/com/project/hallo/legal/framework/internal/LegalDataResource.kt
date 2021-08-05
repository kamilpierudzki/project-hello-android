package com.project.hallo.legal.framework.internal

import com.project.hallo.commons.domain.repository.Response

interface LegalDataResource {
    fun latestAvailableLegal(): Response<LegalApi>
    fun latestAcceptedLegal(): Response<LegalApi>
}