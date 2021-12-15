package com.project.hello.vehicle.prediction.steps

interface CountryCharactersProvider {
    fun get(): Map<String, String>
}