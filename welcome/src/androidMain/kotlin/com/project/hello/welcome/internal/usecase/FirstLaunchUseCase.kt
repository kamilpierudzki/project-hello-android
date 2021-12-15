package com.project.hello.welcome.internal.usecase

import io.reactivex.rxjava3.core.Single

interface FirstLaunchUseCase {
    fun execute(): Single<Boolean>
}