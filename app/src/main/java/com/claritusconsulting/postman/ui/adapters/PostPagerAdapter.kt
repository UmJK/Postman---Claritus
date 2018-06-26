package com.claritusconsulting.postman.ui.adapters

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter

class PostPagerAdapter(fragmentManager: FragmentManager?) :
        FragmentStatePagerAdapter(fragmentManager) {
    private val fragments:ArrayList<Fragment> = ArrayList()
    private val titles:ArrayList<String> = ArrayList()
    override fun getItem(position: Int): Fragment = fragments[position]

    override fun getCount(): Int = fragments.size

    override fun getPageTitle(position: Int): CharSequence? = titles[position]

    fun addFragment(fragment: Fragment, title: String) {
        fragments.add(fragment)
        titles.add(title)
    }
}