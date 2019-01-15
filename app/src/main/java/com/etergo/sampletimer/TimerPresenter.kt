package com.etergo.sampletimer

import android.icu.text.DecimalFormat
import android.icu.text.NumberFormat
import android.os.Handler
import android.os.SystemClock

import java.util.*
import android.os.Looper
import android.R.attr.x
import android.content.Context
import android.content.Intent
import android.provider.BlockedNumberContract.isBlocked

import java.util.concurrent.TimeUnit
import kotlin.concurrent.timer


class TimerPresenter : TimerContract.Presenter<TimerContract.View> {



    var view: TimerContract.View? = null
    var pauseStartingTime: Long? = null
    var startingTime: Long? = null
    var delta: Long? = null
    private var timerObj: Timer
    var mainHandler: Handler = Handler(Looper.getMainLooper())
    /// the timer has to run in milliseconds
    private val TIMER_VALUE = 2 * 60 * 1000L
    // increase timer
    private val INCREASE_TIMER_VALUE = 10 * 1000

    private val UPDATE_INTERVAL = 99L
    private val DELAY = 0L

    init {
        timerObj = Timer()
    }


    override fun takeView(view: TimerContract.View) {
        this.view = view

    }

    override fun dropView() {
        view = null
    }


    override fun onPaused() {
        pauseStartingTime = SystemClock.elapsedRealtime()
        cancelTimer()
    }

    fun cancelTimer() {
        timerObj.purge()
        timerObj.cancel()
    }

    override fun onResumed() {
        var resumeTiming = SystemClock.elapsedRealtime()
        if (pauseStartingTime != null) {
            val interval = resumeTiming - pauseStartingTime!!
            timerObj = Timer()
            startingTime = resumeTiming + delta!! - interval
        }
        else {
            startingTime = resumeTiming + TIMER_VALUE
        }
        val timerTaskObj = object : TimerTask() {
            override fun run() {
                delta = startingTime!! - SystemClock.elapsedRealtime()
                if (delta!! in 1..(startingTime!! - 1)) {
                    mainHandler.post {
                        view?.updateTimerText(formatMilliseconds(delta!!))
                    }
                }
                else {
                    mainHandler.post {
                        view?.updateTimerText("Done")
                        view?.hideImageButton()
                        delta=0

                    }

                    cancelTimer()
                }
            }
        }
        timerObj.scheduleAtFixedRate(timerTaskObj, DELAY, UPDATE_INTERVAL)





    }

    fun formatMilliseconds(milliSeconds: Long): String {
        val minutes = milliSeconds / 1000 / 60
        val seconds = milliSeconds / 1000 % 60
        val remainingMilliSeconds = (milliSeconds - (minutes * 60 * 1000 + seconds * 1000)) / 100
        val minutesFormatter = if (minutes < 10) "0" + minutes.toString() else minutes.toString()
        val secondsFormatter:String =if (seconds < 10) "0" + seconds.toString() else seconds.toString()
        return if (minutes != 0L)
            "$minutesFormatter:$secondsFormatter:$remainingMilliSeconds"
        else {
            "$secondsFormatter:$remainingMilliSeconds"
        }

    }

    override fun onAddTimeButtonClicked() {
        startingTime = if (delta!! <= (TIMER_VALUE - INCREASE_TIMER_VALUE)) {
            startingTime!! + INCREASE_TIMER_VALUE
        }
        else {
            SystemClock.elapsedRealtime() + TIMER_VALUE
        }
    }
    override fun onBackPressed(): Boolean {
        if (delta!=0L)
        {
            view?.sendApptoBackGround()
            return true
        }
        return  false
    }







}