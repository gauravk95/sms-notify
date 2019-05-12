package com.github.sms.data.source.state

import com.github.sms.R

class LoadingState(val loadingStatus: LoadingStatus, val msg: Int) {
    companion object {
        val RESET: LoadingState = LoadingState(LoadingStatus.RESET, R.string.label_loading_reset)
        val FIRST_LOADED: LoadingState = LoadingState(LoadingStatus.FIRST_SUCCESS, R.string.label_loading_success)
        val FIRST_EMPTY: LoadingState = LoadingState(LoadingStatus.FIRST_EMPTY, R.string.label_loading_success)
        val FIRST_LOADING: LoadingState = LoadingState(LoadingStatus.FIRST_RUNNING, R.string.label_loading_in_progress)
        val FIRST_FAILED: LoadingState = LoadingState(LoadingStatus.FIRST_FAILED, R.string.label_loading_failed)
        val LOADED: LoadingState = LoadingState(LoadingStatus.SUCCESS, R.string.label_loading_success)
        val LOADING: LoadingState = LoadingState(LoadingStatus.RUNNING, R.string.label_loading_in_progress)
        val FAILED: LoadingState = LoadingState(LoadingStatus.FAILED, R.string.label_loading_failed)
    }
}