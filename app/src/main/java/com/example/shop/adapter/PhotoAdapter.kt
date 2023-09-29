package com.example.shop.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.viewpager.widget.PagerAdapter
import com.bumptech.glide.Glide
import com.example.shop.R
import com.example.shop.model.Photo
import com.squareup.picasso.Picasso

class PhotoAdapter(val context:Context,val list:ArrayList<Photo>):PagerAdapter() {
    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val view = LayoutInflater.from(container.context).inflate(R.layout.layout_photo,container,false)
        val img :ImageView= view.findViewById(R.id.image_photo)
        val model = list[position]
        if (model!=null){
            Picasso.get()
                .load(model.resourceId)
                .fit()
                .into(img)
        }
        container.addView(view)

        return view
    }

    override fun getCount(): Int {
        return list.size
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view ==`object`
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View?)

    }
}