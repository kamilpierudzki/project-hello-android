package com.project.hello.commons.framework.rx

import com.project.hello.commons.framework.RethrowingExceptionHandler
import io.reactivex.rxjava3.android.plugins.RxAndroidPlugins
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.plugins.RxJavaPlugins
import io.reactivex.rxjava3.schedulers.Schedulers
import org.junit.rules.ExternalResource

object ReactiveXUtils {

    private fun setupJavaSchedulers(scheduler: Scheduler) {
        RxJavaPlugins.setIoSchedulerHandler { scheduler }
        RxJavaPlugins.setComputationSchedulerHandler { scheduler }
        RxJavaPlugins.setNewThreadSchedulerHandler { scheduler }
        RxJavaPlugins.setSingleSchedulerHandler { scheduler }
    }

    private fun setupAndroidSchedulers(scheduler: Scheduler) {
        RxAndroidPlugins.setMainThreadSchedulerHandler { scheduler }
        RxAndroidPlugins.setInitMainThreadSchedulerHandler { scheduler }
    }

    private fun setupHandlerForUncaughtExceptions() {
        Thread.setDefaultUncaughtExceptionHandler(RethrowingExceptionHandler)
    }

    private fun resetSchedulers() {
        RxAndroidPlugins.reset()
        RxJavaPlugins.reset()
    }

    fun getTestRule(scheduler: Scheduler = Schedulers.trampoline()) = object : ExternalResource() {
        override fun before() {
            setupAndroidSchedulers(scheduler)
            setupJavaSchedulers(scheduler)
            setupHandlerForUncaughtExceptions()
        }

        override fun after() {
            resetSchedulers()
        }
    }
}