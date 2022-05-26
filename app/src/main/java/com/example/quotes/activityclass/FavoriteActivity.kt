package com.example.quotes.activityclass

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView.VERTICAL
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.quotes.R
import com.example.quotes.adapter.FavoriteAdapter
import com.example.quotes.databinding.ActivityFavoriteBinding
import com.example.quotes.sqlitedatabase.SqliteDataBase

class FavoriteActivity : AppCompatActivity() {

    var binding : ActivityFavoriteBinding? =null
    var sqliteDataBase : SqliteDataBase? = null
    var adapter: FavoriteAdapter? = null

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_favorite)

        sqliteDataBase = SqliteDataBase(this, null)

        binding?.mMenu?.setOnClickListener {

            finish()

        }

    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onStart() {
        super.onStart()

        if (sqliteDataBase?.favoriteQuotes() != null ){

            binding?.mRecycler?.layoutManager = StaggeredGridLayoutManager(2,VERTICAL)
            adapter = FavoriteAdapter(this, sqliteDataBase?.favoriteQuotes()!!)
            binding?.mRecycler?.adapter = adapter
            binding?.mRelative?.visibility = View.GONE
            adapter?.notifyDataSetChanged()
        }

    }

}