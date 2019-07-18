package com.whatsappstatusdownloader.jayanthl.statusdownloaderwhatsapp

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.os.Vibrator
import android.support.v4.content.FileProvider
import android.support.v4.view.ViewPager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.MenuInflater
import android.view.View
import android.view.ViewGroup

import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.Toast
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.whatsappstatusdownloader.jayanthl.statusdownloaderwhatsapp.R.id.container
import java.io.File

class RecyclerViewVideoAdapter(context: Context, videoNames: MutableList<String>, ExtractedList: MutableList<String>) :RecyclerView.Adapter<RecyclerViewVideoAdapter.VideoViewHolder> (){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerViewVideoAdapter.VideoViewHolder {
        var view:View = LayoutInflater.from(parent.context).inflate(R.layout.image_layout,parent,false)
        var holder: VideoViewHolder = VideoViewHolder(view)
        return holder
    }

    override fun getItemCount(): Int {
        return videoNames.size
    }

    override fun onBindViewHolder(holder: RecyclerViewVideoAdapter.VideoViewHolder, position: Int) {
        var requestOptions: RequestOptions = RequestOptions().fitCenter().centerCrop()
        var uri: Uri = Uri.fromFile(File(videoNames[position]))

        Glide.with(mContext).load(uri).apply(requestOptions).transition(DrawableTransitionOptions.withCrossFade()).into(holder.videoView)

        var destinationPath: String = Environment.getExternalStorageDirectory().toString() + "/StatusWhatsApp/Videos/" +"${ExtractedList[position]}"

        /*if(File(destinationPath).exists()) {
            var colorMatrix: ColorMatrix = ColorMatrix()
            colorMatrix.setSaturation(0.toFloat())

            var filter: ColorMatrixColorFilter = ColorMatrixColorFilter(colorMatrix)
            holder.videoView.setColorFilter(filter)
        }
        */




        holder.videoView.setOnLongClickListener(View.OnLongClickListener {


            var destinationPath: String = Environment.getExternalStorageDirectory().toString() + "/StatusWhatsApp/Videos/" +"${ExtractedList[position]}"

            var popupMenu = PopupMenu(mContext,it)
            var menuInflater: MenuInflater = popupMenu.menuInflater
            menuInflater.inflate(R.menu.popupmenu_item,popupMenu.menu)
            popupMenu.show()
            popupMenu.setOnMenuItemClickListener {

                when(it.itemId) {
                    R.id.action_download -> {
                        if(!(File(destinationPath).exists())) {

                            File(videoNames[position]).copyTo(File(destinationPath),false)
                            Toast.makeText(mContext,"File Successfully Saved as ${ExtractedList[position]}",Toast.LENGTH_SHORT).show()

                            var vibrator: Vibrator = mContext.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator

                            vibrator.vibrate(100)

                            //Gray scale o indicate the File is saved

                           /* var colorMatrix: ColorMatrix = ColorMatrix()
                            colorMatrix.setSaturation(0.toFloat())

                            var filter: ColorMatrixColorFilter = ColorMatrixColorFilter(colorMatrix)
                            holder.videoView.setColorFilter(filter)
                            */

                        } else {

                            Toast.makeText(mContext,"Status Already Saved!",Toast.LENGTH_SHORT).show()

                        }
                    }

                    R.id.action_putstatus -> {
                        var shareImage = Intent()
                        shareImage.setAction(Intent.ACTION_SEND)
                        shareImage.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        var uri2: Uri? = FileProvider.getUriForFile(mContext,mContext.applicationContext.packageName + ".com.whatsappstatusdownloader.jayanthl.statusdownloaderwhatsapp.provider",File(videoNames[position]))
                        shareImage.setType("video/*")
                        shareImage.putExtra(Intent.EXTRA_STREAM,uri2)
                        shareImage.setPackage("com.whatsapp")
                        try {
                            mContext.startActivity(shareImage)
                        } catch (e: ActivityNotFoundException) {
                            Toast.makeText(mContext, "Probably WhatsApp Not installed!", Toast.LENGTH_SHORT).show()
                        }
                    }
                }

                return@setOnMenuItemClickListener false
            }





            return@OnLongClickListener true
        })



        holder.videoView.setOnClickListener {



            if(ExtractedList[position] == "downloadall" && position == ExtractedList.size - 1) {

               //Do Nothing

            } else {

                if(Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {

                    var intent = Intent(Intent.ACTION_VIEW)
                    intent.setAction(Intent.ACTION_VIEW)
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    intent.setDataAndType(uri,"video/*")
                    try {
                        mContext.startActivity(intent)
                    } catch (e: ActivityNotFoundException) {}

                } else {
                    var intent: Intent = Intent(Intent.ACTION_VIEW)
                    intent.setAction(Intent.ACTION_VIEW)
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                    intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE,true)

                    var multData: ArrayList <Uri> = ArrayList()
                    /*for ( i in 0..videoNames.size -1) {
                        multData.add(FileProvider.getUriForFile(mContext,mContext.applicationContext.packageName + ".com.whatsappstatusdownloader.jayanthl.statusdownloaderwhatsapp.provider",File(videoNames[i])))
                    }*/
                    intent.putParcelableArrayListExtra(Intent.EXTRA_STREAM,multData)
                    var uri2: Uri? = FileProvider.getUriForFile(mContext,mContext.applicationContext.packageName + ".com.whatsappstatusdownloader.jayanthl.statusdownloaderwhatsapp.provider",File(videoNames[position]))
                    intent.setDataAndType(uri2,"video/*")
                    try {
                        mContext.startActivity(intent)
                    } catch (e: ActivityNotFoundException) {}
                }

            }


            /*"""
            var videoData: ArrayList<String> = ArrayList()
            for (i in videoNames) {
                videoData.add(i)
            }

            var intent: Intent = Intent(mContext,VideoContainerViewActivity::class.java)
            intent.putExtra("position",position)
            intent.putStringArrayListExtra("videoData",videoData)
            mContext.startActivity(intent)
            """*/
        }
    }




    var mContext: Context
    var videoNames: MutableList<String>
    var ExtractedList: MutableList<String>
    init {
        this.mContext = context
        this.videoNames = videoNames
        this.ExtractedList = ExtractedList
    }

    class VideoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var videoView: ImageView
        init {
            videoView = itemView.findViewById(R.id.imageview)
        }
    }
}
