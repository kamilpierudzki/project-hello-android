package com.project.hello.transit.agency.framework.internal.repository.datasource

import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.runner.AndroidJUnit4
import com.project.hello.commons.domain.data.ResponseApi
import com.project.hello.transit.agency.framework.internal.cryptography.Decoding
import com.project.hello.transit.agency.framework.internal.cryptography.implementation.DecodingImpl
import com.project.hello.transit.agency.framework.internal.model.api.SupportedTransitAgency
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
internal class LocalTransitAgencyDataSourceTest {

    val decoding: Decoding = DecodingImpl()

    lateinit var tested: LocalTransitAgencyDataSource

    @Before
    fun setUp() {
        val resources = InstrumentationRegistry.getInstrumentation().context.resources
        tested = LocalTransitAgencyDataSource(resources, decoding)
    }

    @Test
    fun test_1() {
        val fileId = SupportedTransitAgency.POZNAN.file
        val response = tested.loadTransitAgency(fileId)
        assert(response is ResponseApi.Success)
    }
}