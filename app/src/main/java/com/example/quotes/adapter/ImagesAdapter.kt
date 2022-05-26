package com.example.quotes.adapter

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.quotes.R
import com.example.quotes.activityclass.ImageActivity
import com.example.quotes.activityclass.QuotesActivity
import com.example.quotes.apimodels.DataItem
import com.google.android.material.imageview.ShapeableImageView
import com.squareup.picasso.Picasso

class ImagesAdapter(
    private val list: List<DataItem>,
    private val image: String ) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        val layoutInflater = LayoutInflater.from(parent.context)
        val view: View


            view = layoutInflater.inflate(R.layout.items_images, parent, false)
            return ViewHolder(view)


    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {


            var holderImage = holder as ViewHolder

            Picasso.get().load(image).into(holderImage.itemImage)

    }

    override fun getItemCount(): Int {

        return list.size

    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var itemImage: ShapeableImageView = itemView.findViewById(R.id.mImage)

    }

    @SuppressLint("NotifyDataSetChanged")
    fun addAll(responseNews: List<DataItem>) {

        list.toMutableList().addAll(responseNews)
        notifyDataSetChanged();
    }

}