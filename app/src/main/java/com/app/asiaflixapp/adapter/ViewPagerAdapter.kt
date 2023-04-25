package com.app.asiaflixapp.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter


class ViewPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {
    private val pertamaFragment: MutableList<Fragment> = ArrayList()
    override fun getItem(position: Int): Fragment {
        return pertamaFragment[position]
    }

    override fun getCount(): Int {
        return pertamaFragment.size
    }

    fun AddFragment(fragment: Fragment) {
        pertamaFragment.add(fragment)
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return super.getPageTitle(position)
    }
}