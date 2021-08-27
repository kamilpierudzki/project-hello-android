package com.project.hello.vehicle.domain.steps

interface CountryCharactersProvider {
    fun get(): Map<String, String>
}