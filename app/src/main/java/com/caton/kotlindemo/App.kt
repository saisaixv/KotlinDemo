package com.caton.kotlindemo

import android.app.Application
import com.caton.kotlindemo.job.DemoJobCreator
import com.evernote.android.job.JobManager

class App : Application() {
    override fun onCreate() {
        super.onCreate()

        JobManager.create(this).addJobCreator(DemoJobCreator())
    }
}