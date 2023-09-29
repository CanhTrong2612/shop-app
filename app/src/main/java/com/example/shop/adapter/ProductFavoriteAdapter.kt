package com.example.shop.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.shop.R
import com.example.shop.fragment.OrderFragment
import com.example.shop.model.Product
import com.squareup.picasso.Picasso

class ProductFavoriteAdapter(val context: Context,val list: ArrayList<Product>,val fragment: OrderFragment) :RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var isToggled = true // Biến trạng thái cho ImageButton
        val img: ImageView = view.findViewById(R.id.iv_product_favorite)
        val titleName: TextView = view.findViewById(R.id.tv_product_favorite_item_title)
        val price: TextView = view.findViewById(R.id.tv_product_favorite_item_price)
        val dollar: TextView = view.findViewById(R.id.dollar_product_favorite)
        val imgFavorite: ImageButton = view.findViewById(R.id.img_favorite_product)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return MyViewHolder(
            LayoutInflater.from(context)
                .inflate(R.layout.item_layout_product_favorite, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val model = list[position]
        if (holder is MyViewHolder) {
            holder.titleName.text = model.title
            holder.dollar.text = "$"
            holder.price.text = model.price
            holder.imgFavorite.setOnClickListener {
                if (holder.isToggled) {
                    holder.imgFavorite.setColorFilter(
                        ContextCompat.getColor(
                            context,
                            R.color.colorBorder
                        )
                    )
                } else {
                    holder.imgFavorite.setColorFilter(
                        ContextCompat.getColor(
                            context,
                            R.color.blackpink
                        )
                    )
                }
                holder.isToggled = !holder.isToggled
            }
            Picasso.get()
                .load(model.image)
                .fit()
                .into(holder.img)
            holder.imgFavorite?.setOnClickListener {
                fragment.deleteProductFavorite(model.product_id,model)
            }
        }
    }
}