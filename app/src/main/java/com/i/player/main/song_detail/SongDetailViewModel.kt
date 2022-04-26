package com.i.player.main.song_detail

import android.media.MediaPlayer
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.i.player.helpers.DateTimeUtil
import com.i.player.main.BaseViewModel
import com.i.player.models.SongModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SongDetailViewModel : BaseViewModel() {
    private val mPlaybackStatus = MutableLiveData<Boolean>(false)
    val playbackStatus: LiveData<Boolean> get() = mPlaybackStatus

    private val mPlaybackTime = MutableLiveData<String>("00:00")
    val playbackTime: LiveData<String> get() = mPlaybackTime

    private val mPlaybackDuration = MutableLiveData<String>("00:00")
    val playbackDuration: LiveData<String> get() = mPlaybackDuration

    private val mPlaybackProgress = MutableLiveData<Int>(0)
    val playbackProgress: LiveData<Int> get() = mPlaybackProgress

    private val mCurrentSong = MutableLiveData<SongModel>()
    val currentSong: LiveData<SongModel> get() = mCurrentSong

    private var mMediaPlayer: MediaPlayer? = null
    private var mTimerJob: Job? = null
    private var mTimerStatus: Boolean = false

    private var mPlaybackIndex = 0
    private var mPlaybackList = mutableListOf<SongModel>()
    private var mIsMediaPlayerPrepared = false

    fun playSong(songList: List<SongModel>?, songEntity: SongModel?) {
        if (songList != null && songEntity != null) {
            mPlaybackIndex = songList.indexOf(songEntity)
            mPlaybackList.addAll(songList)
            playOrPause()
        }
    }

    fun getDuration(): Int = mMediaPlayer?.duration ?: 0

    fun getCurrentDuration(): Int = mMediaPlayer?.currentPosition ?: 0

    fun setProgress(progress: Int) {
        if (mIsMediaPlayerPrepared) {
            val seekTo = (progress * getDuration()) / 100
            mMediaPlayer?.seekTo(seekTo)
        }
    }

    fun playNext() {
        if (mPlaybackList.isEmpty()) return

        mPlaybackIndex++
        if (mPlaybackIndex == mPlaybackList.size) {
            mPlaybackIndex = 0
        }

        preparePlayer(mPlaybackIndex)
    }

    fun playPrevious() {
        if (mPlaybackList.isEmpty()) return

        mPlaybackIndex--
        if (mPlaybackIndex < 0) {
            mPlaybackIndex = mPlaybackList.size - 1
        }

        preparePlayer(mPlaybackIndex)
    }

    fun playOrPause() {
        if (mPlaybackList.isEmpty()) return

        val player = mMediaPlayer ?: preparePlayer(mPlaybackIndex)

        if (player.isPlaying) {
            playOrPause(false)
        } else if (mIsMediaPlayerPrepared) {
            playOrPause(true)
        }
    }

    private fun playOrPause(play: Boolean) {
        if (play) {
            mPlaybackStatus.postValue(true)
            mMediaPlayer?.start()
            startTimer()
        } else {
            mPlaybackStatus.postValue(false)
            mMediaPlayer?.pause()
            stopTimer()
        }
    }

    private fun preparePlayer(index: Int): MediaPlayer {
        if (mMediaPlayer != null) {
            releaseMediaPlayer()
        }

        mMediaPlayer = MediaPlayer()
        mMediaPlayer?.setOnPreparedListener {
            mIsMediaPlayerPrepared = true
            playOrPause(true)
        }

        val entity = mPlaybackList[index]
        mCurrentSong.postValue(entity)
        val duration = DateTimeUtil.getFormattedDuration(120)
        mPlaybackDuration.postValue(duration)

        mIsMediaPlayerPrepared = false
        val player = mMediaPlayer!!
        player.setDataSource(entity.previewUrl)
        player.prepareAsync()
        mMediaPlayer = player
        return player
    }

    private fun startTimer() {
        mTimerStatus = true
        mTimerJob = viewModelScope.launch {
            while (mTimerStatus) {
                val currentDuration = getCurrentDuration()
                val duration = DateTimeUtil.getFormattedDuration(currentDuration)
                mPlaybackTime.postValue(duration)
                val progress = (currentDuration * 100) / getDuration()
                mPlaybackProgress.postValue(progress)
                delay(1000)
            }
        }
    }

    private fun stopTimer() {
        mTimerStatus = false
        mTimerJob?.cancel()
    }

    fun releaseMediaPlayer() {
        mMediaPlayer?.stop()
        mMediaPlayer?.reset()
        mMediaPlayer?.release()
        mMediaPlayer = null
    }
}