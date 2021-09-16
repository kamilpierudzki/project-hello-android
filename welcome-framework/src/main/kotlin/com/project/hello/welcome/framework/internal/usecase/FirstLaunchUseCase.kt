package com.project.hello.welcome.framework.internal.usecase

import io.reactivex.rxjava3.core.Single

interface FirstLaunchUseCase {
    fun execute(): Single<Boolean>
}