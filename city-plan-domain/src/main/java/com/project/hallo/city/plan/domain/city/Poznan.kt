package com.project.hallo.city.plan.domain.city

import com.project.hallo.city.plan.domain.LineAPI

class Poznan {

    fun getPlanForTram(): List<LineAPI> {
        return listOf(
            LineAPI("1", listOf("Franowo")),
            LineAPI("1", listOf("Junikowo")),
            LineAPI("2", listOf("Dębiec", "Debiec")),
            LineAPI("2", listOf("Ogrody")),
            LineAPI("3", listOf("Unii Lubelskiej", "UniiLubelskiej")),
            LineAPI("3", listOf("Wilczak")),
            LineAPI("4", listOf("Starołęka", "Starolęka", "Staroleka")),
            LineAPI("4", listOf("Os. Piastowskie", "Os.Piastowskie")),
            LineAPI("5", listOf("Górczyn", "Gorczyn")),
            LineAPI("6", listOf("Junikowo")),
            LineAPI("7", listOf("Os. Piastowskie", "Os.Piastowskie")),
            LineAPI("7", listOf("Ogrody")),
            LineAPI("8", listOf("Miłostowo", "Milostowo")),
            LineAPI("8", listOf("Górczyn", "Gorczyn")),
            LineAPI("9", listOf("Piątkowska", "Piatkowska")),
            LineAPI("9", listOf("Dębiec", "Debiec")),
            LineAPI("10", listOf("Dębiec", "Debiec")),
            LineAPI("10", listOf("Połabska", "Polabska")),
            LineAPI("11", listOf("Unii Lubelskiej", "UniiLubelskiej")),
            LineAPI("11", listOf("Piątkowska", "Piatkowska")),
            LineAPI("12", listOf("Os. Sobieskiego", "Os.Sobieskiego")),
            LineAPI("12", listOf("Starołęka", "Starolęka", "Staroleka")),
            LineAPI("13", listOf("Rondo Rataje", "RondoRataje")),
            LineAPI("13", listOf("Junikowo")),
            LineAPI("14", listOf("Górczyn", "Gorczyn")),
            LineAPI("14", listOf("Os. Sobieskiego", "Os.Sobieskiego")),
            LineAPI("15", listOf("Os. Sobieskiego", "Os.Sobieskiego")),
            LineAPI("15", listOf("Budziszyńska", "Budziszynska")),
            LineAPI("16", listOf("Os. Sobieskiego", "Os.Sobieskiego")),
            LineAPI("16", listOf("Franowo")),
            LineAPI("17", listOf("Ogrody")),
            LineAPI("17", listOf("Starołęka", "Starolęka", "Staroleka")),
            LineAPI("18", listOf("Ogrody")),
            LineAPI("18", listOf("Rondo Rataje", "RondoRataje")),
            LineAPI("36", listOf("Os. Sobieskiego", "Os.Sobieskiego")),
            LineAPI("96", listOf("Franowo")),
            LineAPI("96", listOf("Miłostowo", "Milostowo")),
            LineAPI("201", listOf("Os. Sobieskiego", "Os.Sobieskiego")),
            LineAPI("201", listOf("Unii Lubelskiej", "UniiLubelskiej")),
        )
    }
}