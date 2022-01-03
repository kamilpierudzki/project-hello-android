package com.project.hello.vehicle.prediction.internal.station.implementation

import com.project.hello.vehicle.prediction.internal.station.model.GeometryAPI
import com.project.hello.vehicle.prediction.internal.station.model.LocationAPI
import com.project.hello.vehicle.prediction.internal.station.model.ResultAPI
import org.junit.Assert
import org.junit.Test

internal class PositionCalculationImplTest {

    val centralLocation = LocationAPI(0.0, 0.0)
    val furtherLocation = LocationAPI(20.0, 20.0)
    val resultFurther = ResultAPI("A", "", emptyList(), GeometryAPI(furtherLocation))
    val resultCloser = ResultAPI("A", "", emptyList(), GeometryAPI(furtherLocation))

    val tested = PositionCalculationImpl()

    @Test
    fun `test 1`() {
        //given
        val input = listOf(resultCloser)

        // when
        val result = tested.findTheClosestResultOrNull(centralLocation, input)

        // then
        Assert.assertEquals(resultCloser, result)
    }

    @Test
    fun `test 2`() {
        //given
        val input = listOf(resultCloser, resultFurther)

        // when
        val result = tested.findTheClosestResultOrNull(centralLocation, input)

        // then
        Assert.assertEquals(resultCloser, result)
    }

    @Test
    fun `test 3`() {
        //given
        val input = listOf(resultCloser, resultCloser)

        // when
        val result = tested.findTheClosestResultOrNull(centralLocation, input)

        // then
        Assert.assertEquals(resultCloser, result)
    }

    @Test
    fun `test 4`() {
        //given
        val input = listOf(resultFurther, resultFurther)

        // when
        val result = tested.findTheClosestResultOrNull(centralLocation, input)

        // then
        Assert.assertEquals(resultFurther, result)
    }

    @Test
    fun `test 5`() {
        //given
        val input = listOf(resultFurther, resultCloser, resultFurther)

        // when
        val result = tested.findTheClosestResultOrNull(centralLocation, input)

        // then
        Assert.assertEquals(resultCloser, result)
    }
}