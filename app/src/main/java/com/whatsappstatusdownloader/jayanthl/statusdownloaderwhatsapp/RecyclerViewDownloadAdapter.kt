package com.whatsappstatusdownloader.jayanthl.statusdownloaderwhatsapp


import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Environment
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

class RecyclerViewDownloadadapter(context: Context, finalNames: MutableList<String>, ExtractedList: MutableList<String>) :RecyclerView.Adapter<RecyclerViewDownloadadapter.DownloadViewHolder> (){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerViewDownloadadapter.DownloadViewHolder {
        var view:View = LayoutInflater.from(parent.context).inflate(R.layout.image_layout,parent,false)
        var holder = DownloadViewHolder(view)
        return holder
    }

    override fun getItemCount(): Int {
        return finalNames.size
    }

    override fun onBindViewHolder(holder: RecyclerViewDownloadadapter.DownloadViewHolder, position: Int) {
        var requestOptions: RequestOptions = RequestOptions().fitCenter().centerCrop()
        var uri: Uri = Uri.fromFile(File(finalNames[position]))
        Glide.with(mContext).load(uri).apply(requestOptions).transition(DrawableTransitionOptions.withCrossFade()).into(holder.videoView)

        /*if(finalNames[position].endsWith(".mp4")) {
            holder.superposeView.setImageResource(R.drawable.ic_video_superpose)
            //Glide.with(mContext).load(R.drawable.ic_triangl).apply(requestOptions).into(holder.superposeView)
            Log.i(TAG,"applying filter at ${finalNames[position]} and $position")
        }*/

        var imageDestinationPath: String = Environment.getExternalStorageDirectory().toString() + "/StatusWhatsApp/Images/" +"${ExtractedList[position]}"




        Log.i("now","Triggered")
        Log.i("now rem","$finalNames")

        holder.videoView.setOnLongClickListener(View.OnLongClickListener {


            var popupMenu = PopupMenu(mContext,it)
            var menuInflater: MenuInflater = popupMenu.menuInflater
            menuInflater.inflate(R.menu.deletemenu_item,popupMenu.menu)
            popupMenu.show()
            popupMenu.setOnMenuItemClickListener {
                when(it.itemId) {
                    R.id.action_delete -> {
                        if (File(imageDestinationPath).exists()) {
                            Log.i("Tapped", "At : $position")
                            File(imageDestinationPath).delete()
                            finalNames.removeAt(position)
                            ExtractedList.removeAt(position)
                            Log.i("now begin", "$finalNames")
                            notifyItemRemoved(position)
                            Toast.makeText(mContext, "Deleted!", Toast.LENGTH_SHORT).show()
                            notifyItemRangeChanged(position, finalNames.size)
                            notifyItemRangeChanged(position, ExtractedList.size)
                            var vibrator: Vibrator = mContext.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator

                            vibrator.vibrate(100)
                            Log.i("now kone", "$finalNames")
                        }
                    }

                    R.id.action_share -> {
                        if(finalNames[position].contains(".mp4")) {

                            var shareImage = Intent()
                            shareImage.setAction(Intent.ACTION_SEND)
                            shareImage.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                            var uri2: Uri? = FileProvider.getUriForFile(mContext, mContext.applicationContext.packageName + ".com.whatsappstatusdownloader.jayanthl.statusdownloaderwhatsapp.provider", File(finalNames[position]))
                            shareImage.setType("video/*")
                            shareImage.putExtra(Intent.EXTRA_STREAM, uri2)
                            shareImage.setPackage("com.whatsapp")
                            try {

                                mContext.startActivity(shareImage)
                            } catch (e: ActivityNotFoundException) {
                                Toast.makeText(mContext, "Probably WhatsApp Not installed!", Toast.LENGTH_SHORT).show()
                            }

                        } else {
                            var shareImage = Intent()
                            shareImage.setAction(Intent.ACTION_SEND)
                            shareImage.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                            var uri2: Uri? = FileProvider.getUriForFile(mContext, mContext.applicationContext.packageName + ".com.whatsappstatusdownloader.jayanthl.statusdownloaderwhatsapp.provider", File(finalNames[position]))
                            shareImage.setType("image/*")
                            shareImage.putExtra(Intent.EXTRA_STREAM, uri2)
                            shareImage.setPackage("com.whatsapp")
                            try {
                                mContext.startActivity(shareImage)
                            } catch (e: ActivityNotFoundException) {
                                Toast.makeText(mContext, "Probably WhatsApp Not installed!", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }

                }
                return@setOnMenuItemClickListener false
            }

            return@OnLongClickListener true
        })



        holder.videoView.setOnClickListener {


            if(finalNames[position].contains(".mp4")) {

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
                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE,true)

                    var multData: ArrayList <Uri> = ArrayList()
                    for ( i in 0..finalNames.size -1) {
                        multData.add(FileProvider.getUriForFile(mContext,mContext.applicationContext.packageName + ".com.whatsappstatusdownloader.jayanthl.statusdownloaderwhatsapp.provider",File(finalNames[i])))
                    }
                    intent.putParcelableArrayListExtra(Intent.EXTRA_STREAM,multData)
                    var uri2: Uri? = FileProvider.getUriForFile(mContext,mContext.applicationContext.packageName + ".com.whatsappstatusdownloader.jayanthl.statusdownloaderwhatsapp.provider",File(finalNames[position]))
                    intent.setDataAndType(uri2,"video/*")
                    try {
                        mContext.startActivity(intent)
                    } catch (e: ActivityNotFoundException) {}
                }
            } else {
                var imageNames: ArrayList<String> = ArrayList()
                for (i in finalNames) {
                    if(i.endsWith(".jpg"))  {
                        imageNames.add(i)
                    }
                }
                var extractedNames: ArrayList<String> = ArrayList()
                for(j in ExtractedList) {
                    if(j.endsWith(".jpg")) {
                        extractedNames.add(j)
                    }
                }
                var intent = Intent(mContext,ImageContainerViewActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                intent.putStringArrayListExtra("imageNames", imageNames)
                intent.putExtra("adapterType", 1)
                intent.putExtra("position", position)
                intent.putStringArrayListExtra("extractedNames", extractedNames)
                mContext!!.startActivity(intent)
            }
        }
    }

    var mContext: Context
    var finalNames: MutableList<String>
    var ExtractedList: MutableList<String>
    init {
        this.mContext = context
        this.finalNames = finalNames
        this.ExtractedList = ExtractedList
    }

    class DownloadViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var videoView: ImageView
        init {
            videoView = itemView.findViewById(R.id.imageview)
        }
    }
}