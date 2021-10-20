package com.project.hello.transit.agency.framework.internal.repository

import com.project.hello.commons.domain.data.ResponseApi
import com.project.hello.transit.agency.domain.repository.datasource.SelectedTransitAgencyDataSource
import com.project.hello.transit.agency.domain.repository.datasource.SupportedTransitAgenciesDataSource
import com.project.hello.transit.agency.framework.internal.model.api.TransitAgencyAPI
import com.project.hello.transit.agency.framework.internal.repository.datasource.LocalTransitAgencyDataSource
import org.junit.Assert
import org.junit.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

internal class TransitAgencyPlanRepositoryImplTest {

    val localTransitAgencyDataSource: LocalTransitAgencyDataSource = mock()
    val selectedTransitAgencyDataSource: SelectedTransitAgencyDataSource = mock()
    val localSupportedTransitAgenciesDataSource: SupportedTransitAgenciesDataSource = mock()

    val transitAgencyApi1 = createTransitAgencyAPI("A")
    val response1 = ResponseApi.Success(transitAgencyApi1)
    val transitAgencyApi2 = createTransitAgencyAPI("B")
    val response2 = ResponseApi.Success(transitAgencyApi2)
    val response3 = ResponseApi.Error<TransitAgencyAPI>("")

    val tested = TransitAgencyPlanRepositoryImpl(
        localTransitAgencyDataSource,
        selectedTransitAgencyDataSource,
        localSupportedTransitAgenciesDataSource
    )

    fun createTransitAgencyAPI(transitAgency: String) = TransitAgencyAPI(
        transitAgency, 0, "", 1,
        emptyList(),
        emptyList(),
        emptyList(),
        emptyList()
    )

    @Test
    fun `given all files are loaded successfully when loadTransitAgencies is called then result list contains data`() {
        // given
        whenever(localSupportedTransitAgenciesDataSource.loadSupportedTransitAgenciesFileResources())
            .thenReturn(listOf(0, 1))
        whenever(localTransitAgencyDataSource.loadTransitAgency(0)).thenReturn(response1)
        whenever(localTransitAgencyDataSource.loadTransitAgency(1)).thenReturn(response2)

        // when
        val result = tested.loadTransitAgencies()

        // then
        Assert.assertEquals(2, result.size)
    }

    @Test
    fun `given some files are NOT loaded successfully when loadTransitAgencies is called then result list contains data`() {
        // given
        whenever(localSupportedTransitAgenciesDataSource.loadSupportedTransitAgenciesFileResources())
            .thenReturn(listOf(0, 1))
        whenever(localTransitAgencyDataSource.loadTransitAgency(0)).thenReturn(response1)
        whenever(localTransitAgencyDataSource.loadTransitAgency(1)).thenReturn(response3)

        // when
        val result = tested.loadTransitAgencies()

        // then
        Assert.assertEquals(1, result.size)
    }

    @Test
    fun `when getCurrentlySelectedTransitAgency is called then selected transit agency is loaded`() {
        // when
        tested.getCurrentlySelectedTransitAgency()

        // then
        verify(selectedTransitAgencyDataSource).loadTransitAgency()
    }

    @Test
    fun `when saveCurrentlySelectedTransitAgency is called then selected transit agency is saved`() {
        // when
        tested.saveCurrentlySelectedTransitAgency(mock())

        // then
        verify(selectedTransitAgencyDataSource).saveTransitAgency(any())
    }
}