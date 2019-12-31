package com.caton.kotlindemo.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.caton.kotlindemo.ui.fragment.OneFragment
import com.caton.kotlindemo.ui.fragment.ThreeFragment
import com.caton.kotlindemo.ui.fragment.TwoFragment

class ViewPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm){

    lateinit var mFragmentList: List<Fragment>

    override fun getItem(position: Int): Fragment {
        return mFragmentList.get(position)
    }

    override fun getCount(): Int {
        return mFragmentList.size
    }

    override fun getPageTitle(position: Int): CharSequence? {
        when (mFragmentList.get(position)){
            is OneFragment->return "One"
            is TwoFragment-> return "Two"
            is ThreeFragment->return "Three"
        }

        return super.getPageTitle(position)
    }
}