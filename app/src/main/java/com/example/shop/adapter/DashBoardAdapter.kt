package com.example.shop.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.shop.ProductDetailActivity
import com.example.shop.R
import com.example.shop.firebase.FirestoreClass
import com.example.shop.fragment.DashboardFragment
import com.example.shop.model.Product
import com.example.shop.utils.Constant
import com.squareup.picasso.Picasso

class DashBoardAdapter(val context: Context, var list:ArrayList<Product>,val fragment: DashboardFragment):RecyclerView.Adapter<RecyclerView.ViewHolder>(){
    private var mProduct:Product?=null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_dashboard_layout,parent,false))
    }

    override fun getItemCount(): Int {
         return list.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val model = list[position]
        if (holder is ViewHolder){
            holder.titleName.text = model.title
            holder.dollar.text = "$"
            holder.price.text = model.price
            holder.imgFavorite.setOnClickListener{
                if (holder.isToggled){
                    holder.imgFavorite.setColorFilter(ContextCompat.getColor(context,R.color.colorBorder))
                }
                else{
                    holder.imgFavorite.setColorFilter(ContextCompat.getColor(context,R.color.blackpink))
                }
                holder.isToggled = !holder.isToggled
                if (holder.isToggled==true){
                    mProduct = model
                    fragment.addProductFavorite(mProduct!!.product_id,mProduct!!)

                }

            }

            Picasso.get()
                .load(model.image)
                .fit()
                .into(holder.img)
            holder.itemView.setOnClickListener {
                val intent = Intent(context,ProductDetailActivity::class.java)
                intent.putExtra(Constant.PRODUCT_ID,model.product_id)
                intent.putExtra(Constant.EXTRA_PRODUCT_OWNER_ID,model.user_id)
                context.startActivity(intent)
            }


        }
    }
    class ViewHolder(view: View):RecyclerView.ViewHolder(view){
        var isToggled = false // Biến trạng thái cho ImageButton
        val img :ImageView = view.findViewById(R.id.iv_dashboard_item_image)
        val titleName :TextView = view.findViewById(R.id.tv_dashboard_item_title)
        val price :TextView = view.findViewById(R.id.tv_dashboard_item_price)
        val dollar :TextView = view.findViewById(R.id.dollar)
        val imgFavorite :ImageButton = view.findViewById(R.id.img_favorite)
    }


}