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
class CartListAdapter(val context: Context,val list:ArrayList<Cart>,private var updateCartItem:Boolean):RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return MyViewHolder(LayoutInflater.from(context).inflate(R.layout.item_cart,parent,false))
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val model = list[position]
        if (holder is MyViewHolder) {
//           holder.checkCartItem.setOnCheckedChangeListener { compoundButton, b ->
//                if (b){
//                    Constant.listmua.add(model)
//
//                    for(i in Constant.listmua){
//                        Log.e("loixxx", println(i).toString())
//                        if (updateCartItem==true){
//                        holder.ibAdd.setOnClickListener {
//                            val cartQuantity: Int = i.cart_quantity.toInt()
//                            i.cart_quantity = (cartQuantity + 1).toString()
//                            holder.tvQuantity.setText(i.cart_quantity)
//                            (context as CartActivity).tinhTong(Constant.listmua)
//                        }
//                        holder.ibRemove.setOnClickListener {
//                            if (i.cart_quantity == "1") {
//                                FirestoreClass().deleteCart(context as CartActivity, model.id)
//                            } else {
//                                val cartQuantity: Int = i.cart_quantity.toInt()
//                                i.cart_quantity = (cartQuantity - 1).toString()
//                                holder.tvQuantity.setText(model.cart_quantity)
//                                (context as CartActivity).tinhTong(Constant.listmua)
//                            }
//                        }
//                            (context as CartActivity).add(model)
//                        }
//                        else{
//                            holder.checkCartItem.visibility = View.GONE
//                            holder.ibDeleteCart.visibility = View.GONE
//                        }
//
//
//                    }
//
//                }
//               else{
//                  for( i in 0.. Constant.listmua.size step 1){
//                      if (Constant.listmua[i].id==model.id){
//                          Constant.listmua.remove(Constant.listmua[i])
//                      }
//                  }
//
//                   }
//                }

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
                holder.tvPrice.text = "${model.price}"
                holder.tvQuantity.text = model.cart_quantity.toString()
                holder.ibDeleteCart.setOnClickListener {
                    if (context is CartActivity) {
                        context.deleteCart(model.id)
                    }
                }
                holder.ibAdd.setOnClickListener {
                    val cartQuantity: Int = model.cart_quantity.toInt()
                    val itemHashMap = HashMap<String, Any>()
                    itemHashMap[Constant.CART_QUANTITY] = (cartQuantity + 1).toString()
                    FirestoreClass().updateCart(context as CartActivity, itemHashMap, model.id)
                }
                holder.ibRemove.setOnClickListener {
                    if (model.cart_quantity == "1") {
                        FirestoreClass().deleteCart(context as CartActivity, model.id)
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