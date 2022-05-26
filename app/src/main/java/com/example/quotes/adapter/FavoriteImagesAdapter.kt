package com.example.quotes.adapter

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.net.Uri
import android.os.Environment
import android.os.StrictMode
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.core.graphics.drawable.toBitmap
import androidx.recyclerview.widget.RecyclerView
import com.example.quotes.R
import com.example.quotes.apimodels.DataItem
import com.example.quotes.models.QuotesItems
import com.example.quotes.sqlitedatabase.SqliteDataBase
import com.google.android.material.imageview.ShapeableImageView
import com.squareup.picasso.Picasso
import java.io.*
import java.text.SimpleDateFormat
import java.util.*

class FavoriteImagesAdapter(private val activity: Activity, private val list: List<QuotesItems>, var sqliteDataBase: SqliteDataBase) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        val v = LayoutInflater.from(parent.context).inflate(R.layout.items_images_list, parent, false)
        return PagingHolder(v)

    }

    @SuppressLint( "SimpleDateFormat", "UseCompatLoadingForDrawables")
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        var holderImage = holder as PagingHolder

        Picasso.get().load(list[position].imageURL).into(holder.itemImage)

        if (sqliteDataBase.checkIfUserExit(list[position].imageId.toString())){
            holder.mFavorite.setImageResource(R.drawable.ic_like_done)
        }else{
            holder.mFavorite.setImageResource(R.drawable.ic_like_)
        }

        val builder: StrictMode.VmPolicy.Builder = StrictMode.VmPolicy.Builder()
        StrictMode.setVmPolicy(builder.build())

        holder.mFavorite.setOnClickListener {


                holder.mFavorite.setImageResource(R.drawable.ic_like_done)
                sqliteDataBase.deleteQuotes(list[position].imageId.toString())
                notifyDataSetChanged()


        }

        holder.mDownload.setOnClickListener {

            val fileOutputStream : FileOutputStream

            val file : File = getDisk()

            if (!file.exists()){

                file.mkdirs()

            }

            val simpleDateFormat = SimpleDateFormat("yyyysshhmmss")

            val date = simpleDateFormat.format(Date())

            val name = "IMG$date.jpg"

            val fileName = file.absolutePath + "/" + name

            val newFile = File(fileName)

            try {

                val bitmap : Bitmap = viewToBitmap(holderImage.itemImage,holderImage.itemImage.width,holderImage.itemImage.height)

                fileOutputStream = FileOutputStream(newFile)
                bitmap.compress(Bitmap.CompressFormat.JPEG,100,fileOutputStream)
                Toast.makeText(activity, "save image successfully", Toast.LENGTH_SHORT).show()
                fileOutputStream.flush()
                fileOutputStream.close()

            }catch (e : FileNotFoundException){
                e.printStackTrace()
            }catch (e : IOException){
                e.printStackTrace()
            }

            refreshGallery(newFile)

        }

        holder.mShare.setOnClickListener {


            val intent = Intent(Intent.ACTION_SEND).setType("image/*")

            val bitmap = holderImage.itemImage.drawable.toBitmap() // your imageView here.

            val bytes = ByteArrayOutputStream()

            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes)

            val path = MediaStore.Images.Media.insertImage(activity.contentResolver, bitmap, "", null)

            val uri = Uri.parse(path)

            intent.putExtra(Intent.EXTRA_STREAM, uri)

            activity.startActivity(intent)


        }

//        onclick.onClick(position,list,image)

    }

    override fun getItemCount(): Int {
        return list.size
    }

    class PagingHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var itemImage: ShapeableImageView = itemView.findViewById(R.id.mImage)
        var mFavorite: ImageView = itemView.findViewById(R.id.mFavorite)
        var mDownload: ImageView = itemView.findViewById(R.id.mDownload)
        var mShare:    ImageView = itemView.findViewById(R.id.mShare)

    }

    private fun getDisk() : File  {

        val file : File = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
        return File(file,"Quotes")

    }

    private fun refreshGallery(file : File){

        val intent = Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE)
        intent.data = Uri.fromFile(file)
        activity.sendBroadcast(intent)

    }

    private fun viewToBitmap(view : View, width : Int , height : Int): Bitmap {

        val bitmap = Bitmap.createBitmap(width,height,Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        view.draw(canvas)
        return bitmap

    }

}