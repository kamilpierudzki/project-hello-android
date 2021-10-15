package com.project.hello.transit.agency.framework.internal.datasource.resources.implementation

import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.runner.AndroidJUnit4
import com.project.hello.transit.agency.framework.internal.cryptography.Decoding
import com.project.hello.transit.agency.framework.internal.cryptography.implementation.DecodingImpl
import com.project.hello.transit.agency.framework.internal.model.api.SupportedTransitAgency
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
internal class RawResourcesTransitAgencyDataSourceImplTest {

    val decoding: Decoding = DecodingImpl()

    lateinit var tested: RawResourcesTransitAgencyDataSourceImpl

    @Before
    fun setUp() {
        val resources = InstrumentationRegistry.getInstrumentation().context.resources
        tested = RawResourcesTransitAgencyDataSourceImpl(resources, decoding)
    }

    @Test
    fun test_1() {
        val fileId = SupportedTransitAgency.POZNAN.file
        tested.fetchTransitAgencyData(fileId)
    }
}