package com.example.quotes.activityclass

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.viewpager.widget.ViewPager
import com.example.quotes.R
import com.example.quotes.adapter.ViewPagerAdapter
import com.example.quotes.databinding.ActivityMainBinding
import com.example.quotes.models.OnBoardingData
import com.google.android.material.tabs.TabLayout

@Suppress("DEPRECATION")
class MainActivity : AppCompatActivity() {

    var binding: ActivityMainBinding? = null
    var ViewPagerAdapter: ViewPagerAdapter? = null
    var onBoardingPager: ViewPager? = null
    var position = 0
    var dialog : Dialog? = null
    var pref: SharedPreferences? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (restorePrefData()){
            val i = Intent(applicationContext, ItemsActivity::class.java)
            startActivity(i)
            finish()
        }
        binding = DataBindingUtil.setContentView(this,R.layout.activity_main)

        dialog = Dialog(this)

       startApp()

    }

    private fun startApp(){

        val onBoardingData:MutableList<OnBoardingData> = ArrayList()

        onBoardingData.add(OnBoardingData("Quotes To\n" + "Brighten Your day","The goal is to help you find the inspiration and motivation you need for living a good and simple life.",R.drawable.frame1))
        onBoardingData.add(OnBoardingData("Uplifting Daily\n" + "Quotes","Quotes that inspire you and motivate you for your life and will totally brighten up and make your day wonderful.",R.drawable.frame2))
        onBoardingData.add(OnBoardingData("Trending Quotes & Categories","We help you get all Trending Quotes and they are also categorized. You can also checkout 50+ Categories which contain thousands of quotes.",R.drawable.frame3))

        setOnViewPagerAdapter(onBoardingData)

        position = onBoardingPager!!.currentItem

        binding?.bText?.setOnClickListener {
            if (position < onBoardingData.size){
                position++
                onBoardingPager!!.currentItem = position
            }
            if (position == onBoardingData.size){
                savePrefData()
                val i = Intent(applicationContext, ItemsActivity::class.java)
                startActivity(i)
            }
        }

        binding?.mTab!!.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                position = tab!!.position
                if (tab.position == onBoardingData.size - 1){
                    binding?.bText!!.text = "Start"
                }else{
                    binding?.bText!!.text = "Next"
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
            }

        })

    }

    private fun setOnViewPagerAdapter(onBoardingData: List<OnBoardingData>){

        onBoardingPager = findViewById(R.id.mViewPager);
        ViewPagerAdapter = ViewPagerAdapter(this,onBoardingData)
        onBoardingPager!!.adapter = ViewPagerAdapter
        binding?.mTab?.setupWithViewPager(onBoardingPager)

    }

    private fun savePrefData(){
        pref = applicationContext.getSharedPreferences("pref", Context.MODE_PRIVATE)
        val editor = pref!!.edit()
        editor.putBoolean("isFirstTimeRun", true)
        editor.apply()
    }

    private fun restorePrefData():Boolean{
        pref = applicationContext.getSharedPreferences("pref", Context.MODE_PRIVATE)
        return pref!!.getBoolean("isFirstTimeRun", false)
    }

}