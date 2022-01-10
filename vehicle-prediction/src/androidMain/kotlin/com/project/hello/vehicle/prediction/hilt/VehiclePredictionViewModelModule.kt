package com.project.hello.vehicle.prediction.hilt

import android.content.Context
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.SettingsClient
import com.project.hello.commons.concurrency.Synchronization
import com.project.hello.commons.time.SystemTime
import com.project.hello.vehicle.prediction.VehiclePrediction
import com.project.hello.vehicle.prediction.analysis.Buffering
import com.project.hello.vehicle.prediction.analysis.implementation.BufferingImpl
import com.project.hello.vehicle.prediction.internal.station.*
import com.project.hello.vehicle.prediction.internal.station.implementation.*
import com.project.hello.vehicle.prediction.steps.*
import com.project.hello.vehicle.prediction.steps.implementation.*
import com.project.hello.vehicle.prediction.timeout.TimeoutCheckerFactory
import com.project.hello.vehicle.prediction.timeout.implementation.TimeoutCheckerFactoryImpl
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
internal abstract class VehiclePredictionViewModelModule {

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
        fun providePredictedLinesAnalysis(synchronization: Synchronization): Buffering =
            BufferingImpl(synchronization)

        @Provides
        fun provideCountryCharactersProviderImpl(synchronization: Synchronization) =
            CountryCharactersProviderImpl(synchronization)

        @Provides
        fun provideCountryCharactersProvider(impl: CountryCharactersProviderImpl):
                CountryCharactersProvider = impl

        @Provides
        fun provideCountryCharactersEmitter(impl: CountryCharactersProviderImpl):
                CountryCharactersEmitter = impl

        @Provides
        fun provideTextMatching(universalTransformation: UniversalTransformation): TextMatching =
            TextMatchingImpl(universalTransformation)

        @Provides
        fun provideUniversalTransformation(
            countryCharactersProvider: CountryCharactersProvider
        ): UniversalTransformation = UniversalTransformationImpl(countryCharactersProvider)

        @Provides
        fun provideFindingLines(textMatching: TextMatching): MatchingCityLines =
            MatchingCityLinesImpl(textMatching)

        @Provides
        fun provideReduction(): Reduction = ReductionImpl()

        @Provides
        fun provideFragmentation(): Fragmentation = FragmentationImpl()

        @Provides
        fun provideOutputAnalysis(): OutputAnalysis = OutputAnalysisImpl()

        @Provides
        fun provideVehiclePrediction(
            matchingCityLines: MatchingCityLines,
            reduction: Reduction,
            fragmentation: Fragmentation,
            outputAnalysis: OutputAnalysis,
            universalTransformation: UniversalTransformation
        ): VehiclePrediction =
            VehiclePredictionImpl(
                matchingCityLines,
                reduction,
                fragmentation,
                outputAnalysis,
                universalTransformation
            )

        @Provides
        fun provideTimeoutCheckerFactory(systemTime: SystemTime): TimeoutCheckerFactory =
            TimeoutCheckerFactoryImpl(systemTime, isDebugging = false)

        @Provides
        fun provideFusedLocationProviderClient(
            @ApplicationContext appContext: Context
        ): FusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(appContext)

        @Provides
        fun provideGoogleMapsService(): GoogleMapsService {
            val retrofit = Retrofit.Builder()
                .baseUrl("https://maps.googleapis.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
            return retrofit.create(GoogleMapsService::class.java)
        }

        @Provides
        fun provideSettingsClient(
            @ApplicationContext appContext: Context
        ): SettingsClient = LocationServices.getSettingsClient(appContext)
    }
}