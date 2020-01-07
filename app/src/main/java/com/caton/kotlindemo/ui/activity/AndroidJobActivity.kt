package com.caton.kotlindemo.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.caton.kotlindemo.R
import com.caton.kotlindemo.job.DemoSyncJob
import kotlinx.android.synthetic.main.activity_aidlaccess_service.*
import kotlinx.android.synthetic.main.activity_android_job.*
import kotlinx.android.synthetic.main.activity_android_job.toolbar

class AndroidJobActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_android_job)
        DemoSyncJob.scheduleJob()
        setSupportActionBar(toolbar)
        toolbar.setNavigationIcon(R.mipmap.ic_back_black)
        toolbar.setTitle("android job")
        toolbar.setNavigationOnClickListener(View.OnClickListener {
            onBackPressed()
        })
    }
}
