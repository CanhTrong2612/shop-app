package com.example.shop.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.shop.CartActivity
import com.example.shop.R
import com.example.shop.firebase.FirestoreClass
import com.example.shop.model.Cart
import com.example.shop.model.Product
import com.example.shop.utils.Constant
import com.squareup.picasso.Picasso

class CartListAdapter(val context: Context,val list:ArrayList<Cart>):RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return MyViewHolder(LayoutInflater.from(context).inflate(R.layout.item_cart,parent,false))
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val model = list[position]
        if (holder is MyViewHolder){
//            Glide
//                .with(context)
//                .load(model.image)
//                .centerCrop()
//                .into(holder.iv)
            Picasso.get()
                .load(model.image)
                .fit()
                .into(holder.iv)

            holder.tvTitle.text = model.title
            holder.tvPrice.text ="${model.price}"
            holder.tvQuantity.text = model.cart_quantity.toString()
            holder.ibDeleteCart.setOnClickListener {
                if (context is CartActivity){
                    context.deleteCart(model.id)
                }
            }
            holder.ibAdd.setOnClickListener {
                val cartQuantity: Int = model.cart_quantity.toInt()
                val itemHashMap = HashMap<String, Any>()
                itemHashMap[Constant.CART_QUANTITY] = (cartQuantity + 1).toString()
                FirestoreClass().updateCart(context as CartActivity,itemHashMap, model.id)
            }
            holder.ibRemove.setOnClickListener {
                if (model.cart_quantity == "1") {
                    FirestoreClass().deleteCart(context as CartActivity,model.id)
                } else {
                    val cartQuantity: Int = model.cart_quantity.toInt()
                    val itemHashMap = HashMap<String, Any>()
                    itemHashMap[Constant.CART_QUANTITY] = (cartQuantity - 1).toString()
                    FirestoreClass().updateCart(context as CartActivity, itemHashMap, model.id)
                }
            }
        }
    }
    class MyViewHolder(view: View):RecyclerView.ViewHolder(view){
        val iv : ImageView = view.findViewById(R.id.iv_cart_item_image)
        val tvTitle: TextView = view.findViewById(R.id.tv_cart_item_title)
        val tvPrice: TextView = view.findViewById(R.id.tv_cart_item_price)
        val tvQuantity : TextView = view.findViewById(R.id.tv_cart_quantity)
        val ibAdd: ImageButton = view.findViewById(R.id.ib_add_cart_item)
        val ibRemove : ImageButton = view.findViewById(R.id.ib_remove_cart_item)
        val ibDeleteCart : ImageButton = view.findViewById(R.id.ib_delete_cart_item)
    }
}