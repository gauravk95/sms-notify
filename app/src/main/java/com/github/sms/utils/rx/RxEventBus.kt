package com.github.sms.utils.rx;

import io.reactivex.annotations.NonNull
import io.reactivex.subjects.PublishSubject
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Consumer

/**
 * A Simple utility class to act as EventBus using RxJava
 */
object RxEventBus {

    private val sSubject = PublishSubject.create<Any>().toSerialized()

    fun subscribe(@NonNull action: Consumer<Any>): Disposable {
        val schedulerProvider = AppSchedulerProvider()
        return sSubject
                .subscribeOn(schedulerProvider.io)
                .observeOn(schedulerProvider.ui)
                .subscribe(action)
    }

    fun publish(@NonNull message: Any) {
        sSubject.onNext(message)
    }

}