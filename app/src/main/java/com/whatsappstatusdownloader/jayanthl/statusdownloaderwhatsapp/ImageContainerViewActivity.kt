package com.whatsappstatusdownloader.jayanthl.statusdownloaderwhatsapp

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.os.Vibrator
import android.support.v4.content.FileProvider
import android.support.v4.view.ViewPager
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.Toast
import java.io.File

class ImageContainerViewActivity : AppCompatActivity() {

    val TAG: String = "ImageContainer"

    var position = -1
    var adapterType = -1

    lateinit var viewpager: ViewPager

    lateinit var imageNames: MutableList<String>
    lateinit var extractedNames: MutableList<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image_container_view)

        imageNames = intent.getStringArrayListExtra("imageNames")
        extractedNames = intent.getStringArrayListExtra("extractedNames")
        adapterType = intent.getIntExtra("adapterType", -1)
        position = intent.getIntExtra("position", -1)
        viewpager = findViewById(R.id.imageContainer)

        var imageViewAdapter = ImageViewAdapter(this,imageNames)
        viewpager.adapter = imageViewAdapter
        viewpager.currentItem = position

    }


    override fun onCreatePanelMenu(featureId: Int, menu: Menu?): Boolean {
        val inflater = MenuInflater(this)
        if(adapterType == 0) {
            inflater.inflate(R.menu.imageview_menu, menu)
        } else if (adapterType == 1) {
            inflater.inflate(R.menu.imageview_deletemenu, menu)
        }

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        var currentposition = viewpager.currentItem
        when(item!!.itemId) {
            R.id.action_imageviewdownload -> {
                var destinationPath = Environment.getExternalStorageDirectory().toString() + "/StatusWhatsApp/Images/" + extractedNames[currentposition]
                if(!File(destinationPath).exists()) {
                    try {
                        File(imageNames[currentposition]).copyTo(File(destinationPath), false)
                        var vibrator: Vibrator = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
                        vibrator.vibrate(100)
                        Toast.makeText(this, "Status successfully saved as ${extractedNames[currentposition]}", Toast.LENGTH_SHORT).show()
                    } catch (e: NoSuchFileException) {
                        Toast.makeText(this, "An error occurred Please try once again!", Toast.LENGTH_LONG).show()
                    }
                } else { Toast.makeText(this, "Status Already saved", Toast.LENGTH_SHORT).show() }
                return true
            }
            R.id.action_imageviewshare -> {
                shareonWhatsApp(currentposition)
                return true
            }
            R.id.action_imageviewsecondshareonwhatsapp -> {
                shareonWhatsApp(currentposition)
                return true
            }
            else -> return false
        }
        return false
    }

    //A function to share on WhatsApp
    private fun shareonWhatsApp(currentposition: Int) {
        var shareImage = Intent()
        shareImage.setAction(Intent.ACTION_SEND)
        shareImage.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        var uri2: Uri? = FileProvider.getUriForFile(this,applicationContext.packageName + ".com.whatsappstatusdownloader.jayanthl.statusdownloaderwhatsapp.provider",File(imageNames[currentposition]))
        shareImage.setType("image/*")
        shareImage.putExtra(Intent.EXTRA_STREAM,uri2)
        shareImage.setPackage("com.whatsapp")
        try {
            startActivity(shareImage)
        } catch (e: ActivityNotFoundException) {
            Toast.makeText(this, "Probably WhatsApp Not installed!", Toast.LENGTH_SHORT).show()
        }
    }
}
