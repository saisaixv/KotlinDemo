package com.caton.kotlindemo.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.work.OneTimeWorkRequest
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import com.caton.kotlindemo.R
import com.caton.kotlindemo.workmanager.UploadWorker
import kotlinx.android.synthetic.main.activity_work_manager.*
import java.util.concurrent.TimeUnit

class WorkManagerActivity : AppCompatActivity() {
    lateinit var uploadWorker: OneTimeWorkRequest
    lateinit var periodicWorkRequest: PeriodicWorkRequest

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_work_manager)
        setSupportActionBar(toolbar)
        toolbar.setNavigationIcon(R.mipmap.ic_back_black)
        toolbar.setTitle("WorkManager")
        toolbar.setNavigationOnClickListener(View.OnClickListener {
            onBackPressed()
        })

        btn_one.setOnClickListener(View.OnClickListener {
            if (uploadWorker == null) {
                uploadWorker = OneTimeWorkRequest
                    .Builder(UploadWorker::class.java)
                    .build()
            }
            WorkManager.getInstance(this@WorkManagerActivity).enqueue(uploadWorker)
        })
        btn_periodoc.setOnClickListener(View.OnClickListener {
            if (periodicWorkRequest == null) {
                periodicWorkRequest = PeriodicWorkRequest
                    .Builder(UploadWorker::class.java, 15, TimeUnit.MINUTES)//最短可定義時間爲15分鐘
                    .build()
            }
            WorkManager.getInstance(this@WorkManagerActivity).enqueue(periodicWorkRequest)
        })
    }
}
