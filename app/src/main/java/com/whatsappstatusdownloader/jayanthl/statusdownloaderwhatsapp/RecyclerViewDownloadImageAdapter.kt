package com.whatsappstatusdownloader.jayanthl.statusdownloaderwhatsapp

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Vibrator
import android.support.v4.content.FileProvider
import android.support.v7.widget.RecyclerView
import android.util.Log
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
import java.io.File
// This class is used by fragment 4

class RecyclerViewDownloadImageAdapter(mContext: Context, videoImageList: MutableList<String> ) : RecyclerView.Adapter<RecyclerViewDownloadImageAdapter.DownloadImageViewhHolder> (){

    var mContext: Context
    var videoImageList: MutableList<String>

    init {
        this.mContext = mContext
        this.videoImageList = videoImageList
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DownloadImageViewhHolder {
        var view: View = LayoutInflater.from(mContext).inflate(R.layout.image_layout,parent, false)
        var holder = DownloadImageViewhHolder(view)
        return holder
    }

    override fun getItemCount(): Int {
        return videoImageList.size
    }

    override fun getItemId(position: Int): Long {
        return super.getItemId(position)
    }

    override fun onBindViewHolder(holder: DownloadImageViewhHolder, position: Int) {

        var requestOptions = RequestOptions().fitCenter().centerCrop()
        var uri: Uri = Uri.fromFile(File(videoImageList[position]))
        Glide.with(mContext).load(uri).apply(requestOptions).transition(DrawableTransitionOptions.withCrossFade()).into(holder.videoImage)



        holder.videoImage.setOnLongClickListener(View.OnLongClickListener {



            var popupMenu = PopupMenu(mContext,it)
            var menuInflater: MenuInflater = popupMenu.menuInflater
            menuInflater.inflate(R.menu.deletemenu_item,popupMenu.menu)
            popupMenu.show()
            popupMenu.setOnMenuItemClickListener {
                when(it.itemId) {
                    R.id.action_delete -> {
                        var imageDestinationPath: String = videoImageList[position]
                        if (File(imageDestinationPath).exists()) {
                            Log.i("Tapped", "At : $position")
                            File(imageDestinationPath).delete()
                            videoImageList.removeAt(position)
                            Log.i("now begin", "$videoImageList")
                            notifyItemRemoved(position)
                            Toast.makeText(mContext, "Deleted!", Toast.LENGTH_SHORT).show()
                            notifyItemRangeChanged(position, videoImageList.size)
                            Log.i("now kone", "$videoImageList")
                            var vibrator: Vibrator = mContext.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator

                            vibrator.vibrate(100)
                        }
                    }

                    R.id.action_share -> {

                        var shareVideo = Intent()
                        shareVideo.setAction(Intent.ACTION_SEND)
                        shareVideo.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        var uri2: Uri? = FileProvider.getUriForFile(mContext, mContext.applicationContext.packageName + ".com.whatsappstatusdownloader.jayanthl.statusdownloaderwhatsapp.provider", File(videoImageList[position]))
                        shareVideo.setType("video/*")
                        shareVideo.putExtra(Intent.EXTRA_STREAM, uri2)
                        shareVideo.setPackage("com.whatsapp")
                        try {
                            mContext.startActivity(shareVideo)
                        } catch (e: ActivityNotFoundException) {
                            Toast.makeText(mContext, "Probably WhatsApp Not installed!", Toast.LENGTH_SHORT).show()
                        }

                    }

                }
                return@setOnMenuItemClickListener false
            }

            return@OnLongClickListener true
        })



        holder.videoImage.setOnClickListener {

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
                var uri2: Uri? = FileProvider.getUriForFile(mContext,mContext.applicationContext.packageName + ".com.whatsappstatusdownloader.jayanthl.statusdownloaderwhatsapp.provider",File(videoImageList[position]))
                intent.setDataAndType(uri2,"video/*")
                try {
                    mContext.startActivity(intent)
                } catch (e: ActivityNotFoundException) {}
            }
        }
    }






    class DownloadImageViewhHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var videoImage: ImageView
        init {
            this.videoImage = itemView.findViewById(R.id.imageview)
        }

    }
}