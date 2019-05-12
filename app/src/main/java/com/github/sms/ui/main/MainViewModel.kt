package com.github.sms.ui.main

import android.arch.lifecycle.MutableLiveData
import com.github.sms.R
import com.github.sms.base.BaseViewModel
import com.github.sms.data.models.event.ListUpdateAction
import com.github.sms.data.models.event.SmsUpdateAction
import com.github.sms.data.models.event.SmsUpdateEvent
import com.github.sms.data.models.local.SmsHolder
import com.github.sms.data.models.local.SmsItem
import com.github.sms.data.source.repository.AppDataSource
import com.github.sms.service.SmsReceiver
import com.github.sms.utils.lifecycle.SingleLiveEvent
import com.github.sms.utils.rx.SchedulerProvider
import io.reactivex.disposables.CompositeDisposable
import com.github.sms.utils.rx.RxEventBus
import io.reactivex.functions.Consumer

/**
 * ViewModel for the [MainActivity] screen.
 * The ViewModel works with the [AppDataSource] to get the data.
 */
class MainViewModel constructor(appRepository: AppDataSource,
                                schedulerProvider: SchedulerProvider,
                                compositeDisposable: CompositeDisposable) :
        BaseViewModel(appRepository, schedulerProvider, compositeDisposable) {

    val itemList: MutableLiveData<List<SmsHolder>> = MutableLiveData()
    val listUpdateAction: SingleLiveEvent<ListUpdateAction> = SingleLiveEvent()

    var highlightTargetMessage: SmsItem? = null

    init {
        setupEventListeners()
    }

    /**
     * Listen to Events via [RxEventBus]
     */
    private fun setupEventListeners() {
        val disposable = RxEventBus.subscribe(Consumer {
            if (it is SmsUpdateEvent) {
                when (it.action) {
                    SmsUpdateAction.UPDATE -> {
                        //update the list
                        loadSmsList(true)
                    }
                    SmsUpdateAction.HIGHLIGHT -> {
                        //make the starting highlightTarget null
                        highlightTargetMessage = null
                        //highlight the new message
                        highlightTargetSms(it.message, itemList.value)
                        itemList.postValue(itemList.value)
                        listUpdateAction.postValue(ListUpdateAction.SCROLL_TO_TOP)
                    }
                }
            }
        })

        compositeDisposable.addAll(disposable)
    }

    /**
     * Loads the sms
     */
    fun loadSmsList(forceRefresh: Boolean) {
        isLoading().value = true

        val disposable = dataSource.getSmsItemList(forceRefresh)
                .subscribeOn(schedulerProvider.io)
                .observeOn(schedulerProvider.ui)
                .subscribe({
                    if (highlightTargetMessage != null)
                        highlightTargetSms(highlightTargetMessage, it)
                    isLoading().value = false
                    itemList.value = it
                }, {
                    isLoading().value = false
                    getErrorMsg().value = R.string.default_error_message
                })

        compositeDisposable.add(disposable)
    }

    /**
     * Finds the Sms with [message] and highlights it
     *
     * NOTE: When message is received by [SmsReceiver], ICC EF Record Id is not created
     * To find the correct message we use sentTimeStamp and senderAddress, content can also help
     * Maybe better way to do this?
     */
    private fun highlightTargetSms(message: SmsItem?, currentSmsList: List<SmsHolder>?) {
        if (message == null) return

        if (currentSmsList != null) {
            for (item in currentSmsList)
                if (item is SmsItem)
                    item.isHighlighted =
                            (item.timeSent == message.timeSent
                                    && item.address == message.address)
        }
    }

}