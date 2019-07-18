package com.whatsappstatusdownloader.jayanthl.statusdownloaderwhatsapp

import android.content.ContentValues
import android.content.Intent
import android.support.v4.app.Fragment
import android.os.Bundle
import android.os.Environment
import android.support.v4.app.FragmentTransaction
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.StaggeredGridLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import kotlinx.android.synthetic.main.image_layout.*
import java.io.File

class Tab3Fragment : Fragment() {



    private val TAG: String = "Tab3Fragment"

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        Log.i(TAG,"loading view3")
        var view:View = inflater.inflate(R.layout.tab3_fragment,container,false)




        var imageStatusPath: String = Environment.getExternalStorageDirectory().toString() + "/StatusWhatsApp/Images/"

        var dataNames: MutableList <String> = mutableListOf()

        var ExtractedList: MutableList <String> = mutableListOf()


        //Append the Image saved image path name
        File(imageStatusPath).walk().forEach {
            var name: String = it.toString()

            var shortedName: String = ""


            dataNames.add("$it")

            name = name.reversed()
            for (i in 0..(name.length -1)) {
                if(name[i] == '/') {
                    break
                } else {
                    shortedName = shortedName + name[i]

                }
            }
            Log.i(ContentValues.TAG,"F2Shorted Name : $shortedName")
            ExtractedList.add(shortedName.reversed())

        }

        dataNames.removeAt(0)
        ExtractedList.removeAt(0)


        //segregating mp4 and jpeg files

        var newDataList: MutableList<String> = mutableListOf()
        var newExtractedList: MutableList<String> = mutableListOf()

        for ( i in dataNames) {
            if(!i.endsWith(".mp4")) {
                newDataList.add(i)
            }
        }

        for ( i in ExtractedList) {
            if(!i.endsWith(".mp4")) {
                newExtractedList.add(i)
            }
        }




        for(i in dataNames) {
            if(i.endsWith(".mp4")) {
                newDataList.add(i)
            }
        }

        for(i in ExtractedList) {
            if(i.endsWith(".mp4")) {
                newExtractedList.add(i)
            }
        }

        Log.i(TAG,"segregated list : ${newDataList}")
        Log.i(TAG,"segregated extracted : ${newExtractedList}")


        Log.i(TAG,"All saved Images :${ExtractedList}")
        Log.i(TAG,"All thesaved full path ${dataNames}")



        Log.i(ContentValues.TAG," F2Final Extracted Names : $ExtractedList")
        Log.i("TAG","Fragment 2 vid list : $dataNames")


        var adapter :RecyclerViewDownloadadapter = RecyclerViewDownloadadapter(activity!!.applicationContext,newDataList,newExtractedList)
        adapter.notifyDataSetChanged()

        var recyclerView: RecyclerView = view.findViewById(R.id.recyclerview3)

        //recyclerView.layoutManager = LinearLayoutManager(activity!!.applicationContext)
        /*-------Either use above Lineare Layout Manager or below one one Jayanth L----------*/

        var staggeredGridLayoutmanager: StaggeredGridLayoutManager = StaggeredGridLayoutManager(3, LinearLayoutManager.VERTICAL)
        recyclerView.setHasFixedSize(true)
        recyclerView.setItemViewCacheSize(20)
        recyclerView.isDrawingCacheEnabled = true
        recyclerView.drawingCacheQuality = View.DRAWING_CACHE_QUALITY_HIGH
        recyclerView.layoutManager = staggeredGridLayoutmanager
        recyclerView.adapter = adapter




        return view
    }
}