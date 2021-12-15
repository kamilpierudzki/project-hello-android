package com.project.hello.legal.internal.usecase

import com.project.hello.legal.model.LatestAvailableLegal
import io.reactivex.rxjava3.core.Completable

interface LatestAvailableLegalSaverUseCase {
    fun execute(legalToSave: LatestAvailableLegal): Completable
}