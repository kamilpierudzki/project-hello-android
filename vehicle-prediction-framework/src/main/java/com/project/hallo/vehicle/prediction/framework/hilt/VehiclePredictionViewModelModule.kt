package com.project.hallo.vehicle.prediction.framework.hilt

import com.project.hallo.vehicle.domain.VehiclePrediction
import com.project.hallo.vehicle.domain.steps.implementation.VehiclePredictionImpl
import com.project.hallo.vehicle.prediction.framework.internal.analysis.PredictedLinesAnalysisImpl
import com.project.hallo.vehicle.domain.steps.Fragmentation
import com.project.hallo.vehicle.domain.steps.OutputAnalysis
import com.project.hallo.vehicle.domain.steps.Reduction
import com.project.hallo.vehicle.domain.steps.implementation.FindingLinesImpl
import com.project.hallo.vehicle.domain.steps.implementation.FragmentationImpl
import com.project.hallo.vehicle.domain.steps.implementation.OutputAnalysisImpl
import com.project.hallo.vehicle.domain.steps.implementation.ReductionExperimentalImpl
import com.project.hallo.vehicle.domain.analysis.PredictedLinesAnalysis
import com.project.hallo.vehicle.domain.steps.FindingLines
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
        fun provideFindingLines(): FindingLines = FindingLinesImpl()

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