package com.i.player.main.splash

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.i.player.main.BaseViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent

class SplashViewModel : BaseViewModel(), KoinComponent {
    private val mSyncLiveData = MutableLiveData<Boolean>()
    val syncLiveData: LiveData<Boolean> = mSyncLiveData

    private val mErrorLiveData = MutableLiveData<String>()
    val errorLiveData: LiveData<String> = mErrorLiveData

    /**
     * Perform task on App Launch
     */
    fun preProcessing() {
        viewModelScope.launch {
            // Add dummy delay
            delay(SPLASH_DELAY)
            mSyncLiveData.postValue(true)
        }
    }

    companion object {
        // 3 sec delay
        private const val SPLASH_DELAY = 3000L
    }
}