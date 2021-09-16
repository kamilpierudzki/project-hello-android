package com.project.hello.welcome.framework.internal.usecase

import io.reactivex.rxjava3.core.Completable

interface FirstLaunchSaverUseCase {
    fun execute(): Completable
}