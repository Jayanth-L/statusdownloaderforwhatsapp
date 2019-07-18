package com.whatsappstatusdownloader.jayanthl.statusdownloaderwhatsapp

import android.content.ContentValues
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
import java.io.File

class Tab2Fragment : Fragment() {
    private val TAG = "Tab2fragment"


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {


        Log.i(TAG,"loading view2")
        var view:View = inflater.inflate(R.layout.tab2_fragment,container,false)
        var statusPath: String = Environment.getExternalStorageDirectory().toString() + "/WhatsApp/Media/.Statuses"
        var dataNames: MutableList <String> = mutableListOf()

        var ExtractedList: MutableList <String> = mutableListOf()

        File(statusPath).walk().forEach {
            var name: String = it.toString()
            var ext:String = "${name[name.length - 1]}" + "${name[name.length - 2]}" + "${name[name.length - 3]}"
            Log.i(TAG,"Length of ext : " + ext)

            var shortedName: String = ""

            if(ext.equals("4pm")) {
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
        }


        Log.i(ContentValues.TAG," F2Final Extracted Names : $ExtractedList")
        Log.i("TAG","Fragment 2 vid list : $dataNames")


        var adapter = RecyclerViewVideoAdapter(activity!!.applicationContext, dataNames, ExtractedList)
        adapter.notifyDataSetChanged()
        var recyclerView: RecyclerView = view.findViewById(R.id.recyclerview2)

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