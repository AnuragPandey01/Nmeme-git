package com.example.n_meme.app

import android.app.Application
import com.mixpanel.android.mpmetrics.MixpanelAPI
import dagger.hilt.android.HiltAndroidApp
import org.json.JSONObject

@HiltAndroidApp
class BaseApplication: Application(){
    override fun onCreate() {
        super.onCreate()
        val mixpanelAPI = MixpanelAPI.getInstance(this.applicationContext, "00e974be97380ba7274d26ea7e6fca24",true)
        val defaultUEH = Thread.getDefaultUncaughtExceptionHandler()
        val handler = Thread.UncaughtExceptionHandler { thread, ex ->
            mixpanelAPI.track("Crash", JSONObject().apply {
                put("exception_message", ex.message)
                put("exception_stacktrace", ex.stackTrace.toString())
            })
            mixpanelAPI.flush()
            defaultUEH?.uncaughtException(thread, ex)
        }
        Thread.setDefaultUncaughtExceptionHandler(handler)
    }

}