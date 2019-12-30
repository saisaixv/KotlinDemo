package com.caton.kotlindemo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_home.*

class HomeActivity : AppCompatActivity() {
    lateinit var demoList: MutableList<String>
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
        val demoArray = resources.getStringArray(R.array.demo_list)
        demoList = ArrayList()
        for (str in demoArray) {
            demoList.add(str)
        }

    }

    private fun initAdapter() {
        var adapter = RVAdapter(demoList)
        rv.adapter = adapter
    }
}
