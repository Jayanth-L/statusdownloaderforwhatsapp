package com.whatsappstatusdownloader.jayanthl.statusdownloaderwhatsapp

import android.content.*
import android.net.Uri
import android.os.*
import android.support.v7.app.AppCompatActivity
import android.support.design.widget.TabLayout
import android.support.v4.app.FragmentTransaction
import android.support.v4.view.ViewPager
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AlertDialog.Builder
import android.util.Log
import android.view.*
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import java.io.File

class MainActivity : AppCompatActivity() {


    private val TAG = "MainActivity"
    lateinit var mViewPager: ViewPager
    lateinit var sectionsPageAdapter: SectionsPageAdapter
    var array: MutableList<String> = mutableListOf()



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)



        //Creating necessary Directories if thos Doesn't Exists

        var destination: String = "${Environment.getExternalStorageDirectory()}" + "/StatusWhatsApp"
        var videoDestination: String = "${Environment.getExternalStorageDirectory()}" + "/StatusWhatsApp/Videos"
        var imageDestination: String = "${Environment.getExternalStorageDirectory()}" + "/StatusWhatsApp/Images"
        if(!File(destination).exists()) {
            File(destination).mkdir()
            File(videoDestination).mkdir()
            File(imageDestination).mkdir()
        } else {
            if(!(File(imageDestination)).exists()) {
                File(imageDestination).mkdir()
            }

            if(!(File(videoDestination)).exists()) {
                File(videoDestination).mkdir()
            }
        }



        //Initialize ads
        /*
        MobileAds.initialize(this,"ca-app-pub-3940256099942544~3347511713") //Demo Ads
        //MobileAds.initialize(this, "ca-app-pub-6626003824827442~2851303553") //real ads
        var adView: AdView = findViewById(R.id.adView)
        val adRequest: AdRequest = AdRequest.Builder().build()
        adView.loadAd(adRequest)
        */

        //check if the destination directory exists if not then create a one


        //Add ToolBar
        var toolbar: android.support.v7.widget.Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        //Navigation Drawer

        val mToggle = ActionBarDrawerToggle(this,drawer_layout,toolbar, R.string.action_open,R.string.action_close)
        drawer_layout.addDrawerListener(mToggle)
        mToggle.syncState()
        supportActionBar!!.setDisplayHomeAsUpEnabled(false)

        navigation_view.setNavigationItemSelectedListener {
            when(it.itemId) {
                R.id.action_ratethisapp -> {
                    var uri = Uri.parse("market://details?id=${this.packageName}")
                    var market: Intent = Intent(Intent.ACTION_VIEW, uri)
                    drawer_layout.closeDrawers()
                    Toast.makeText(this, "Thanks :)", Toast.LENGTH_SHORT).show()

                    try {
                        startActivity(market)
                    } catch (e: ActivityNotFoundException) {
                        Toast.makeText(this, "Probably Playstore not installed!", Toast.LENGTH_SHORT).show()
                    }
                    return@setNavigationItemSelectedListener true
                }

                R.id.action_downloadallstatuses -> {

                    drawer_layout.closeDrawers()
                    Handler().postDelayed({

                        //Saving All Ststus Images
                        var targetLocation : String = Environment.getExternalStorageDirectory().toString() + "/WhatsApp/Media/.Statuses/"
                        var destinationLocation: String = Environment.getExternalStorageDirectory().toString() + "/StatusWhatsApp/Images/"

                        var allFilesPath: MutableList<String> = mutableListOf()
                        var extractedAllFiles: MutableList<String> = mutableListOf()
                        File(targetLocation).walk().forEach {

                            if(it.toString().endsWith(".jpg")) {
                                allFilesPath.add(it.toString())
                                Log.i(TAG,"temp ext : $it")
                            }

                        }

                        for(i in allFilesPath) {

                            var tempExtract: String = ""
                            var k: String  = i.reversed()
                            for(j in 0..k.length -1) {
                                if(k[j] != '/') {
                                    tempExtract = tempExtract + k[j]
                                } else {
                                    break
                                }
                            }
                            extractedAllFiles.add(tempExtract.reversed())
                        }

                        Log.i(TAG,"tempExtract len :${extractedAllFiles.size} : ${extractedAllFiles}")


                        for( i in 0..extractedAllFiles.size - 1) {

                            if(!(File(destinationLocation + "${extractedAllFiles[i]}").exists()))

                                File(allFilesPath[i]).copyTo(File(destinationLocation + "${extractedAllFiles[i]}"), false)
                        }

                        //Saving All Status Videos

                        var sourceLocation: String = Environment.getExternalStorageDirectory().toString() + "/WhatsApp/Media/.Statuses"

                        var videosDataList: MutableList<String> = mutableListOf()
                        File(sourceLocation).walk().forEach {

                            if(it.toString().endsWith(".mp4")) {
                                videosDataList.add(it.toString())
                            }
                        }

                        var ExtracteddataList: MutableList<String> = mutableListOf()
                        for(i in videosDataList) {
                            var tempExtractedVideo: String = ""
                            var k = i.reversed()
                            for(j in 0..(k.length -1)) {
                                if(k[j] != '/') {
                                    tempExtractedVideo = tempExtractedVideo + k[j]
                                } else {
                                    break
                                }
                            }
                            ExtracteddataList.add(tempExtractedVideo.reversed())
                        }

                        Log.i("VideoSection", "Video : $videosDataList")
                        Log.i("VideoSection", "Extraceted video :$ExtracteddataList")

                        //Now saving all the Video data

                        var currentFile  =  Environment.getExternalStorageDirectory().toString() + "/StatusWhatsApp/Videos/"

                        for (i in 0..(videosDataList.size -1)) {
                            if(!File(currentFile + ExtracteddataList[i]).exists()) {
                                File(videosDataList[i]).copyTo(File(currentFile + ExtracteddataList[i]), false)
                            }
                        }
                        var vibrator: Vibrator = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator

                        vibrator.vibrate(100)

                        Toast.makeText(this, "All Statuses Downloaded Successfully", Toast.LENGTH_SHORT).show()

                        //Refreshing available fragments
                        var fragmentTransaction: FragmentTransaction = supportFragmentManager.beginTransaction()

                        //var fragment1 = supportFragmentManager.findFragmentById(R.id.container)
                        Log.i(TAG,"fragment no ${R.id.container}")
                        try {

                            fragmentTransaction.detach(supportFragmentManager.fragments[0])
                            fragmentTransaction.attach(supportFragmentManager.fragments[0])

                        } catch (e: Exception) {}

                        try {
                            fragmentTransaction.detach(supportFragmentManager.fragments[1])
                            fragmentTransaction.attach(supportFragmentManager.fragments[1])
                        } catch (e: Exception) {}

                        try {
                            fragmentTransaction.detach(supportFragmentManager.fragments[2])
                            fragmentTransaction.attach(supportFragmentManager.fragments[2])
                        } catch (e: Exception) {}

                        try {
                            fragmentTransaction.detach(supportFragmentManager.fragments[-1])
                            fragmentTransaction.attach(supportFragmentManager.fragments[-1])
                        } catch (e: Exception) {}
                        fragmentTransaction.commit()
                    }, 500)

                    return@setNavigationItemSelectedListener true
                }

                R.id.action_deleteallstatuses -> {
                    drawer_layout.closeDrawers()
                    Handler().postDelayed( {

                        //Deleting All Images
                        var deletingImageLocation: String = Environment.getExternalStorageDirectory().toString() + "/StatusWhatsApp/Images/"
                        var deleteImageList: MutableList<String> = mutableListOf()

                        File(deletingImageLocation).walk().forEach {
                            deleteImageList.add(it.toString())
                        }
                        deleteImageList.removeAt(0)

                        for(i in 0..(deleteImageList.size -1)) {
                            if(File(deleteImageList[i]).exists()) {
                                File(deleteImageList[i]).delete()
                            }
                        }

                        //deleting All Videos

                        var deletingVideoLocation: String = Environment.getExternalStorageDirectory().toString() + "/StatusWhatsApp/Videos/"

                        var deleteVideoList: MutableList<String> = mutableListOf()
                        File(deletingVideoLocation).walk().forEach {
                            deleteVideoList.add(it.toString())
                        }

                        deleteVideoList.removeAt(0)

                        for(i in 0..deleteVideoList.size -1) {
                            if(File(deleteVideoList[i]).exists()) {
                                File(deleteVideoList[i]).delete()
                            }
                        }

                        var vibrator: Vibrator = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator

                        vibrator.vibrate(100)

                        Toast.makeText(this, "Successfully Deleted All Video Status", Toast.LENGTH_SHORT).show()

                        // Refreshing Fragments
                        var fragmentTransaction: FragmentTransaction = supportFragmentManager.beginTransaction()

                        try {

                            fragmentTransaction.detach(supportFragmentManager.fragments[0])
                            fragmentTransaction.attach(supportFragmentManager.fragments[0])

                        } catch (e: Exception) {}

                        try {
                            fragmentTransaction.detach(supportFragmentManager.fragments[1])
                            fragmentTransaction.attach(supportFragmentManager.fragments[1])
                        } catch (e: Exception) {}

                        try {
                            fragmentTransaction.detach(supportFragmentManager.fragments[2])
                            fragmentTransaction.attach(supportFragmentManager.fragments[2])
                        } catch (e: Exception) {}

                        try {
                            fragmentTransaction.detach(supportFragmentManager.fragments[-1])
                            fragmentTransaction.attach(supportFragmentManager.fragments[-1])
                        } catch (e: Exception) {}
                        fragmentTransaction.commit()
                    }, 500)

                    return@setNavigationItemSelectedListener true
                }

                R.id.action_shareapp -> {
                    try {
                        val intent = Intent(Intent.ACTION_SEND)
                        intent.setType("text/plain")
                        intent.putExtra(Intent.EXTRA_SUBJECT, " Status Downloader For WhatsApp")
                        var expln = "\n Let me recommend you this App, where you can  Download your friends WhatsApp Status :)\n\n"
                        expln = expln + "https://play.google.com/store/apps/details?id=${this.packageName}"
                        intent.putExtra(Intent.EXTRA_TEXT, expln)
                        startActivity(Intent.createChooser(intent, "choose one :)"))
                    } catch (e: Exception) {}
                    drawer_layout.closeDrawers()

                    return@setNavigationItemSelectedListener true
                }
                else -> return@setNavigationItemSelectedListener false
            }
        }


        /*-------------A Section for View Pager-----------*/
        //var mSectionsPageAdapter: SectionsPageAdapter = SectionsPageAdapter(supportFragmentManager)
        mViewPager = findViewById(R.id.container)
        mViewPager.offscreenPageLimit = 0
        setupViewPageAdapter(mViewPager)
        //mViewPager.setOnPageChangeListener(this)


        var tabLayout: TabLayout = findViewById(R.id.tabs)
        tabLayout.setupWithViewPager(mViewPager)
        Log.d(TAG,"settings icons")
        tabLayout.getTabAt(0)!!.setIcon(R.drawable.ic_image_black_24dp)
        tabLayout.getTabAt(1)!!.setIcon(R.drawable.ic_slow_motion_video_black_24dp)
        tabLayout.getTabAt(3)!!.setIcon(R.drawable.ic_video_with_download)
        tabLayout.getTabAt(2)!!.setIcon(R.drawable.ic_image_with_download)
        Log.d(TAG,"Successfull")

    }




    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        var inflater: MenuInflater = MenuInflater(this)
        inflater.inflate(R.menu.menu_main,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item!!.itemId) {

            R.id.action_downloadallimages -> {

                Handler().postDelayed({
                    //program to copy all the statuses from the target location

                    var targetLocation : String = Environment.getExternalStorageDirectory().toString() + "/WhatsApp/Media/.Statuses/"
                    var destinationLocation: String = Environment.getExternalStorageDirectory().toString() + "/StatusWhatsApp/Images/"

                    var allFilesPath: MutableList<String> = mutableListOf()
                    var extractedAllFiles: MutableList<String> = mutableListOf()
                    File(targetLocation).walk().forEach {

                        if(it.toString().endsWith(".jpg")) {
                            allFilesPath.add(it.toString())
                            Log.i(TAG,"temp ext : $it")
                        }

                    }

                    for(i in allFilesPath) {

                        var tempExtract: String = ""
                        var k: String  = i.reversed()
                        for(j in 0..k.length -1) {
                            if(k[j] != '/') {
                                tempExtract = tempExtract + k[j]
                            } else {
                                break
                            }
                        }
                        extractedAllFiles.add(tempExtract.reversed())
                    }

                    Log.i(TAG,"tempExtract len :${extractedAllFiles.size} : ${extractedAllFiles}")


                    for( i in 0..extractedAllFiles.size - 1) {

                        if(!(File(destinationLocation + "${extractedAllFiles[i]}").exists()))

                            File(allFilesPath[i]).copyTo(File(destinationLocation + "${extractedAllFiles[i]}"), false)
                    }






                    //Refreshing available fragments
                    var fragmentTransaction: FragmentTransaction = supportFragmentManager.beginTransaction()

                    //var fragment1 = supportFragmentManager.findFragmentById(R.id.container)
                    Log.i(TAG,"fragment no ${R.id.container}")
                    try {

                        fragmentTransaction.detach(supportFragmentManager.fragments[0])
                        fragmentTransaction.attach(supportFragmentManager.fragments[0])

                    } catch (e: Exception) {}

                    try {
                        fragmentTransaction.detach(supportFragmentManager.fragments[1])
                        fragmentTransaction.attach(supportFragmentManager.fragments[1])
                    } catch (e: Exception) {}

                    try {
                        fragmentTransaction.detach(supportFragmentManager.fragments[2])
                        fragmentTransaction.attach(supportFragmentManager.fragments[2])
                    } catch (e: Exception) {}

                    try {
                        fragmentTransaction.detach(supportFragmentManager.fragments[-1])
                        fragmentTransaction.attach(supportFragmentManager.fragments[-1])
                    } catch (e: Exception) {}
                    fragmentTransaction.commit()

                    var vibrator: Vibrator = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator

                    vibrator.vibrate(100)
                    Toast.makeText(this,"All the Statuses Saved!!", Toast.LENGTH_SHORT).show()
                }, 500)

                return true


            }

            R.id.action_downloadallvideos -> {

                Handler().postDelayed({
                    //Downloading all Videos

                    var sourceLocation: String = Environment.getExternalStorageDirectory().toString() + "/WhatsApp/Media/.Statuses"
                    //var destinationLocation: String = Environment.getExternalStorageDirectory().toString() + "/StatusWhatsApp/Videos/"

                    var videosDataList: MutableList<String> = mutableListOf()
                    File(sourceLocation).walk().forEach {

                        if(it.toString().endsWith(".mp4")) {
                            videosDataList.add(it.toString())
                        }
                    }

                    var ExtracteddataList: MutableList<String> = mutableListOf()
                    for(i in videosDataList) {
                        var tempExtractedVideo: String = ""
                        var k = i.reversed()
                        for(j in 0..(k.length -1)) {
                            if(k[j] != '/') {
                                tempExtractedVideo = tempExtractedVideo + k[j]
                            } else {
                                break
                            }
                        }
                        ExtracteddataList.add(tempExtractedVideo.reversed())
                    }

                    Log.i("VideoSection", "Video : $videosDataList")
                    Log.i("VideoSection", "Extraceted video :$ExtracteddataList")

                    //Now saving all the Video data

                    var currentFile  =  Environment.getExternalStorageDirectory().toString() + "/StatusWhatsApp/Videos/"

                    for (i in 0..(videosDataList.size -1)) {
                        if(!File(currentFile + ExtracteddataList[i]).exists()) {
                            File(videosDataList[i]).copyTo(File(currentFile + ExtracteddataList[i]), false)
                        }
                    }

                    Toast.makeText(this, "All Videos Downloaded Successfully", Toast.LENGTH_SHORT).show()

                    //Refreshing the Fragment

                    var fragmentTransaction: FragmentTransaction = supportFragmentManager.beginTransaction()

                    //var fragment1 = supportFragmentManager.findFragmentById(R.id.container)
                    Log.i(TAG,"fragment no ${R.id.container}")
                    try {

                        fragmentTransaction.detach(supportFragmentManager.fragments[0])
                        fragmentTransaction.attach(supportFragmentManager.fragments[0])

                    } catch (e: Exception) {}

                    try {
                        fragmentTransaction.detach(supportFragmentManager.fragments[1])
                        fragmentTransaction.attach(supportFragmentManager.fragments[1])
                    } catch (e: Exception) {}

                    try {
                        fragmentTransaction.detach(supportFragmentManager.fragments[2])
                        fragmentTransaction.attach(supportFragmentManager.fragments[2])
                    } catch (e: Exception) {}

                    try {
                        fragmentTransaction.detach(supportFragmentManager.fragments[-1])
                        fragmentTransaction.attach(supportFragmentManager.fragments[-1])
                    } catch (e: Exception) {}
                    fragmentTransaction.commit()
                    var vibrator: Vibrator = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator

                    vibrator.vibrate(100)
                }, 500)

                return true
            }

            R.id.action_deleteallimages -> {

                Handler().postDelayed({
                    //Deleting all Images

                    var deletingImageLocation: String = Environment.getExternalStorageDirectory().toString() + "/StatusWhatsApp/Images/"
                    var deleteImageList: MutableList<String> = mutableListOf()

                    File(deletingImageLocation).walk().forEach {
                        deleteImageList.add(it.toString())
                    }

                    deleteImageList.removeAt(0)

                    if(deleteImageList.size == 0) {
                        Toast.makeText(this, "Already Emptied :)", Toast.LENGTH_SHORT).show()
                    } else {

                        for (i in 0..(deleteImageList.size - 1)) {
                            if (File(deleteImageList[i]).exists()) {
                                File(deleteImageList[i]).delete()
                            }
                        }

                        Toast.makeText(this, "Deleted All Status Images!", Toast.LENGTH_SHORT).show()

                        //refresing al fragments

                        var fragmentTransaction: FragmentTransaction = supportFragmentManager.beginTransaction()

                        //var fragment1 = supportFragmentManager.findFragmentById(R.id.container)
                        Log.i(TAG, "fragment no ${R.id.container}")
                        try {

                            fragmentTransaction.detach(supportFragmentManager.fragments[0])
                            fragmentTransaction.attach(supportFragmentManager.fragments[0])

                        } catch (e: Exception) {
                        }

                        try {
                            fragmentTransaction.detach(supportFragmentManager.fragments[1])
                            fragmentTransaction.attach(supportFragmentManager.fragments[1])
                        } catch (e: Exception) {
                        }

                        try {
                            fragmentTransaction.detach(supportFragmentManager.fragments[2])
                            fragmentTransaction.attach(supportFragmentManager.fragments[2])
                        } catch (e: Exception) {
                        }

                        try {
                            fragmentTransaction.detach(supportFragmentManager.fragments[-1])
                            fragmentTransaction.attach(supportFragmentManager.fragments[-1])
                        } catch (e: Exception) {
                        }
                        fragmentTransaction.commit()
                        var vibrator: Vibrator = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator

                        vibrator.vibrate(100)
                    }
                },500)




                return true
            }

            R.id.action_deleteallvideos -> {


                Handler().postDelayed({
                    //Deleting All Status Videos

                    var deletingVideoLocation: String = Environment.getExternalStorageDirectory().toString() + "/StatusWhatsApp/Videos/"

                    var deleteVideoList: MutableList<String> = mutableListOf()
                    File(deletingVideoLocation).walk().forEach {
                        deleteVideoList.add(it.toString())
                    }

                    deleteVideoList.removeAt(0)

                    if(deleteVideoList.size == 0) {
                        Toast.makeText(this,"Already Emptied :)", Toast.LENGTH_SHORT).show()
                    } else {
                        for(i in 0..deleteVideoList.size -1) {
                            if(File(deleteVideoList[i]).exists()) {
                                File(deleteVideoList[i]).delete()
                            }
                        }

                        Toast.makeText(this, "Successfully Deleted All Video Status", Toast.LENGTH_SHORT).show()

                        // Refreshing Fragments
                        var fragmentTransaction: FragmentTransaction = supportFragmentManager.beginTransaction()

                        //var fragment1 = supportFragmentManager.findFragmentById(R.id.container)
                        Log.i(TAG,"fragment no ${R.id.container}")
                        try {

                            fragmentTransaction.detach(supportFragmentManager.fragments[0])
                            fragmentTransaction.attach(supportFragmentManager.fragments[0])

                        } catch (e: Exception) {}

                        try {
                            fragmentTransaction.detach(supportFragmentManager.fragments[1])
                            fragmentTransaction.attach(supportFragmentManager.fragments[1])
                        } catch (e: Exception) {}

                        try {
                            fragmentTransaction.detach(supportFragmentManager.fragments[2])
                            fragmentTransaction.attach(supportFragmentManager.fragments[2])
                        } catch (e: Exception) {}

                        try {
                            fragmentTransaction.detach(supportFragmentManager.fragments[-1])
                            fragmentTransaction.attach(supportFragmentManager.fragments[-1])
                        } catch (e: Exception) {}
                        var vibrator: Vibrator = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator

                        vibrator.vibrate(100)
                        fragmentTransaction.commit()
                    }
                }, 500)


                return true
            }

            R.id.action_howto -> {
                var view: View = LayoutInflater.from(this).inflate(R.layout.builder_howto, null)
                val builder: Builder = Builder(this)
                builder.setView(view).setTitle("Using this App :)").setPositiveButton("Ok", DialogInterface.OnClickListener { dialog, _ ->
                    dialog.dismiss()
                })
                builder.create().show()
            }

            R.id.action_about -> {
                val view: View = LayoutInflater.from(this).inflate(R.layout.builder_about,null)
                val builder = Builder(this)
                builder.setView(view).setTitle("About").setPositiveButton("Ok", DialogInterface.OnClickListener { dialog, _ ->
                    dialog.dismiss()
                })
                builder.create().show()
            }

            R.id.action_mainshare -> {
                try {
                    val intent = Intent(Intent.ACTION_SEND)
                    intent.setType("text/plain")
                    intent.putExtra(Intent.EXTRA_SUBJECT, " Status Downloader For WhatsApp")
                    var expln = "\n Let me recommend you this App, where you can  Download your friends WhatsApp Status :)\n\n"
                    expln += "https://play.google.com/store/apps/details?id=${this.packageName}"
                    intent.putExtra(Intent.EXTRA_TEXT, expln)
                    startActivity(Intent.createChooser(intent, "choose one :)"))
                } catch (e: Exception) {}
                drawer_layout.closeDrawers()
            }

            R.id.action_mainrateus -> {
                var uri = Uri.parse("market://details?id=${this.packageName}")
                var market: Intent = Intent(Intent.ACTION_VIEW, uri)
                drawer_layout.closeDrawers()
                Toast.makeText(this, "Thanks :)", Toast.LENGTH_SHORT).show()

                try {
                    startActivity(market)
                } catch (e: ActivityNotFoundException) {
                    Toast.makeText(this, "Probably Playstore not installed!", Toast.LENGTH_SHORT).show()
                }
            }

            R.id.action_privacypolicy -> {
                try {
                    val uri = "https://sites.google.com/view/whatsappstatusdownloaderpolicy/home"
                    var intent = Intent(Intent.ACTION_VIEW)
                    intent.data = Uri.parse(uri)
                    startActivity(intent)
                } catch (e: ActivityNotFoundException) {
                    Toast.makeText(this," Probably Browser not installed :(", Toast.LENGTH_SHORT).show()
                }
            }
            else -> super.onOptionsItemSelected(item)
        }

        return super.onOptionsItemSelected(item)

    }

    private fun setupViewPageAdapter(viewPager: ViewPager) : SectionsPageAdapter {

        sectionsPageAdapter = SectionsPageAdapter(supportFragmentManager)

        sectionsPageAdapter.addFragment(Tab1Fragment(),"Images")
        sectionsPageAdapter.addFragment(Tab2Fragment(),"Videos")
        sectionsPageAdapter.addFragment(Tab3Fragment(),"Downloads")
        sectionsPageAdapter.addFragment(Tab4Fragment(),"Ads")

        viewPager.adapter = sectionsPageAdapter
        viewPager.offscreenPageLimit
        sectionsPageAdapter.notifyDataSetChanged()
        return sectionsPageAdapter
    }




    //Page Listeners Overriding Functions

    /*
    override fun onPageScrollStateChanged(state: Int) {

        Log.i("function", " sequence : onPageScrollStatechanged")
        if (state == 0) {
            pagechangecarryflag = 0

            Log.i(TAG,"sectionsviewpager value : ${sectionsPageAdapter.count}")

            var fragment2 = sectionsPageAdapter.getItem(1)
            Log.i(TAG, "The value of a : $fragment2")

            var fragmentTransaction: FragmentTransaction = supportFragmentManager.beginTransaction()

            fragmentTransaction.detach(fragment2)
            fragmentTransaction.attach(fragment2)

            fragmentTransaction.commit()
        }

        Log.i(TAG,"State value = $state")
    }

    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
        Log.i("function", " sequence : onPageScrolled")
    }

    override fun onPageSelected(position: Int) {
        Log.i("function", " sequence : onPageSelected")
        if(position == 2) {

            var mViewPager2 = findViewById<ViewPager>(R.id.container)
            var fragment3 = sectionsPageAdapter.getItem(position)
            Log.i(TAG,"The value of a : $fragment3")

            var fragmentTransaction: FragmentTransaction = supportFragmentManager.beginTransaction()

            fragmentTransaction.detach(fragment3)
            fragmentTransaction.attach(fragment3)

            fragmentTransaction.commit()
            pagechangecarryflag = 1

        } else {
            pagechangecarryflag = 0
        }
    }*/
}