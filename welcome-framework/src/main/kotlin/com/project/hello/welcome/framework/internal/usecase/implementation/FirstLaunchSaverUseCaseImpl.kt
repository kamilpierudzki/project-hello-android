package com.project.hello.welcome.framework.internal.usecase.implementation

import com.project.hello.commons.domain.data.ResponseApi
import com.project.hello.welcome.domain.repository.WelcomeRepository
import com.project.hello.welcome.framework.internal.usecase.FirstLaunchSaverUseCase
import io.reactivex.rxjava3.core.Completable
import javax.inject.Inject

internal class FirstLaunchSaverUseCaseImpl @Inject constructor(
    private val welcomeRepository: WelcomeRepository
) : FirstLaunchSaverUseCase {

    override fun execute(): Completable =
        Completable.create {
            val dataResource = welcomeRepository.getWelcomeResource()
            when (dataResource.saveFirstLaunch()) {
                is ResponseApi.Error -> it.onError(IllegalStateException())
                is ResponseApi.Success -> it.onComplete()
            }
        }
}