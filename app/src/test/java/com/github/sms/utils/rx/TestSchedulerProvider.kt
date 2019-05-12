package com.github.sms.utils.rx

import io.reactivex.Scheduler
import io.reactivex.schedulers.Schedulers

/**
 * Created by gk
 */

class TestSchedulerProvider : SchedulerProvider {

    override val ui: Scheduler = Schedulers.trampoline()

    override val computation: Scheduler = Schedulers.trampoline()

    override val io: Scheduler = Schedulers.trampoline()

    override val trampoline: Scheduler = Schedulers.trampoline()

}
