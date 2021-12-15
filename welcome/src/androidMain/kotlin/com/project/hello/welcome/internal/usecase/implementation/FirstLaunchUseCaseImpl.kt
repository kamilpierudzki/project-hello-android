package com.project.hello.welcome.internal.usecase.implementation

import com.project.hello.commons.data.ResponseApi
import com.project.hello.welcome.repository.WelcomeRepository
import com.project.hello.welcome.internal.usecase.FirstLaunchUseCase
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

internal class FirstLaunchUseCaseImpl @Inject constructor(
    private val welcomeRepository: WelcomeRepository
) : FirstLaunchUseCase {

    override fun execute(): Single<Boolean> {
        return Single.create {
            val dataResource = welcomeRepository.getWelcomeResource()
            val response = when (val responseApi = dataResource.isFirstLaunch()) {
                is ResponseApi.Error -> true
                is ResponseApi.Success -> responseApi.successData
            }
            it.onSuccess(response)
        }
    }
}