package com.project.hello.legal.internal

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.project.hello.commons.data.Response
import com.project.hello.commons.data.toSuccessResponse
import com.project.hello.commons.livedata.Event
import com.project.hello.commons.rx.ReactiveXUtils
import com.project.hello.legal.api.LatestAvailableLegalResult
import com.project.hello.legal.internal.usecase.LatestAcceptedLegalVersionUseCase
import com.project.hello.legal.internal.usecase.LatestAvailableLegalSaverUseCase
import com.project.hello.legal.internal.usecase.LatestAvailableLegalUseCase
import com.project.hello.legal.model.LatestAvailableLegal
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.subjects.PublishSubject
import org.junit.Rule
import org.junit.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

internal class LegalViewModelImplTest {

    @get:Rule
    val liveDataRule = InstantTaskExecutorRule()

    @get:Rule
    val rxRule = ReactiveXUtils.getTestRule()

    val legalVersion = 1
    val latestAvailableLegal = LatestAvailableLegal(legalVersion, "")

    val latestAcceptedLegalVersionUseCaseSubject = PublishSubject.create<Response<Int>>()
    val latestAcceptedLegalVersionUseCase: LatestAcceptedLegalVersionUseCase = mock {
        on { execute() } doReturn latestAcceptedLegalVersionUseCaseSubject
    }
    val latestAvailableLegalUseCaseSubject = PublishSubject.create<Response<LatestAvailableLegal>>()
    val latestAvailableLegalUseCase: LatestAvailableLegalUseCase = mock {
        on { execute() } doReturn latestAvailableLegalUseCaseSubject
    }

    val latestAvailableLegalSaverUseCase: LatestAvailableLegalSaverUseCase = mock()

    fun tested(
        latestAcceptedLegalVersionUseCase: LatestAcceptedLegalVersionUseCase = this.latestAcceptedLegalVersionUseCase,
        latestAvailableLegalUseCase: LatestAvailableLegalUseCase = this.latestAvailableLegalUseCase,
        latestAvailableLegalSaverUseCase: LatestAvailableLegalSaverUseCase = this.latestAvailableLegalSaverUseCase
    ): LegalViewModelImpl {
        return LegalViewModelImpl(
            latestAcceptedLegalVersionUseCase,
            latestAvailableLegalUseCase,
            latestAvailableLegalSaverUseCase
        )
    }

    @Test
    fun `given legal info is available, latest legal is accepted and observing isLatestAvailableLegalAccepted when initialised then positive event is sent`() {
        // given
        val tested = tested()
        latestAvailableLegalUseCaseSubject.onNext(latestAvailableLegal.toSuccessResponse())
        latestAcceptedLegalVersionUseCaseSubject.onNext(legalVersion.toSuccessResponse())

        // when
        val observer = mutableListOf<Event<Boolean>>()
        tested.isLatestAvailableLegalAccepted.observeForever { observer.add(it) }

        // then
        assert(observer.size == 1)
        assert(observer[0].content)
    }

    @Test
    fun `given legal info is available, latest legal is NOT accepted and observing inLatestAvailableLegalAccepted when initialised then negative event is sent`() {
        // given
        val tested = tested()
        latestAvailableLegalUseCaseSubject.onNext(latestAvailableLegal.toSuccessResponse())
        latestAcceptedLegalVersionUseCaseSubject.onNext((legalVersion - 1).toSuccessResponse())

        // when
        val observer = mutableListOf<Event<Boolean>>()
        tested.isLatestAvailableLegalAccepted.observeForever { observer.add(it) }

        // then
        assert(observer.size == 1)
        assert(!observer[0].content)
    }

    @Test
    fun `given legal info is available and observing latestAvailableLegal when initialised then successful data is saved`() {
        // given
        val tested = tested()
        latestAvailableLegalUseCaseSubject.onNext(latestAvailableLegal.toSuccessResponse())

        // when
        val observer = mutableListOf<LatestAvailableLegal>()
        tested.latestAvailableLegal.observeForever { observer.add(it) }

        // then
        assert(observer.size == 1)
        assert(observer[0] == latestAvailableLegal)
    }

    @Test
    fun `given legal data is available, saving data completes successfully and observing latestAvailableLegalSavedResult when onLatestAvailableLegalAccepted is called then positive result is saved`() {
        // given
        val tested = tested()
        latestAvailableLegalUseCaseSubject.onNext(latestAvailableLegal.toSuccessResponse())
        whenever(latestAvailableLegalSaverUseCase.execute(any())).thenReturn(Completable.complete())
        val observer = mutableListOf<Event<LatestAvailableLegalResult>>()
        tested.latestAvailableLegalSavedResult.observeForever { observer.add(it) }

        // when
        tested.onLatestAvailableLegalAccepted()

        // then
        assert(observer.size == 1)
        assert(observer[0].content == LatestAvailableLegalResult.Success)
    }

    @Test
    fun `given legal data is available, saving data fails and observing latestAvailableLegalSavedResult when onLatestAvailableLegalAccepted is called then negative result is saved`() {
        // given
        val tested = tested()
        latestAvailableLegalUseCaseSubject.onNext(latestAvailableLegal.toSuccessResponse())
        whenever(latestAvailableLegalSaverUseCase.execute(any())).thenReturn(
            Completable.error(IllegalStateException(""))
        )
        val observer = mutableListOf<Event<LatestAvailableLegalResult>>()
        tested.latestAvailableLegalSavedResult.observeForever { observer.add(it) }

        // when
        tested.onLatestAvailableLegalAccepted()

        // then
        assert(observer.size == 1)
        assert(observer[0].content is LatestAvailableLegalResult.Error)
    }
}