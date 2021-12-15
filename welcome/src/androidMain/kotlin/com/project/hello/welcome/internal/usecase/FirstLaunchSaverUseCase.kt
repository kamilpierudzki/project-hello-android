package com.project.hello.welcome.internal.usecase

import io.reactivex.rxjava3.core.Completable

interface FirstLaunchSaverUseCase {
    fun execute(): Completable
}