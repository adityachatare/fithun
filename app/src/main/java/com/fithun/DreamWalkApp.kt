package com.fithun

import android.app.Application

import android.content.Context
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class DreamWalkApp : Application() {
    lateinit var context: Context

    override fun onCreate() {
        super.onCreate()
        context = applicationContext

    }




}
