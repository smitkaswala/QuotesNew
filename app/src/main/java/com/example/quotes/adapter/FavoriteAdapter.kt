package com.example.quotes.adapter

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.quotes.R
import com.example.quotes.activityclass.FavoriteImageActivity
import com.example.quotes.activityclass.ImageActivity
import com.example.quotes.apimodels.DataItem
import com.example.quotes.models.QuotesItems
import com.google.android.material.imageview.ShapeableImageView
import com.squareup.picasso.Picasso
import java.io.Serializable

class FavoriteAdapter(private val activity: Activity, private val list : List<QuotesItems>) : RecyclerView.Adapter<FavoriteAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_quotes_list,parent,false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        Picasso.get().load(list[position].imageURL).into(holder.itemImage)


        holder.itemView.setOnClickListener {

            val intent = Intent(activity, FavoriteImageActivity::class.java)
            intent.putExtra("imageList",list as Serializable)
            intent.putExtra("pos",position)
            activity.startActivity(intent)

        }

    }

    override fun getItemCount(): Int {
        return list.size
    }

    class ViewHolder (itemView : View) : RecyclerView.ViewHolder(itemView) {
        var itemImage : ShapeableImageView = itemView.findViewById(R.id.mImage)
    }

}



