package com.whatsappstatusdownloader.jayanthl.statusdownloaderwhatsapp

import android.support.v4.app.Fragment
import android.content.ContentValues.TAG
import android.os.Bundle
import android.os.Environment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.StaggeredGridLayoutManager
import android.util.Log
import android.view.*
import android.widget.Toast
import java.io.File



class Tab1Fragment() : Fragment() {



    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        var view: View = inflater.inflate(R.layout.tab1_fragment, container, false)


        /*-----------A Program Section to gather WhatsApp Status's Images and Videos-------*/
        if(!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            Toast.makeText(context,"Mediaa Not Mounted", Toast.LENGTH_SHORT).show()
        }

        var statusPath: String = Environment.getExternalStorageDirectory().toString() + "/WhatsApp/Media/.Statuses"
        var dataNames: MutableList <String> = mutableListOf()

        var ExtractedList: MutableList <String> = mutableListOf()
        File(statusPath).walk().forEach {
            var name: String = it.toString()
            var ext:String = "${name[name.length - 1]}" + "${name[name.length - 2]}" + "${name[name.length - 3]}"
            Log.i(TAG,"Length of ext : " + ext)

            var shortedName: String = ""

            if(ext.equals("gpj")) {
                dataNames.add("$it")

                name = name.reversed()
                for (i in 0..(name.length -1)) {
                    if(name[i] == '/') {
                        break
                    } else {
                        shortedName = shortedName + name[i]

                    }
                }
                Log.i(TAG,"Shorted Name : $shortedName")
                ExtractedList.add(shortedName.reversed())
                //shortedName = ""
            }




        }


        Log.i(TAG," Final Extracted Names : $ExtractedList")

        //Extract names



        Log.i(TAG,"List : $dataNames")
        Log.i(TAG,"List num : ${dataNames.size}")
        var interstitialAdNumber = 0

        //Normal Recycler adapter
        var adapter = RecyclerViewAdapter(activity!!.applicationContext,dataNames,ExtractedList, interstitialAdNumber)
        adapter.notifyDataSetChanged()

        var recyclerView: RecyclerView = view.findViewById(R.id.recyclerview)

        //recyclerView.layoutManager = LinearLayoutManager(activity!!.applicationContext)
        /*-------Either use above Linear Layout Manager or below one one Jayanth L----------*/
        var intenstitialAdVieCount: Int = 0

        var staggeredGridLayoutmanager: StaggeredGridLayoutManager  = StaggeredGridLayoutManager(3,LinearLayoutManager.VERTICAL)
        recyclerView.setHasFixedSize(true)
        recyclerView.setItemViewCacheSize(20)
        recyclerView.isDrawingCacheEnabled = true
        recyclerView.drawingCacheQuality = View.DRAWING_CACHE_QUALITY_HIGH
        recyclerView.layoutManager = staggeredGridLayoutmanager
        recyclerView.adapter = adapter
        registerForContextMenu(recyclerView)
        
        return view
    }

}