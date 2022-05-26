package com.example.quotes.adapter

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.quotes.R
import com.example.quotes.activityclass.QuotesActivity

import com.example.quotes.models.DataItemList

class ItemsAdapter(private val context: Activity, private var list: ArrayList<DataItemList>) : RecyclerView.Adapter<ItemsAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_quotes, parent, false)
        return ViewHolder(v)

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.itemImage.setImageResource(list[position].image)

        holder.itemView.setOnClickListener {

            val intent = Intent(context, QuotesActivity::class.java)
            intent.putExtra("id", list[position].id)
            intent.putExtra("name", list[position].name)
            context.startActivity(intent)

        }

    }

    override fun getItemCount(): Int {
        return list.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var itemImage: ImageView = itemView.findViewById(R.id.mImage)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateList(quotes: ArrayList<DataItemList>)
    {
        list = quotes
        notifyDataSetChanged()
    }

}