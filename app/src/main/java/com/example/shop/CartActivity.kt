package com.example.shop

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.shop.adapter.CartListAdapter
import com.example.shop.databinding.ActivityCartBinding
import com.example.shop.firebase.FirestoreClass
import com.example.shop.model.Cart
import com.example.shop.utils.Constant

class CartActivity : AppCompatActivity() {
    private var binding:ActivityCartBinding?= null
    private lateinit var mCartList :ArrayList<Cart>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCartBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        actionBar()
        getCartListSuccess()
        binding?.btnCheckout?.setOnClickListener {
            startActivity(Intent(this,AddressActivity::class.java))
        }

    }
    private fun actionBar(){
        setSupportActionBar(binding?.toolbarCartListActivity)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setHomeAsUpIndicator(R.drawable.back_white_24)
        supportActionBar!!.title=""
        binding?.toolbarCartListActivity?.setNavigationOnClickListener { onBackPressed() }
    }
    fun add(cart: Cart){
        FirestoreClass().addToCart2(this,cart)
    }

    fun tinhTong(list: ArrayList<Cart>){
        var subTotal = 0.0
        var total = 0.0
        for ( item in list){
            val price =item.price.toDouble()
            val quantity = item.cart_quantity.toInt()
            subTotal += (price*quantity)
            Log.e("loi lol",subTotal.toString())
        }
        total +=subTotal+10
        binding?.tvTotalAmount?.text = "$$total"
        binding?.tvSubTotal?.text = "$${subTotal}"
        binding?.tvShippingCharge?.text= "$10"
    }
    fun getCartList(list:ArrayList<Cart>){
        if (list.size!=0) {
            mCartList = list
            binding?.rvCartItemsList?.visibility= View.VISIBLE
            binding?.tvNoCartItemFound?.visibility = View.GONE
            binding?.llCheckout?.visibility =View.VISIBLE
            binding?.rvCartItemsList?.layoutManager = LinearLayoutManager(this)
            binding?.rvCartItemsList?.setHasFixedSize(true)
            val adapter = CartListAdapter(this, list,true)
            binding?.rvCartItemsList?.adapter = adapter
            tinhTong(list)



        }
        else{
            binding?.rvCartItemsList?.visibility= View.GONE
            binding?.tvNoCartItemFound?.visibility = View.VISIBLE
            binding?.llCheckout?.visibility =View.GONE
        }
    }
    fun getCartListSuccess(){
        FirestoreClass().getCartist(this)

    }
    fun deleteCart(cartId:String){
        FirestoreClass().deleteCart(this, cartId)
    }
    fun addToCartSuccess(){

    }

    }
