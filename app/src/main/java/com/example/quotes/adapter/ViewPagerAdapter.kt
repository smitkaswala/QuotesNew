package com.example.quotes.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.viewpager.widget.PagerAdapter
import com.example.quotes.R
import com.example.quotes.models.OnBoardingData

class ViewPagerAdapter(private var context: Context, private var onDataList: List<OnBoardingData>) : PagerAdapter() {

    override fun getCount(): Int {
        return onDataList.size
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view == `object`
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }

    @SuppressLint("InflateParams")
    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val view = LayoutInflater.from(context).inflate(R.layout.slider_view, null)

        val imageView: ImageView = view.findViewById(R.id.mImage)
        val title: TextView = view.findViewById(R.id.mText)
        val desc: TextView = view.findViewById(R.id.sText)
        imageView.setImageResource(onDataList[position].imageUrl)
        title.text = onDataList[position].title
        desc.text = onDataList[position].desc

        container.addView(view)
        return view

    }

}

