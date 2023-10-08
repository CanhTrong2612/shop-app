package com.example.shop.adapter

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.shop.CartActivity
import com.example.shop.R
import com.example.shop.firebase.FirestoreClass
import com.example.shop.model.Cart
import com.example.shop.model.Product
import com.example.shop.utils.Constant
import com.google.android.play.core.integrity.e
import com.google.android.play.integrity.internal.i
import com.squareup.picasso.Picasso
class CheckoutAdapter(val context: Context,val list:ArrayList<Cart>):RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return MyViewHolder(LayoutInflater.from(context).inflate(R.layout.item_checkout,parent,false))
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val model = list[position]
        if (holder is MyViewHolder) {

            Picasso.get()
                .load(model.image)
                .fit()
                .into(holder.iv)

            holder.tvTitle.text = model.title
            holder.tvPrice.text = "${model.price}"
            holder.tvQuantity.text = model.cart_quantity.toString()


        }
    }


    class MyViewHolder(view: View):RecyclerView.ViewHolder(view){
        val iv : ImageView = view.findViewById(R.id.iv_cart_checkout)
        val tvTitle: TextView = view.findViewById(R.id.tv_cart_item_title_checkout)
        val tvPrice: TextView = view.findViewById(R.id.tv_cart_item_price_checkout)
        val tvQuantity : TextView = view.findViewById(R.id.tv_soluong)
    }
}