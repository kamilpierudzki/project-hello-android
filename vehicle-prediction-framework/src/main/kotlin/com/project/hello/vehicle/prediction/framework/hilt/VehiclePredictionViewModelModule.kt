package com.project.hello.vehicle.prediction.framework.hilt

import com.project.hello.vehicle.domain.VehiclePrediction
import com.project.hello.vehicle.domain.analysis.Buffering
import com.project.hello.vehicle.domain.analysis.implementation.BufferingImpl
import com.project.hello.vehicle.domain.steps.*
import com.project.hello.vehicle.domain.steps.implementation.*
import com.project.hello.vehicle.domain.timeout.TimeoutCheckerFactory
import com.project.hello.vehicle.domain.timeout.implementation.TimeoutCheckerFactoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
internal abstract class VehiclePredictionViewModelModule {

    companion object {

        @Provides
        @ViewModelScoped
        fun providePredictedLinesAnalysis(): Buffering = BufferingImpl()

        @Provides
        @ViewModelScoped
        fun provideCountryCharactersProviderImpl() = CountryCharactersProviderImpl()

        @Provides
        @ViewModelScoped
        fun provideCountryCharactersProvider(impl: CountryCharactersProviderImpl): CountryCharactersProvider =
            impl

        @Provides
        @ViewModelScoped
        fun provideCountryCharactersEmitter(impl: CountryCharactersProviderImpl): CountryCharactersEmitter =
            impl

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
        fun provideReduction(): Reduction =
            ReductionImpl()

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
        fun provideTimeoutCheckerFactory(): TimeoutCheckerFactory =
            TimeoutCheckerFactoryImpl(isDebugging = false)
    }
}