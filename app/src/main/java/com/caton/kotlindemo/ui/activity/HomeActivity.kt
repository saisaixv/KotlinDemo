package com.caton.kotlindemo.ui.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.caton.kotlindemo.R
import com.caton.kotlindemo.adapter.RVAdapter
import com.caton.kotlindemo.dao.Item
import com.caton.kotlindemo.util.Utils
import kotlinx.android.synthetic.main.activity_home.*

class HomeActivity : AppCompatActivity(), RVAdapter.OnItemClickListener {
    override fun onItemClick(view: View, position: Int) {
        var cls = Utils.getClassByString(demoList.get(position).className)
        startActivity(Intent(this@HomeActivity, cls))
    }

    lateinit var demoList: MutableList<Item>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        initView()
        initData()

        initAdapter()
    }

    private fun initView() {
        setSupportActionBar(toolbar)
        rv.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
    }

    private fun initData() {
        demoList = ArrayList()

        demoList.add(Item("TabLayout+ViewPager+Fragment", MainActivity().javaClass.name))
        demoList.add(Item("AIDL 的使用", AIDLAccessServiceActivity().javaClass.name))
        demoList.add(Item("android job", AndroidJobActivity().javaClass.name))
        demoList.add(Item("WorkManager", WorkManagerActivity().javaClass.name))
        demoList.add(Item("LocalSocket", LocalSocketActivity().javaClass.name))

    }

    private fun initAdapter() {
        var adapter = RVAdapter(demoList)
        adapter.onItemClickListener = this
        rv.adapter = adapter
    }
}
