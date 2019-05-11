package com.github.sms.utils.rx;

import io.reactivex.annotations.NonNull
import io.reactivex.subjects.PublishSubject
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Consumer

/**
 * A Simple utility class to act as EventBus using RxJava
 */
class RxEventBus {

    companion object {
        private val sSubject = PublishSubject.create<Any>()

        fun subscribe(@NonNull action: Consumer<Any>): Disposable {
            return sSubject.subscribe(action)
        }

        fun publish(@NonNull message: Any) {
            sSubject.onNext(message)
        }
    }

}