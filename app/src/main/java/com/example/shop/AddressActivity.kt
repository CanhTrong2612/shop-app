package com.example.shop

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.shop.adapter.ListAddressAdapter
import com.example.shop.databinding.ActivityAddAddressBinding
import com.example.shop.databinding.ActivityAddressBinding
import com.example.shop.firebase.FirestoreClass
import com.example.shop.model.Address
import com.example.shop.utils.SwipeToDeleteCallback
import com.example.shop.utils.SwipeToEditCallback

class AddressActivity : AppCompatActivity() {
    private var binding: ActivityAddressBinding? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddressBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        setupActionBar()
        getListAddress()
        binding?.tvAddAddress?.setOnClickListener {
            startActivity(Intent(this, AddAddressActivity::class.java))
        }
    }

    fun setupActionBar() {
        setSupportActionBar(binding?.toolbarAddressListActivity)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.title = ""
        supportActionBar!!.setHomeAsUpIndicator(R.drawable.back_white_24)
        binding?.toolbarAddressListActivity?.setNavigationOnClickListener { onBackPressed() }
    }

    fun getListAddressSuccess(list: ArrayList<Address>) {
        if (list.size > 0) {
            binding?.rvAddressList?.visibility = View.VISIBLE
            binding?.tvNoAddressFound?.visibility = View.GONE
            binding?.rvAddressList?.layoutManager = LinearLayoutManager(this)
            binding?.rvAddressList?.setHasFixedSize(true)
            val adapter = ListAddressAdapter(this, list)
            binding?.rvAddressList?.adapter = adapter
            val editAddress = object : SwipeToEditCallback(this) {
                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                    val adapter = binding?.rvAddressList?.adapter as ListAddressAdapter
                    adapter?.notifyEditItem(this@AddressActivity, viewHolder.adapterPosition)
                }
            }
            val editItemTouchHelper = ItemTouchHelper(editAddress)
            editItemTouchHelper.attachToRecyclerView(binding?.rvAddressList)

            val deleteSwiperHandel = object : SwipeToDeleteCallback(this) {
                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                    FirestoreClass().deleteAddress(
                        this@AddressActivity,
                        list[viewHolder.adapterPosition].id
                    )
                }
            }
            val deleteItemTouchHelper = ItemTouchHelper(deleteSwiperHandel)
            deleteItemTouchHelper.attachToRecyclerView(binding?.rvAddressList)
        } else {
            binding?.rvAddressList?.visibility = View.GONE
            binding?.tvNoAddressFound?.visibility = View.VISIBLE
        }
    }

        fun getListAddress() {
            FirestoreClass().getListAddress(this)
        }
    }

