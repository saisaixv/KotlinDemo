package com.caton.kotlindemo.ui.activity

import android.app.backup.BackupManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.caton.kotlindemo.R
import kotlinx.android.synthetic.main.activity_back_up_agent.*
import kotlinx.android.synthetic.main.activity_nio.*
import kotlinx.android.synthetic.main.activity_nio.toolbar

class BackUpAgentActivity : AppCompatActivity() {

    lateinit var backupManager:BackupManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_back_up_agent)

        toolbar.setNavigationIcon(R.mipmap.ic_back_black)
        toolbar.setNavigationOnClickListener {
            onBackPressed()
        }
        toolbar.setTitle("BackupAgent的使用")

        btn_backup.setOnClickListener {
            backupManager.dataChanged()//此处运行备份
        }

        backupManager = BackupManager(this)
        //检查备份功能是否启用  adb shell bmgr enabled
        //打开备份功能 adb shell bmgr enable true


    }
}
