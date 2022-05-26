package com.example.quotes.activityclass

import android.app.Dialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.*
import com.example.quotes.R
import com.example.quotes.adapter.DogAdapter
import com.example.quotes.adapter.FavoriteAdapter
import com.example.quotes.apimodels.ItemsResponse
import com.example.quotes.apimodels.Quotes
import com.example.quotes.databinding.ActivityQuotesBinding
import com.example.quotes.retrofit.RetrofitInstance
import com.example.quotes.retrofit.RetrofitServices
import com.example.quotes.viewmodels.ActivityViewModel
import com.example.quotes.viewmodels.MainViewModel
import kotlinx.coroutines.flow.collectLatest
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class QuotesActivity : AppCompatActivity() {


    lateinit var binding: ActivityQuotesBinding
    lateinit var name: String
    private var page: Int = 1
    private var id: Int = 0

    lateinit var recyclerViewAdapter: DogAdapter

    var mainViewModel: MainViewModel?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_quotes)

        mainViewModel = ViewModelProvider(this)[MainViewModel::class.java]

        if (intent != null) {

            id = intent.getIntExtra("id", 0)
            name = intent.getStringExtra("name")!!

        }

        binding.mText.text = name



        quotesView(id)


        binding.mMenu.setOnClickListener {
            finish()
        }


    }

    fun quotesView(id: Int) {

        val retrofitService =
            RetrofitInstance.getRetrofitInstance().create(RetrofitServices::class.java)

        val call = retrofitService.quotesItems("list/5/$id", page)

        call.enqueue(object : Callback<ItemsResponse> {
            override fun onResponse(call: Call<ItemsResponse>, response: Response<ItemsResponse>) {

                if (response.isSuccessful) {

                    var itemList: ItemsResponse? = response.body()

                    page = itemList?.quotes?.currentPage!!

                    binding.mRecycler.apply {

                        layoutManager =  StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL)
                        recyclerViewAdapter =  DogAdapter(this@QuotesActivity,id,name)
                        adapter = recyclerViewAdapter
                        binding.animationView.visibility = View.GONE

                    }

                    lifecycleScope.launchWhenCreated{


                        mainViewModel?.getListData(id)?.collectLatest {

                            recyclerViewAdapter.submitData(it)

                        }

                    }

                }

            }

            override fun onFailure(call: Call<ItemsResponse>, t: Throwable) {

            }
        })
    }

}

