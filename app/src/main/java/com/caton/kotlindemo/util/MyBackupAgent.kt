package com.caton.kotlindemo.util

import android.app.backup.BackupAgent
import android.app.backup.BackupDataInput
import android.app.backup.BackupDataOutput
import android.os.ParcelFileDescriptor
import android.util.Log

class MyBackupAgent : BackupAgent() {

    companion object{
        val TAG=MyBackupAgent::class.java.simpleName
    }

    override fun onCreate() {
        super.onCreate()
        Log.e(TAG,"onCreate")
    }

    override fun onRestore(
        data: BackupDataInput?,
        appVersionCode: Int,
        newState: ParcelFileDescriptor?
    ) {
        Log.e(TAG,"onRestore")
    }

    override fun onBackup(
        oldState: ParcelFileDescriptor?,
        data: BackupDataOutput?,
        newState: ParcelFileDescriptor?
    ) {
        Log.e(TAG,"onBackup")
    }

}