package com.project.hello.vehicle.prediction.framework.internal.station.implementation

import com.project.hello.transit.agency.domain.model.Stop
import com.project.hello.transit.agency.domain.model.TransitAgency
import org.junit.Assert
import org.junit.Test

internal class TransitStationFiltrationImplTest {

    val baseTransitAgency = TransitAgency(
        "",
        "",
        emptyList(),
        emptyList(),
        emptyList(),
        emptyList()
    )

    val stop1 = Stop("A", listOf("1"))
    val stop2 = Stop("B", listOf("1", "2"))
    val stop3 = Stop("C", listOf("1", "2", "3"))

    val tested = TransitStationFiltrationImpl()

    @Test
    fun `test 1`() {
        // given
        val ta = baseTransitAgency.copy(
            tramStops = listOf(stop1, stop2, stop3),
        )

        // when
        val result = tested.filteredStationsWithName("B", ta)

        // then
        Assert.assertEquals(1, result.filteredTramStops.size)
        Assert.assertEquals(2, result.filteredTramStops[0].lines.size)
        Assert.assertEquals("1", result.filteredTramStops[0].lines[0])
        Assert.assertEquals("2", result.filteredTramStops[0].lines[1])
    }

    @Test
    fun `test 2`() {
        // given
        val ta = baseTransitAgency.copy(
            tramStops = listOf(stop1, stop2, stop1, stop3, stop1),
        )

        // when
        val result = tested.filteredStationsWithName("A", ta)

        // then
        Assert.assertEquals(3, result.filteredTramStops.size)
    }

    @Test
    fun `test 3`() {
        // given
        val ta = baseTransitAgency.copy(
            tramStops = listOf(),
        )

        // when
        val result = tested.filteredStationsWithName("A", ta)

        // then
        Assert.assertEquals(0, result.filteredTramStops.size)
    }
}