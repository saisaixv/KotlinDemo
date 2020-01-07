package com.caton.kotlindemo.job

import android.util.Log
import android.util.TimeUtils
import com.evernote.android.job.Job
import com.evernote.android.job.JobRequest
import java.util.concurrent.TimeUnit

class DemoSyncJob : Job() {

    companion object {
        const val TAG = "job_demo_tag"
        fun scheduleJob(): Unit {
            JobRequest.Builder(DemoSyncJob.TAG)
                .setExecutionWindow(3_000L, 4_000L)
//                .setPeriodic(TimeUnit.SECONDS.toMillis(900), TimeUnit.SECONDS.toMillis(300))
//                .setUpdateCurrent(true)
                .build()
                .schedule()
        }

    }

    override fun onRunJob(p0: Params): Result {

        Log.e("tag","onRunJob======")
        return Result.SUCCESS
    }


}