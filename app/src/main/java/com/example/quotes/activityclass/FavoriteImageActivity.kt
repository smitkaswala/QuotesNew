package com.example.quotes.activityclass

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SnapHelper
import com.example.quotes.R
import com.example.quotes.adapter.FavoriteImagesAdapter
import com.example.quotes.databinding.ActivityFavoriteImageBinding
import com.example.quotes.models.QuotesItems
import com.example.quotes.sqlitedatabase.SqliteDataBase

class FavoriteImageActivity : AppCompatActivity() {

    lateinit var list: List<QuotesItems>
    private var position: Int = 0
    var binding : ActivityFavoriteImageBinding? = null
    var sqliteDataBase : SqliteDataBase? = null
    var adapter: FavoriteImagesAdapter? = null

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_favorite_image)

        sqliteDataBase = SqliteDataBase(this, null)

        if (intent != null) {

            list = intent.extras?.get("imageList") as List<QuotesItems>
            position = intent.getIntExtra("pos", 0)

        }

        val linearLayoutManager = LinearLayoutManager(this,RecyclerView.VERTICAL,false)
        binding?.mRecycler?.layoutManager = linearLayoutManager
        adapter = FavoriteImagesAdapter(this,list,sqliteDataBase!!)
        binding?.mRecycler?.adapter = adapter
        val snapHelper : SnapHelper = PagerSnapHelper()
        binding?.mRecycler?.onFlingListener = null
        snapHelper.attachToRecyclerView(binding?.mRecycler)
        binding?.mRecycler?.scrollToPosition(position)
        adapter?.notifyDataSetChanged()

        binding?.mMenu?.setOnClickListener {

            finish()

        }

    }

}