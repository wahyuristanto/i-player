package com.i.player.main

import android.app.Application
import android.location.Location
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.i.player.config.ApiClient
import com.i.player.models.SongModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class MainScreenViewModel(application: Application) : AndroidViewModel(application) {
    val call = ApiClient.buildService(application)

    private val _listFeed = MutableLiveData<ArrayList<SongModel>>()
    val listFeed: LiveData<ArrayList<SongModel>>
        get() = _listFeed

    private var mvJob = Job()
    private val coroutinesScope = CoroutineScope(mvJob + Dispatchers.Main)

    fun getFeed(param: String?) {
        coroutinesScope.launch {
            try {
                val get = call.getSongs(
                    param
                )
                Log.e("_listSongs", get.toString())
                _listFeed.postValue(get.results!!)
            } catch (t: Throwable) {
                Log.e("_listSongs", t.message.toString())
            }
        }
    }
}