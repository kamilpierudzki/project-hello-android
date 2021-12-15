package com.project.hello.vehicle.prediction.steps.implementation

import com.project.hello.commons.concurrency.Synchronization
import com.project.hello.vehicle.prediction.steps.CountryCharactersEmitter
import com.project.hello.vehicle.prediction.steps.CountryCharactersProvider

class CountryCharactersProviderImpl(
    private val synchronization: Synchronization
) : CountryCharactersProvider, CountryCharactersEmitter {

    private val countryCharacters = mutableMapOf<String, String>()

    override fun emmit(countryCharacters: Map<String, String>) {
        synchronization.synchronized<Unit>(this) {
            this.countryCharacters.apply {
                clear()
                putAll(countryCharacters)
            }
        }
    }

    override fun get(): Map<String, String> =
        synchronization.synchronized(this) {
            return@synchronized countryCharacters
        }
}