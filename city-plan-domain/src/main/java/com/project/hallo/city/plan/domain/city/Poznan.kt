package com.project.hallo.city.plan.domain.city

import com.project.hallo.city.plan.domain.Line
import com.project.hallo.city.plan.domain.VehicleType

class Poznan {

    fun getPlanForTram(): List<Line> {
        val t = VehicleType.TRAM
        return listOf(
            Line("1", listOf("Franowo"), t),
            Line("1", listOf("Junikowo"), t),
            Line("2", listOf("Dębiec"), t),
            Line("2", listOf("Ogrody"), t),
            Line("3", listOf("Unii Lubelskiej", "UniiLubelskiej"), t),
            Line("3", listOf("Wilczak"), t),
            Line("4", listOf("Starołęka", "Starolęka", "Staroleka"), t),
            Line("4", listOf("Os. Piastowskie", "Os.Piastowskie"), t),
            Line("5", listOf("Górczyn", "Gorczyn"), t),
            Line("6", listOf("Junikowo"), t),
            Line("7", listOf("Os. Piastowskie", "Os.Piastowskie"), t),
            Line("7", listOf("Ogrody"), t),
            Line("8", listOf("Miłostowo", "Milostowo"), t),
            Line("8", listOf("Górczyn", "Gorczyn"), t),
            Line("9", listOf("Piątkowska", "Piatkowska"), t),
            Line("9", listOf("Dębiec", "Debiec"), t),
            Line("10", listOf("Dębiec", "Debiec"), t),
            Line("10", listOf("Połabska", "Polabska"), t),
            Line("11", listOf("Unii Lubelskiej", "UniiLubelskiej"), t),
            Line("11", listOf("Piątkowska", "Piatkowska"), t),
            Line("12", listOf("Os. Sobieskiego", "Os.Sobieskiego"), t),
            Line("12", listOf("Starołęka", "Starolęka", "Staroleka"), t),
            Line("13", listOf("Rondo Rataje", "RondoRataje"), t),
            Line("13", listOf("Junikowo"), t),
            Line("14", listOf("Górczyn", "Gorczyn"), t),
            Line("14", listOf("Os. Sobieskiego", "Os.Sobieskiego"), t),
            Line("15", listOf("Os. Sobieskiego", "Os.Sobieskiego"), t),
            Line("15", listOf("Budziszyńska", "Budziszynska"), t),
            Line("16", listOf("Os. Sobieskiego", "Os.Sobieskiego"), t),
            Line("16", listOf("Franowo"), t),
            Line("17", listOf("Ogrody"), t),
            Line("17", listOf("Starołęka", "Starolęka", "Staroleka"), t),
            Line("18", listOf("Ogrody"), t),
            Line("18", listOf("Rondo Rataje", "RondoRataje"), t),
            Line("36", listOf("Os. Sobieskiego", "Os.Sobieskiego"), t),
            Line("96", listOf("Franowo"), t),
            Line("96", listOf("Miłostowo", "Milostowo"), t),
            Line("201", listOf("Os. Sobieskiego", "Os.Sobieskiego"), t),
            Line("201", listOf("Unii Lubelskiej", "UniiLubelskiej"), t),
        )
    }
}