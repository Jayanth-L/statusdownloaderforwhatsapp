package com.whatsappstatusdownloader.jayanthl.statusdownloaderwhatsapp

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Environment
import android.os.Vibrator
import android.support.v4.content.FileProvider
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.*
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.Toast
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import java.io.File

class RecyclerViewAdapter(context: Context, dataNames: MutableList<String>, ExtractedNames: MutableList<String>, interstitialAdNumber: Int) : RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>(), View.OnCreateContextMenuListener {

    var mContext: Context
    var dataNames: MutableList<String>
    var ExtractedNames: MutableList<String>
    var interstitialAdNumber: Int
    init {
        this.dataNames = dataNames
        this.mContext = context
        this.ExtractedNames = ExtractedNames
        this.interstitialAdNumber = interstitialAdNumber
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var view: View = LayoutInflater.from(parent.context).inflate(R.layout.image_layout,parent,false)
        var holder: ViewHolder = ViewHolder(view,mContext)
        return holder

    }

    override fun getItemCount(): Int {
        return dataNames.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        var requestOptions: RequestOptions = RequestOptions().fitCenter().centerCrop()
        var uri: Uri = Uri.fromFile(File(dataNames[position]))
        Glide.with(mContext).load(uri).apply(requestOptions).transition(DrawableTransitionOptions.withCrossFade()).into(holder.imageView)

        var destinationPath: String = Environment.getExternalStorageDirectory().toString() + "/StatusWhatsApp/Images/" +"${ExtractedNames[position]}"

        /*
        //if the File exists in the destination directory then set it to Gray Scale
        if(File(destinationPath).exists()) {
            var colorMatrix: ColorMatrix = ColorMatrix()
            colorMatrix.setSaturation(0.toFloat())

            var filter: ColorMatrixColorFilter = ColorMatrixColorFilter(colorMatrix)
            holder.imageView.setColorFilter(filter)
        }
        */



        holder.imageView.setOnLongClickListener(View.OnLongClickListener {



            var destinationPath: String = Environment.getExternalStorageDirectory().toString() + "/StatusWhatsApp/Images/" +"${ExtractedNames[position]}"

            var popupMenu = PopupMenu(mContext,it)
            var menuInflater: MenuInflater = popupMenu.menuInflater
            menuInflater.inflate(R.menu.popupmenu_item,popupMenu.menu)
            popupMenu.show()
            popupMenu.setOnMenuItemClickListener {

                when(it.itemId) {
                    R.id.action_download -> {

                        if(!(File(destinationPath).exists())) {


                            File(dataNames[position]).copyTo(File(destinationPath),false)
                            Toast.makeText(mContext,"File Successfully Saved as ${ExtractedNames[position]}",Toast.LENGTH_SHORT).show()

                            var vibrator: Vibrator = mContext.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator

                            vibrator.vibrate(100)

                            //Gray scale o indicate the File is saved

                            /*var colorMatrix: ColorMatrix = ColorMatrix()
                            colorMatrix.setSaturation(0.toFloat())

                            var filter: ColorMatrixColorFilter = ColorMatrixColorFilter(colorMatrix)
                            holder.imageView.setColorFilter(filter)
                            */


                        } else {

                            Toast.makeText(mContext,"Status Already Saved!",Toast.LENGTH_SHORT).show()

                        }

                    }

                    R.id.action_putstatus -> {

                        var shareImage = Intent()
                        shareImage.setAction(Intent.ACTION_SEND)
                        shareImage.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        var uri2: Uri? = FileProvider.getUriForFile(mContext,mContext.applicationContext.packageName + ".com.whatsappstatusdownloader.jayanthl.statusdownloaderwhatsapp.provider",File(dataNames[position]))
                        shareImage.setType("image/*")
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


        holder.imageView.setOnClickListener(View.OnClickListener {

            interstitialAdNumber = interstitialAdNumber + 1
            Log.i("Adapter", "InterstitialAdNumber : $interstitialAdNumber")

            if(ExtractedNames[position] == "downloadall" && position == dataNames.size -1) {
                //Do Nothing
            } else {

                var imageNames: ArrayList<String> = ArrayList()
                var extractedData: ArrayList<String> = ArrayList()

                for (i in dataNames) {
                    imageNames.add(i)
                }

                for(i in ExtractedNames) {
                    extractedData.add(i)
                }


                var intent = Intent(mContext,ImageContainerViewActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                intent.putStringArrayListExtra("imageNames", imageNames)
                intent.putExtra("adapterType", 0)
                intent.putExtra("position", position)
                intent.putStringArrayListExtra("extractedNames", extractedData)
                mContext!!.startActivity(intent)
            }
        })
    }


    override fun onCreateContextMenu(menu: ContextMenu?, v: View?, menuInfo: ContextMenu.ContextMenuInfo?) {

        //var menuItem: MenuItem = menu!!.add("hello")
        Toast.makeText(mContext,"clicking create",Toast.LENGTH_SHORT).show()
    }




    class ViewHolder(itemView: View, mContext: Context) : RecyclerView.ViewHolder(itemView) {
        var imageView: ImageView
        var mContext: Context
        //var parentLayout: RelativeLayout
        init {
            imageView = itemView.findViewById(R.id.imageview)
            //parentLayout = itemView.findViewById(R.id.parent_layout)
            this.mContext = mContext

        }
    }

}


