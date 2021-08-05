package com.project.hallo.legal.framework.internal

import com.project.hallo.commons.domain.repository.Response
import javax.inject.Inject

internal class LatestAcceptedLegalDataSourceImpl @Inject constructor(

) : LatestAcceptedLegalDataSource {

    override fun fetchLatestAcceptedLegal(): Response<LegalApi> {
        TODO("Not yet implemented")
    }
}