package com.caton.kotlindemo.ui.activity

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.caton.kotlindemo.R
import com.caton.kotlindemo.adapter.ViewPagerAdapter
import com.caton.kotlindemo.ui.fragment.OneFragment
import com.caton.kotlindemo.ui.fragment.ThreeFragment
import com.caton.kotlindemo.ui.fragment.TwoFragment
import com.caton.kotlindemo.ui.fragment.dummy.DummyContent
import com.google.android.material.navigation.NavigationView
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener,
    OneFragment.OnListFragmentInteractionListener, TwoFragment.OnFragmentInteractionListener,
    ThreeFragment.OnListFragmentInteractionListener {
    override fun onFragmentInteraction(uri: Uri) {
        Log.e("tag", uri.fragment)
    }

    override fun onListFragmentInteraction(item: DummyContent.DummyItem?) {
        Log.e("tag", item!!.content)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {

        Toast.makeText(this, item.title, Toast.LENGTH_SHORT).show();

        return true
    }

    lateinit var mDrawer: DrawerLayout
    lateinit var mToolbar: Toolbar
    lateinit var mNavigationView: NavigationView
    lateinit var tabLayout: TabLayout
    lateinit var viewPager: ViewPager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initView()
        initData()
        initAdapter()
    }

    fun initView() {
        mDrawer = findViewById<DrawerLayout>(R.id.drawer)
        mToolbar = findViewById<Toolbar>(R.id.toolbar)
        mNavigationView = findViewById<NavigationView>(R.id.navigation_menu)
        mNavigationView.setNavigationItemSelectedListener(this)

        tabLayout = findViewById(R.id.tab_layout)
        viewPager = findViewById(R.id.vp)
    }

    fun initData(): Unit {
        toolbar.setTitle("Tablayout+ViewPager+Fragment")
        toolbar.setNavigationIcon(R.mipmap.ic_back_black)
        toolbar.setNavigationOnClickListener(View.OnClickListener {
            onBackPressed()
        })
        if (tabLayout == null) {
            Toast.makeText(this, "tablayout is null", Toast.LENGTH_SHORT).show()
            return
        }

        tabLayout.tabMode = TabLayout.MODE_FIXED
        tabLayout.setupWithViewPager(viewPager)

        listOf<Fragment>(OneFragment(), TwoFragment(), ThreeFragment())
    }

    fun initAdapter() {

        var vpAdapter = ViewPagerAdapter(supportFragmentManager)
        vpAdapter.mFragmentList = listOf<Fragment>(OneFragment(), TwoFragment(), ThreeFragment())
        viewPager.adapter = vpAdapter
        viewPager.setCurrentItem(0)
        viewPager.offscreenPageLimit = 3
    }
}
