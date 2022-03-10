package com.serwylo.babydots

import android.app.Application
import android.media.MediaPlayer
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.*

class MainViewModel(application: Application) : AndroidViewModel(application) {

    lateinit var mediaPlayer: MediaPlayer

    var isMusicOn = false
        set(value) {
            if (value) {
                mediaPlayer.start()
            } else {
                mediaPlayer.pause()
            }

            field = value
        }

    private var currentSong: String? = null

    /**
     * If the song we are currently playing is different to that specified by the preferences,
     * then stop any existing music and re-create the [MediaPlayer].
     *
     * Does *not* play the newly created music player, that is up to you.
     */
    fun reloadMusicPlayer() {
        val songName = Preferences.getSongName(getApplication())
        if (currentSong == songName) {
            return
        }

        currentSong = songName

        if (isMusicOn) {
            mediaPlayer.stop()
        }

        val songRes = when(songName) {
            "canon_in_d_major" -> R.raw.canon_in_d_major
            "gymnopedie_1" -> R.raw.gymnopedie_1
            else -> R.raw.vivaldi
        }

        mediaPlayer = MediaPlayer.create(getApplication(), songRes)
        mediaPlayer.isLooping = true
    }

    fun stopMusic() {
        if (isMusicOn) {
            mediaPlayer.pause()
        }
    }

    fun resumeMusic() {
        if (isMusicOn) {
            mediaPlayer.start()
        }
    }

    private var timerJob: Job? = null

    private val _timerCounter = MutableLiveData(-1L)
    val timerCounter: LiveData<Long> = _timerCounter

    private val _isSleepTime = MutableLiveData(false)
    val isSleepTime: LiveData<Boolean> = _isSleepTime

    fun cancelSleepTime() {
        _isSleepTime.value = false
    }

    fun startTimer() {
        _timerCounter.value = sleepTimerDuration()
        launchTimerJob()
    }

    private fun launchTimerJob() {
        timerJob = viewModelScope.launch {
            while ((_timerCounter.value ?: 0) > 0) {
                delay(1000)
                _timerCounter.value = (_timerCounter.value ?: 0) - 1000
            }

            _isSleepTime.value = true
        }
    }

    fun resumeTimer() {
        if (_timerCounter.value ?: 0L > 0) {
            launchTimerJob()
        }
    }

    fun pauseTimer() {
        timerJob?.cancel()
        timerJob = null
    }

    fun cancelTimer() {
        timerJob?.cancel()
        timerJob = null
        _timerCounter.value = -1L
    }

    private fun sleepTimerDuration(): Long {
        return (Preferences.getSleepTimerMins(getApplication()) * 60 * 1000).toLong()
    }

}