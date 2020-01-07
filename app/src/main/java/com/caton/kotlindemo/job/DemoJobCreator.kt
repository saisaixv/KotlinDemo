package com.caton.kotlindemo.job

import com.evernote.android.job.Job
import com.evernote.android.job.JobCreator

class DemoJobCreator : JobCreator {
    override fun create(p0: String): Job? {
        when (p0) {
            DemoSyncJob.TAG -> return DemoSyncJob()
            else -> return null
        }
    }

}