package com.example.quotes.adapter

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.paging.PagingData
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.quotes.R
import com.example.quotes.activityclass.ImageActivity
import com.example.quotes.apimodels.DataItem
import com.example.quotes.apimodels.Quotes
import com.google.android.material.imageview.ShapeableImageView
import com.squareup.picasso.Picasso
import java.io.Serializable

class DogAdapter
constructor(val context: Activity,var id : Int,var name : String) : PagingDataAdapter<DataItem, RecyclerView.ViewHolder>(DiffUtils) {

    class MyViewHolder(view: View): RecyclerView.ViewHolder(view) {

        val tvName: ShapeableImageView = view.findViewById(R.id.mImage)

    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        val photosItem = getItem(position)

        val holderNew : MyViewHolder = holder as MyViewHolder

        Picasso.get().load(photosItem?.image).into(holderNew.tvName)

        holderNew.itemView.setOnClickListener {

            val intent = Intent(context, ImageActivity::class.java)
            intent.putExtra("imageList", listOf(photosItem) as Serializable)
            intent.putExtra("image",photosItem?.image)
            intent.putExtra("imageId",photosItem?.id)
            intent.putExtra("pos",position)
            intent.putExtra("name",name)
            intent.putExtra("id",photosItem?.categoryId)
            context.startActivity(intent)

        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context).inflate(R.layout.item_quotes_list, parent, false)
        return MyViewHolder(inflater)
    }

    object DiffUtils : DiffUtil.ItemCallback<DataItem>(){
        override fun areItemsTheSame(oldItem: DataItem, newItem: DataItem): Boolean {
            return oldItem.id == newItem.id
        }
        override fun areContentsTheSame(oldItem: DataItem, newItem: DataItem): Boolean {
            return oldItem == newItem
        }
    }

}












