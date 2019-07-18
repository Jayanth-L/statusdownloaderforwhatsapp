package com.whatsappstatusdownloader.jayanthl.statusdownloaderwhatsapp

import android.content.Context
import android.net.Uri
import android.os.Environment
import android.support.v4.view.PagerAdapter
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.RelativeLayout
import android.widget.Toast
import java.io.File

class ImageViewAdapter (mContext: Context, imageNames: MutableList<String>): PagerAdapter (){

    var mContext: Context
    var imageNames: MutableList<String>

    init {
        this.mContext = mContext
        this.imageNames = imageNames
    }

    override fun getItemPosition(`object`: Any): Int {
        return super.getItemPosition(`object`)
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        var layoutInflater: LayoutInflater = mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        var view: View = layoutInflater.inflate(R.layout.image_view_for_container,container,false)
        var imageView: ImageView = view.findViewById(R.id.containerimagevieww)
        imageView.setImageURI(Uri.fromFile(File(imageNames[position])))
        container.addView(view)

        return view
    }

    override fun getCount(): Int {
        return imageNames.size
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return (view== `object` as RelativeLayout)
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as RelativeLayout)
    }
}