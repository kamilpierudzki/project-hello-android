package com.project.hallo.vehicle.domain.steps

interface CountryCharactersProvider {
    fun get(): Map<String, String>
}