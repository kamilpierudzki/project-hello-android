package com.project.hello.transit.station.framework.hilt

import android.content.Context
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.project.hello.transit.station.framework.api.LocationUseCase
import com.project.hello.transit.station.framework.api.TransitStationUseCase
import com.project.hello.transit.station.framework.internal.*
import com.project.hello.transit.station.framework.internal.GoogleMapsService
import com.project.hello.transit.station.framework.internal.NearbySearchOptionsFactory
import com.project.hello.transit.station.framework.internal.NearbySearchResultFiltration
import com.project.hello.transit.station.framework.internal.NearbySearchUseCase
import com.project.hello.transit.station.framework.internal.PositionCalculation
import com.project.hello.transit.station.framework.internal.implementation.*
import com.project.hello.transit.station.framework.internal.implementation.LocationUseCaseImpl
import com.project.hello.transit.station.framework.internal.implementation.NearbySearchOptionsFactoryImpl
import com.project.hello.transit.station.framework.internal.implementation.NearbySearchUseCaseImpl
import com.project.hello.transit.station.framework.internal.implementation.NearbySearchResultFiltrationImpl
import com.project.hello.transit.station.framework.internal.implementation.TransitStationUseCaseImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


@Module
@InstallIn(ViewModelComponent::class)
internal abstract class TransitStationViewModelModule {

    @Binds
    abstract fun bindTransitStationUseCase(impl: TransitStationUseCaseImpl): TransitStationUseCase

    @Binds
    abstract fun bindLocationUseCase(impl: LocationUseCaseImpl): LocationUseCase

    @Binds
    abstract fun bindNearbySearchOptionsFactory(impl: NearbySearchOptionsFactoryImpl):
            NearbySearchOptionsFactory

    @Binds
    abstract fun bindNearbySearchUseCase(impl: NearbySearchUseCaseImpl): NearbySearchUseCase

    @Binds
    abstract fun bindNearbySearchResultFiltration(impl: NearbySearchResultFiltrationImpl):
            NearbySearchResultFiltration

    @Binds
    abstract fun bindPositionCalculation(impl: PositionCalculationImpl): PositionCalculation

    @Binds
    abstract fun bindTransitStationFiltration(impl: TransitStationFiltrationImpl):
            TransitStationFiltration

    companion object {
        @Provides
        fun provideFusedLocationProviderClient(
            @ApplicationContext appContext: Context
        ): FusedLocationProviderClient {
            return LocationServices.getFusedLocationProviderClient(appContext)
        }

        @Provides
        fun provideGoogleMapsService(): GoogleMapsService {
            val retrofit = Retrofit.Builder()
                .baseUrl("https://maps.googleapis.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
            return retrofit.create(GoogleMapsService::class.java)
        }
    }
}