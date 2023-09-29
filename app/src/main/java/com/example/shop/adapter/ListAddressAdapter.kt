package com.example.shop.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.shop.R
import com.example.shop.model.Address

class ListAddressAdapter(val context: Context, val list: ArrayList<Address>):RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_layout_address,parent,false))
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val model = list[position]
        if (holder is ViewHolder){
            holder.tvName.text= model.name
            holder.tvDetail.text = model.additionalNote
            holder.tvPhone.text = model.mobileNumber
            holder.tvType.text = model.type
        }
    }
    class ViewHolder(view: View):RecyclerView.ViewHolder(view){
        val tvName : TextView = view.findViewById(R.id.tv_address_full_name)
        val tvPhone : TextView =view.findViewById(R.id.tv_address_mobile_number)
        val tvDetail : TextView = view.findViewById(R.id.tv_address_details)
        val tvType : TextView = view.findViewById(R.id.tv_address_type)
    }
}