package com.project.hello.vehicle.prediction.framework.internal.station.implementation

import com.project.hello.vehicle.prediction.framework.internal.station.Geometry
import com.project.hello.vehicle.prediction.framework.internal.station.Location
import com.project.hello.vehicle.prediction.framework.internal.station.Result
import org.junit.Assert
import org.junit.Test

internal class PositionCalculationImplTest {

    val centralLocation = Location(0.0, 0.0)
    val furtherLocation = Location(20.0, 20.0)
    val resultFurther = Result("A", "", emptyList(), Geometry(furtherLocation))
    val resultCloser = Result("A", "", emptyList(), Geometry(furtherLocation))

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