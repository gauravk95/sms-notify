package com.github.sms.ui.main

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Transformations
import android.arch.paging.LivePagedListBuilder
import android.arch.paging.PagedList
import com.github.sms.base.BaseViewModel
import com.github.sms.data.models.event.ListUpdateAction
import com.github.sms.data.models.event.SmsUpdateAction
import com.github.sms.data.models.event.SmsUpdateEvent
import com.github.sms.data.models.local.SmsItem
import com.github.sms.data.source.factory.SmsDataFactory
import com.github.sms.data.source.repository.AppDataSource
import com.github.sms.data.source.state.LoadingState
import com.github.sms.service.SmsReceiver
import com.github.sms.utils.AppConstants
import com.github.sms.utils.AppLogger
import com.github.sms.utils.lifecycle.SingleLiveEvent
import com.github.sms.utils.rx.SchedulerProvider
import io.reactivex.disposables.CompositeDisposable
import com.github.sms.utils.rx.RxEventBus
import java.util.concurrent.Executors

/**
 * ViewModel for the [MainActivity] screen.
 * The ViewModel works with the [AppDataSource] to get the data.
 */
class MainViewModel constructor(appRepository: AppDataSource,
                                schedulerProvider: SchedulerProvider,
                                compositeDisposable: CompositeDisposable) :
        BaseViewModel(appRepository, schedulerProvider, compositeDisposable) {

    val listUpdateAction: SingleLiveEvent<ListUpdateAction> = SingleLiveEvent()

    /**
     * [highlightMessageTimestamp] is being used to identify selected sms when received by [SmsReceiver]
     *
     * NOTE: When message is received by [SmsReceiver], ICC EF Record Id is not created
     * To find the correct message we use sentTimeStamp and senderAddress, content
     * Maybe better way to do this?
     */
    val highlightMessageTimestamp: MutableLiveData<Long> = MutableLiveData()

    private val smsDataFactory = SmsDataFactory(appRepository, compositeDisposable)

    val smsList by lazy {
        val pagedListConfig = PagedList.Config.Builder()
                .setEnablePlaceholders(false)
                .setInitialLoadSizeHint(AppConstants.PAGE_SIZE)
                .setPageSize(AppConstants.PAGE_SIZE).build()

        LivePagedListBuilder(smsDataFactory, pagedListConfig)
                .setFetchExecutor(Executors.newFixedThreadPool(2))
                .build()
    }

    val loadingState: LiveData<LoadingState> = Transformations.switchMap(smsDataFactory.mutableLiveData) {
        it.loadingState
    }

    init {
        setupEventListeners()
    }

    /**
     * Listen to Events via [RxEventBus]
     */
    private fun setupEventListeners() {

        // Listen for SmsUpdateEvents only
        val disposable = RxEventBus.listen(SmsUpdateEvent::class.java)
                .subscribeOn(schedulerProvider.io)
                .observeOn(schedulerProvider.ui)
                .subscribe {
                    AppLogger.d("RxBus", "Got Message event ${it.action}.")
                    when (it.action) {
                        SmsUpdateAction.UPDATE -> {
                            //update the list
                            smsDataFactory.reset()
                        }
                        SmsUpdateAction.HIGHLIGHT -> {
                            if (it.message != null) {
                                //highlight the message
                                highlightMessageTimestamp.value = it.message.timeSent
                                //smsList.postValue(smsList.value)
                                listUpdateAction.postValue(ListUpdateAction.SCROLL_TO_TOP)
                            }
                        }
                    }
                }

        compositeDisposable.addAll(disposable)
    }

    fun reload() {
        smsDataFactory.reset()
    }

    fun updateHighlight(timeSent: Long?) {
        if (timeSent != null)
            highlightMessageTimestamp.value = timeSent
    }

}