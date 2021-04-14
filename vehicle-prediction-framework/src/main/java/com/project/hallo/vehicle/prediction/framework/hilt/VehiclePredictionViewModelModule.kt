package com.project.hallo.vehicle.prediction.framework.hilt

import com.project.hallo.vehicle.domain.VehiclePrediction
import com.project.hallo.vehicle.domain.analysis.PredictedLinesAnalysis
import com.project.hallo.vehicle.domain.steps.*
import com.project.hallo.vehicle.domain.steps.implementation.*
import com.project.hallo.vehicle.prediction.framework.internal.analysis.PredictedLinesAnalysisImpl
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
        fun provideTextMatching(countryCharactersProvider: CountryCharactersProvider): TextMatching =
            TextMatchingImpl(countryCharactersProvider)

        @Provides
        @ViewModelScoped
        fun provideFindingLines(textMatching: TextMatching): FindingLines =
            FindingLinesExtendedImpl(textMatching)

        @Provides
        @ViewModelScoped
        fun provideReduction(): Reduction = ReductionExperimentalImpl()

        @Provides
        @ViewModelScoped
        fun provideFragmentation(): Fragmentation = FragmentationImpl()

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