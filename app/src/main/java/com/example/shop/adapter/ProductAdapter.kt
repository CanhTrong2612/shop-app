package com.example.shop.adapter

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.shop.AddAddressActivity
import com.example.shop.AddProductActivity
import com.example.shop.ProductDetailActivity
import com.example.shop.R
import com.example.shop.firebase.FirestoreClass
import com.example.shop.fragment.ProductFragment
import com.example.shop.model.Product
import com.example.shop.utils.Constant
import com.squareup.picasso.Picasso

class ProductAdapter(val context: Context,val list:ArrayList<Product>,val dragment:ProductFragment):RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return MyViewHolder(LayoutInflater.from(context).inflate(R.layout.item_product,parent,false))
    }

    override fun getItemCount(): Int {
        return list.size
    }
    fun notifyEditItem(activity: Activity, position: Int) {
        val intent = Intent(context, AddProductActivity::class.java)
        intent.putExtra(Constant.EXTRA_EDIT_PRODUCT, list[position])
        activity.startActivityForResult(intent, Constant.ADD_ADDRESS_REQUEST_CODE)
        notifyItemChanged(position)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val model = list[position]
        if (holder is MyViewHolder){
            Picasso.get()
                .load(model.image)
                .fit()
                .into(holder.img)
            holder.cartTitle.text= model.title
            holder.cartPrice.text = model.price
            holder.ibDelete.setOnClickListener {
                dragment.deleteProduct(model.product_id)
            }
            holder.ibEdit.setOnClickListener {
                val intent = Intent(context, AddProductActivity::class.java)
                intent.putExtra(Constant.EXTRA_EDIT_PRODUCT, list[position])
                context.startActivity(intent)
                notifyItemChanged(position)
            }
            holder.itemView.setOnClickListener {
                val intent = Intent(context, ProductDetailActivity::class.java)
                intent.putExtra(Constant.PRODUCT_ID,model.product_id)
                intent.putExtra(Constant.EXTRA_PRODUCT_OWNER_ID,model.user_id)
                context.startActivity(intent)
            }
        }
    }
    class MyViewHolder(view: View):RecyclerView.ViewHolder(view){
        val img :ImageView = view.findViewById(R.id.iv_cart_item_image)
        val cartTitle :TextView = view.findViewById(R.id.tv_cart_item_title)
        val cartPrice :TextView = view.findViewById(R.id.tv_cart_item_price)
        val ibDelete :ImageButton = view.findViewById(R.id.ib_delete_cart_item)
        val ibEdit :ImageButton = view.findViewById(R.id.ib_edit_cart_item)
    }


}