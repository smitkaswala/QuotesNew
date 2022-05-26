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
import com.example.quotes.retrofit.ItemClick
import com.example.quotes.retrofit.ShareClick
import com.example.quotes.sqlitedatabase.SqliteDataBase
import com.google.android.material.imageview.ShapeableImageView
import com.squareup.picasso.Picasso
import java.io.*
import java.text.SimpleDateFormat
import java.util.*

class ImagesPageAdapter(private val activity: Activity,private val list: List<DataItem>, var image : String, var sqliteDataBase: SqliteDataBase, var imageId : Int, var id : Int, var onItem : ItemClick, var shareClick : ShareClick) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        val v = LayoutInflater.from(parent.context).inflate(R.layout.items_images_list, parent, false)
        return PagingHolder(v)

    }

    @SuppressLint( "SimpleDateFormat", "UseCompatLoadingForDrawables")
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        var holderImage = holder as PagingHolder

        if (position == 0){

            if (sqliteDataBase.checkIfUserExit(imageId.toString())){
                holder.mFavorite.setImageResource(R.drawable.ic_like_done)
            }else{
                holder.mFavorite.setImageResource(R.drawable.ic_like_)
            }

        }else{

            if (sqliteDataBase.checkIfUserExit(list[position].id.toString())){
                holder.mFavorite.setImageResource(R.drawable.ic_like_done)
            }else{
                holder.mFavorite.setImageResource(R.drawable.ic_like_)
            }

        }

        val builder: StrictMode.VmPolicy.Builder = StrictMode.VmPolicy.Builder()
        StrictMode.setVmPolicy(builder.build())


        if (position == 0){

            Picasso.get().load(image).into(holderImage.itemImage)

            holder.mFavorite.setOnClickListener {


                if (holder.mFavorite.drawable?.constantState == activity.resources.getDrawable(R.drawable.ic_like_).constantState) {
                    holder.mFavorite.setImageResource(R.drawable.ic_like_done)

                    sqliteDataBase.addName(imageId,image,id)

                } else {
                    holder.mFavorite.setImageResource(R.drawable.ic_like_)
                    sqliteDataBase.deleteQuotes(imageId.toString())

                }

            }

        }else
        {
            Picasso.get().load(list[position].image).into(holderImage.itemImage)

            holder.mFavorite.setOnClickListener {

                if (holder.mFavorite.drawable?.constantState == activity.resources.getDrawable(R.drawable.ic_like_).constantState) {
                    holder.mFavorite.setImageResource(R.drawable.ic_like_done)
                    list[position].id?.let {
                        list[position].categoryId?.let { it1 ->
                            list[position].image?.let { it2 ->
                                sqliteDataBase.addName(it, it2, it1)
                            }
                        }
                    }
                } else {
                    holder.mFavorite.setImageResource(R.drawable.ic_like_)
                    sqliteDataBase.deleteQuotes(list[position].id.toString())
                }

            }

        }

        holder.mDownload.setOnClickListener {

            onItem.onClick(holderImage.itemImage)


        }

        holder.mShare.setOnClickListener {

            shareClick.shareClick(holderImage.itemImage)

        }

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


}