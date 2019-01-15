package com.etergo.sampletimer

import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.os.SystemClock
import android.util.Log
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(),TimerContract.View {

    override fun onBack() {
        super.onBackPressed()
    }

    override fun sendApptoBackGround() {

        sendToBackground(this@MainActivity)
    }


    lateinit var presenter: TimerPresenter
    val ARG_PAUSE_TIMER= "pauseTimer"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
         presenter= TimerPresenter()
         presenter.takeView(this)

        timerImage.setOnClickListener{
            presenter.onAddTimeButtonClicked()
        }

    }

    override fun onPause() {
        super.onPause()
        presenter.onPaused()

    }

    override fun onResume() {
        super.onResume()
        presenter.onResumed()
    }

    override fun updateTimerText(text: String) {
        timerText.text=text
    }

    override fun hideImageButton() {
        timerImage.visibility= View.GONE
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)

        if (presenter.pauseStartingTime == null) {
            presenter.pauseStartingTime = savedInstanceState.getLong(ARG_PAUSE_TIMER)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        presenter.pauseStartingTime?.let {
            outState.putLong(ARG_PAUSE_TIMER, presenter.pauseStartingTime!!)
        }

    }

    override fun onBackPressed() {
        if (presenter.onBackPressed()) {
            return
        }
        super.onBackPressed()


    }

    companion object {
        fun sendToBackground(context: Context) {
            val intent = Intent()
            intent.setAction(Intent.ACTION_MAIN)
            intent.addCategory(Intent.CATEGORY_HOME)
            context.startActivity(intent)
        }
    }



}
