package com.erickcapilla.dcyourself.adapter

import android.text.method.ScrollingMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.viewpager.widget.PagerAdapter
import com.erickcapilla.dcyourself.model.DataRecommendations
import com.erickcapilla.dcyourself.R

class RecommendationAdapter(private val recommendationList: MutableList<DataRecommendations>, private val imgView: Int): PagerAdapter() {

    override fun getCount(): Int {
        return recommendationList.size
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view == `object`
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val view = LayoutInflater.from(container.context).inflate(R.layout.adapter_viewpager, null)
        val title = view.findViewById<TextView>(R.id.title)
        val description = view.findViewById<TextView>(R.id.body)
        val img = view.findViewById<ImageView>(R.id.img)
        img.setImageResource(imgView)
        title.text = recommendationList[position].title
        description.text = recommendationList[position].description
        description.movementMethod = ScrollingMovementMethod()
        container.addView(view, 0)

        return view
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }
}