package com.project.hello.vehicle.prediction.framework.hilt

import com.project.hello.vehicle.domain.VehiclePrediction
import com.project.hello.vehicle.domain.analysis.PredictedLinesAnalysis
import com.project.hello.vehicle.domain.analysis.implementation.PredictedLinesAnalysisImpl
import com.project.hello.vehicle.domain.steps.*
import com.project.hello.vehicle.domain.steps.implementation.*
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
        fun providePredictedLinesAnalysis(): PredictedLinesAnalysis = PredictedLinesAnalysisImpl()

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
        @ViewModelScoped
        fun provideTextMatching(universalTransformation: UniversalTransformation): TextMatching =
            TextMatchingImpl(universalTransformation)

        @Provides
        @ViewModelScoped
        fun provideUniversalTransformation(
            countryCharactersProvider: CountryCharactersProvider
        ): UniversalTransformation = UniversalTransformationImpl(countryCharactersProvider)

        @Provides
        @ViewModelScoped
        fun provideFindingLines(textMatching: TextMatching): MatchingCityLines =
            MatchingCityLinesImpl(textMatching)

        @Provides
        @ViewModelScoped
        fun provideReduction(): Reduction =
            ReductionImpl()

        @Provides
        fun provideFragmentation(): Fragmentation = FragmentationImpl()

        @Provides
        @ViewModelScoped
        fun provideOutputAnalysis(): OutputAnalysis = OutputAnalysisImpl()

        @Provides
        @ViewModelScoped
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
    }
}