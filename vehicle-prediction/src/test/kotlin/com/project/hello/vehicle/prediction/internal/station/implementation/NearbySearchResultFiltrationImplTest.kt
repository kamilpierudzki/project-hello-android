package com.project.hello.vehicle.prediction.internal.station.implementation

import com.project.hello.vehicle.prediction.internal.station.NearbySearchValues.ParamValue
import com.project.hello.vehicle.prediction.internal.station.model.Geometry
import com.project.hello.vehicle.prediction.internal.station.model.Location
import com.project.hello.vehicle.prediction.internal.station.model.Result
import org.junit.Assert
import org.junit.Test

internal class NearbySearchResultFiltrationImplTest {

    val geometry = Geometry(Location(0.0, 0.0))
    val correctResult =
        Result("", "", listOf("a", ParamValue.PLACE_TYPE_VALUE), geometry)
    val incorrectResult = Result("", "", listOf("a", "b"), geometry)

    val tested = NearbySearchResultFiltrationImpl()

    @Test
    fun `test 1`() {
        // given
        val input = listOf(incorrectResult, correctResult, incorrectResult)

        // when
        val results = tested.filteredTransitStations(input)

        // then
        Assert.assertEquals(1, results.size)
    }

    @Test
    fun `test 2`() {
        // given
        val input = listOf(correctResult, correctResult, correctResult)

        // when
        val results = tested.filteredTransitStations(input)

        // then
        Assert.assertEquals(3, results.size)
    }

    @Test
    fun `test 3`() {
        // given
        val input = listOf(incorrectResult, incorrectResult, incorrectResult)

        // when
        val results = tested.filteredTransitStations(input)

        // then
        Assert.assertEquals(0, results.size)
    }
}