package com.whatsappstatusdownloader.jayanthl.statusdownloaderwhatsapp

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.app.FragmentStatePagerAdapter
import android.support.v4.view.PagerAdapter
import android.view.ViewGroup
import java.io.Serializable
import java.util.*
import kotlin.collections.HashMap

class SectionsPageAdapter(fm: FragmentManager) : FragmentStatePagerAdapter(fm), Serializable {

    var mFragmentmanager: FragmentManager
    var mFragmentTags: HashMap<Int,String>

    init {
        this.mFragmentmanager = fm
        mFragmentTags = HashMap<Int,String>()
    }

    private var fragmentList: MutableList<Fragment> = mutableListOf()
    private var fragmenttitleList: MutableList<String> = mutableListOf()


    fun addFragment(fragment: Fragment, title:String) {
        fragmentList.add(fragment)
        fragmenttitleList.add(title)
    }

    override fun getItem(position: Int): Fragment? {
        return (fragmentList[position])
    }

    override fun getItemPosition(`object`: Any): Int {
        notifyDataSetChanged()
        return  PagerAdapter.POSITION_NONE
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {

        var obj: Object =  (super.instantiateItem(container, position) as Object)
        var f: Fragment = obj as Fragment
        var tag: String? = f.tag
        mFragmentTags.put(position,tag.toString())

        return obj
    }

    override fun getCount(): Int {
        return fragmentList.size
    }


}