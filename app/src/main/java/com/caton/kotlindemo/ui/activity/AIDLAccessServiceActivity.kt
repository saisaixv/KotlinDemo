package com.caton.kotlindemo.ui.activity

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import android.view.View
import android.widget.Toast
import com.caton.aidlserver.IMyAidlInterface
import com.caton.aidlserver.People
import com.caton.kotlindemo.R
import com.caton.kotlindemo.dao.Student
import com.caton.kotlindemo.dao.StudentAidlInterface
import com.caton.kotlindemo.service.ChildProcessService
import com.caton.kotlindemo.util.Utils
import kotlinx.android.synthetic.main.activity_aidlaccess_service.*

class AIDLAccessServiceActivity : AppCompatActivity(), ServiceConnection {
    companion object{
        var TAG=AIDLAccessServiceActivity.javaClass.name
    }
    lateinit var iMyAidlInterface: IMyAidlInterface
    lateinit var studentAidlInterface: StudentAidlInterface
    override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
        iMyAidlInterface = IMyAidlInterface.Stub.asInterface(service)
    }

    override fun onServiceDisconnected(name: ComponentName?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_aidlaccess_service)

        initView()
    }

    private fun initView() {

        setSupportActionBar(toolbar)
        toolbar.setNavigationIcon(R.mipmap.ic_back_black)
        toolbar.setNavigationOnClickListener(View.OnClickListener {
            onBackPressed()
        })
        toolbar.setTitle("AIDL 的使用")
        btn_start.setOnClickListener(View.OnClickListener {
            startMyService()
            Toast.makeText(
                this@AIDLAccessServiceActivity,
                "启动服务", Toast.LENGTH_SHORT
            ).show()
        })
        btn_bind.setOnClickListener(View.OnClickListener {

            var intent = Intent()
            intent.setAction("com.caton.aidlserver")
            intent.setPackage("com.caton.aidlserver")
//            startService(intent)
            bindService(intent, this@AIDLAccessServiceActivity, Context.BIND_AUTO_CREATE)
            Toast.makeText(
                this@AIDLAccessServiceActivity,
                "绑定服务", Toast.LENGTH_SHORT
            ).show()
        })
        btn_plus.setOnClickListener(View.OnClickListener {
            Toast.makeText(
                this@AIDLAccessServiceActivity,
                iMyAidlInterface.addOperation(3, 5).toString(),
                Toast.LENGTH_SHORT
            ).show()
        })
        btn_getinfo1.setOnClickListener(View.OnClickListener {
            var people = People("张三")
            Toast.makeText(
                this@AIDLAccessServiceActivity,
                iMyAidlInterface.getPeopleInfo1(people),
                Toast.LENGTH_SHORT
            ).show()
        })
        btn_getinfo2.setOnClickListener(View.OnClickListener {
            var people = People("张三")
            Toast.makeText(
                this@AIDLAccessServiceActivity,
                iMyAidlInterface.getPeopleInfo2(people),
                Toast.LENGTH_SHORT
            ).show()
        })
        btn_getinfo3.setOnClickListener(View.OnClickListener {
            var people = People("张三")
            Toast.makeText(
                this@AIDLAccessServiceActivity,
                iMyAidlInterface.getPeopleInfo3(people),
                Toast.LENGTH_SHORT
            ).show()
        })

        btn_start_child.setOnClickListener(View.OnClickListener {
            startService(Intent(this@AIDLAccessServiceActivity, ChildProcessService().javaClass))
        })
        btn_bind_child.setOnClickListener(View.OnClickListener {
            bindService(
                Intent(this@AIDLAccessServiceActivity, ChildProcessService().javaClass),
                object : ServiceConnection {
                    override fun onServiceDisconnected(name: ComponentName?) {
                        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                    }

                    override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
                        studentAidlInterface=StudentAidlInterface.Stub.asInterface(service)
                    }

                },
                Context.BIND_AUTO_CREATE
            )
        })

        btn_get.setOnClickListener(View.OnClickListener {
            var stu=Student("李四")
            Log.e(TAG,studentAidlInterface.getName(stu))
        })
        btn_study.setOnClickListener(View.OnClickListener {
            var stu=Student("李四")
            studentAidlInterface.study(stu)
        })
    }

    var serverPkgName = "com.caton.aidlserver"
    fun startMyService() {

        if (!Utils.checkPackageNameIsExist(this@AIDLAccessServiceActivity, serverPkgName)) {
            Toast.makeText(
                this@AIDLAccessServiceActivity,
                "server不存在，请先安装aidl server",
                Toast.LENGTH_SHORT
            ).show()
            return
        }

        var intent = Intent(Intent.ACTION_MAIN)
        val componentName = ComponentName(serverPkgName, serverPkgName + ".MainActivity")
        intent.setComponent(componentName)
        startActivity(intent)
    }
}
