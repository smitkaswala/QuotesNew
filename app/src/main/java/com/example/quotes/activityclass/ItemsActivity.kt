package com.example.quotes.activityclass

import android.app.Dialog
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.SearchView
import android.widget.Toast
import androidx.core.view.GravityCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.quotes.BuildConfig
import com.example.quotes.R
import com.example.quotes.adapter.ItemsAdapter
import com.example.quotes.databinding.ActivityItemsBinding
import com.example.quotes.models.DataItemList
import com.example.quotes.utils.NetworkConnection
import java.util.*
import kotlin.collections.ArrayList

class ItemsActivity : AppCompatActivity() {

    var dialog : Dialog? = null
    var binding : ActivityItemsBinding? = null
    private var layoutManager: RecyclerView.LayoutManager? = null
    private lateinit var adapterItems : ItemsAdapter
    private lateinit var listQuotes : ArrayList<DataItemList>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_items)

        layoutManager = LinearLayoutManager(this)

        dialog = Dialog(this)

//        val networkConnection = NetworkConnection(applicationContext)

//        networkConnection.observe(this, androidx.lifecycle.Observer { isConnected ->
//            if (isConnected){
//
//                dialog?.dismiss()
//
//                startApp()
//
//            }else{
//
//                dialog?.setContentView(R.layout.internet_connection)
//
//                dialog?.setCanceledOnTouchOutside(false)
//
//                dialog?.window?.setLayout(
//                    WindowManager.LayoutParams.MATCH_PARENT,
//                    WindowManager.LayoutParams.MATCH_PARENT)
//
//                dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
//
//                dialog?.window?.attributes?.windowAnimations = android.R.style.Animation_Dialog
//
//                val button : Button? = dialog?.findViewById(R.id.mButton)
//
//                button?.setOnClickListener {
//
//                    if (isConnected){
//
//                        startApp()
//                    }
//
//                }
//
//                dialog?.show()
//
//            }
//        })

        checkConnection()


    }

    private fun checkConnection(){

        val connectivityManager : ConnectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val wifi : NetworkInfo? = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI)
        val mobile : NetworkInfo? = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)

        if (wifi!!.isConnected){

            dialog?.dismiss()
            startApp()

        }else if (mobile!!.isConnected){

            dialog?.dismiss()
            startApp()

        }else{

            dialog?.setContentView(R.layout.internet_connection)

            dialog?.setCanceledOnTouchOutside(false)

            dialog?.window?.setLayout(WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT)

            dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

            dialog?.window?.attributes?.windowAnimations = android.R.style.Animation_Dialog

            val button : Button? = dialog?.findViewById(R.id.mButton)

            button?.setOnClickListener {

                checkConnection()

            }

            dialog?.show()

        }

    }

    private fun startApp(){

        binding?.includeToolbar?.mRecycler?.layoutManager = layoutManager

        binding?.drawerLayout?.setScrimColor(Color.TRANSPARENT)

        binding?.includeToolbar?.mMenu?.setOnClickListener {

            binding?.drawerLayout?.openDrawer(GravityCompat.START)

        }

        binding?.includeToolbar?.mSearch?.setOnSearchClickListener {
            binding?.includeToolbar?.mName?.visibility = View.GONE
        }
        binding?.includeToolbar?.mSearch?.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
            androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                TODO("Not yet implemented")
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                searchQuotes(newText.toString())
                return false
            }
        })
        binding?.includeToolbar?.mSearch?.setOnCloseListener(object : SearchView.OnCloseListener,
            androidx.appcompat.widget.SearchView.OnCloseListener{
            override fun onClose(): Boolean {
                binding?.includeToolbar?.mName?.visibility = View.VISIBLE
                return false

            }

        })

        binding?.mCard1?.setOnClickListener {

            val intent = Intent(this, FavoriteActivity::class.java)
            startActivity(intent)
            binding?.drawerLayout?.close()

        }

        binding?.mCard2?.setOnClickListener {

            val intent= Intent()
            intent.action= Intent.ACTION_SEND
            intent.type="text/plain"
            intent.putExtra(Intent.EXTRA_SUBJECT,"Hey Check out this Quotes app")
            var shareMessage = "\nHey Check out this Quotes app\n\n"
            shareMessage = """ ${shareMessage}https://play.google.com/store/apps/details?id=${BuildConfig.APPLICATION_ID} """.trimIndent()
            intent.putExtra(Intent.EXTRA_TEXT, shareMessage)
            startActivity(Intent.createChooser(intent,"Share View"))
            binding?.drawerLayout?.close()

        }

        binding?.mCard3?.setOnClickListener {

            val uri = Uri.parse("market://details?id=$packageName")
            val myAppLinkToMarket = Intent(Intent.ACTION_VIEW, uri)
            try {
                startActivity(myAppLinkToMarket)
            } catch (e: ActivityNotFoundException) {
                Toast.makeText(this, "Impossible to find an application for the market", Toast.LENGTH_LONG).show()
            }
            binding?.drawerLayout?.close()

        }


        mainImage()

    }
    private fun mainImage(){

        val imageId = intArrayOf(
            R.drawable.motivation,R.drawable.success,R.drawable.relationship,R.drawable.life,R.drawable.depression,R.drawable.athletes,R.drawable.students,
            R.drawable.teachers,R.drawable.moms,R.drawable.fitness,R.drawable.mens,R.drawable.morning,R.drawable.positive,R.drawable.wisdom,
            R.drawable.inspirational,R.drawable.happiness,R.drawable.attitude,R.drawable.friendship,R.drawable.love,R.drawable.smile
        )

        val quotesId = intArrayOf(
            71,72,73,74,75,76,77,78,79,80,81,82,83,84,85,86,87,88,89,90
        )

         val nameList = arrayOf("Motivation","Success","Relationship","Life","Depression","Athletes","Students","Teachers","Moms","Fitness","Mens"
        ,"Morning","Positive","Wisdom","Inspirational","Happiness","Attitude","Friendship","Love","Smile")


        listQuotes = ArrayList()


        for (i in imageId.indices){

            val list = DataItemList(imageId[i],quotesId[i],nameList[i])
            listQuotes.add(list)

        }

        adapterItems = ItemsAdapter(this,listQuotes)
        binding?.includeToolbar?.mRecycler?.adapter = adapterItems

    }

    private fun searchQuotes(value : String){

        var quotesList = ArrayList<DataItemList>()
        for (quotes  in listQuotes){


            if (quotes.name.lowercase(Locale.getDefault()).contains(value.lowercase(Locale.getDefault()))){

                quotesList.add(quotes)
            }
        }
        adapterItems.updateList(quotesList)

    }

}
