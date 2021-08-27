package com.project.hello.vehicle.prediction.framework.hilt

import com.project.hello.vehicle.domain.VehiclePrediction
import com.project.hello.vehicle.domain.analysis.PredictedLinesAnalysis
import com.project.hello.vehicle.domain.steps.*
import com.project.hello.vehicle.domain.steps.implementation.*
import com.project.hello.vehicle.prediction.framework.internal.analysis.PredictedLinesAnalysisImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
internal abstract class VehiclePredictionViewModelModule {

    @Binds
    abstract fun bindPredictedLinesAnalysis(impl: PredictedLinesAnalysisImpl): PredictedLinesAnalysis

    companion object {

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
        fun provideFindingLines(textMatching: TextMatching): FindingLines =
            FindingLinesExtendedImpl(textMatching)

        @Provides
        @ViewModelScoped
        fun provideReduction(universalTransformation: UniversalTransformation): Reduction =
            ReductionExperimentalImpl(universalTransformation)

        @Provides
        @ViewModelScoped
        fun provideFragmentation(universalTransformation: UniversalTransformation): Fragmentation =
            FragmentationImpl(universalTransformation)

        @Provides
        @ViewModelScoped
        fun provideOutputAnalysis(): OutputAnalysis = OutputAnalysisImpl()

        @Provides
        @ViewModelScoped
        fun provideVehiclePrediction(
            findingLines: FindingLines,
            reduction: Reduction,
            fragmentation: Fragmentation,
            outputAnalysis: OutputAnalysis
        ): VehiclePrediction =
            VehiclePredictionImpl(findingLines, reduction, fragmentation, outputAnalysis)
    }
}