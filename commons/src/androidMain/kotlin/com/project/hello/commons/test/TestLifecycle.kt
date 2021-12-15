package com.project.hello.commons.test

import android.annotation.SuppressLint
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry
import org.mockito.Mockito.`when`
import org.mockito.Mockito.spy

abstract class TestLifecycle internal constructor() : LifecycleOwner, Lifecycle() {

    companion object {
        fun create(): TestLifecycle = spy(TestLifecycle::class.java).apply {
            `when`(lifecycle).thenReturn(this)
        }
    }

    @SuppressLint("VisibleForTests")
    private val lifecycleRegistry = LifecycleRegistry.createUnsafe(this)

    private fun handleLifecycleEvent(event: Event) = lifecycleRegistry.handleLifecycleEvent(event)

    override fun addObserver(observer: LifecycleObserver) = lifecycleRegistry.addObserver(observer)

    override fun removeObserver(observer: LifecycleObserver) = lifecycleRegistry.removeObserver(observer)

    override fun getCurrentState() = lifecycleRegistry.currentState

    fun create() = handleLifecycleEvent(Event.ON_CREATE)

    fun start() = handleLifecycleEvent(Event.ON_START)

    fun resume() = handleLifecycleEvent(Event.ON_RESUME)

    fun pause() = handleLifecycleEvent(Event.ON_PAUSE)

    fun stop() = handleLifecycleEvent(Event.ON_STOP)

    fun destroy() = handleLifecycleEvent(Event.ON_DESTROY)
}