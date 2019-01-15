package com.etergo.sampletimer

import java.util.*

interface TimerContract {

    interface View {

        fun updateTimerText(text: String)

        fun hideImageButton()

        fun sendApptoBackGround()

        fun onBack()

    }

    interface Presenter<View> {
        fun takeView(view: TimerContract.View)

        fun dropView()

        fun onPaused()

        fun onResumed()

        fun onAddTimeButtonClicked()

        fun onBackPressed():Boolean

    }
}