package com.whatsappstatusdownloader.jayanthl.statusdownloaderwhatsapp

import android.support.v4.app.Fragment
import android.os.Bundle
import android.os.Environment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.StaggeredGridLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import java.io.File

class Tab4Fragment : Fragment() {



    private val TAG: String = "Tab4Fragment"

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        var view: View = inflater.inflate(R.layout.tab4_fragment, container, false)
        var recyclerView: RecyclerView = view.findViewById(R.id.recyclerview4)

        var videoStatusPath: String = Environment.getExternalStorageDirectory().toString() + "/StatusWhatsApp/Videos/"
        var videoImageList: MutableList<String> = mutableListOf()

        File(videoStatusPath).walk().forEach {
            videoImageList.add("$it")
        }
        videoImageList.removeAt(0)
        var adapter = RecyclerViewDownloadImageAdapter(activity!!.applicationContext, videoImageList)
        adapter.notifyDataSetChanged()

        var staggeredGridLayoutManager = StaggeredGridLayoutManager(3,LinearLayoutManager.VERTICAL)
        recyclerView.setHasFixedSize(true)
        recyclerView.setItemViewCacheSize(20)
        recyclerView.isDrawingCacheEnabled = true
        recyclerView.drawingCacheQuality = View.DRAWING_CACHE_QUALITY_HIGH
        recyclerView.layoutManager = staggeredGridLayoutManager
        recyclerView.adapter = adapter


        return view
    }



}