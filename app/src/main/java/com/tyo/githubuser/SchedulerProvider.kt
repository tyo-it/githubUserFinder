package com.tyo.githubuser

import io.reactivex.Scheduler

interface SchedulerProvider {
    fun mainThread(): Scheduler
    fun io(): Scheduler
}

class SchedulerProviderImpl(
        private val mainScheduler: Scheduler,
        private val ioScheduler: Scheduler): SchedulerProvider {

    override fun mainThread(): Scheduler = mainScheduler
    override fun io(): Scheduler = ioScheduler
}