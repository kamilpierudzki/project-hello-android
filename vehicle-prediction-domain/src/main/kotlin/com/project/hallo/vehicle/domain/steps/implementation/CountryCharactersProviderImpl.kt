package com.project.hallo.vehicle.domain.steps.implementation

import com.project.hallo.vehicle.domain.steps.CountryCharactersEmitter
import com.project.hallo.vehicle.domain.steps.CountryCharactersProvider

class CountryCharactersProviderImpl : CountryCharactersProvider, CountryCharactersEmitter {

    private val countryCharacters = mutableMapOf<String, String>()

    override fun emmit(countryCharacters: Map<String, String>) {
        synchronized(this) {
            this.countryCharacters.apply {
                clear()
                putAll(countryCharacters)
            }
        }
    }

    override fun get(): Map<String, String> = synchronized(this) { countryCharacters }
}