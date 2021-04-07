package com.project.halo.vehicle.prediction.framework.hilt

import com.project.halo.vehicle.domain.VehiclePredictionImpl
import com.project.halo.vehicle.domain.analysis.PredictedLinesAnalysisImpl
import com.project.halo.vehicle.domain.steps.*
import com.project.halo.vehicle.prediction.data.VehiclePrediction
import com.project.halo.vehicle.prediction.data.analysis.PredictedLinesAnalysis
import com.project.halo.vehicle.prediction.data.steps.FindingLines
import com.project.halo.vehicle.prediction.data.steps.Fragmentation
import com.project.halo.vehicle.prediction.data.steps.OutputAnalysis
import com.project.halo.vehicle.prediction.data.steps.Reduction
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
internal class VehiclePredictionViewModelModule {

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
    fun providePredictedLinesAnalysis(): PredictedLinesAnalysis = PredictedLinesAnalysisImpl()

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