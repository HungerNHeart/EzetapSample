package com.task.ezetapapp

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MainApp: Application() {

    override fun onCreate() {
        super.onCreate()
        mContext = this
    }

    companion object{
        private lateinit var mContext: MainApp

        fun getApp() = mContext
    }
}