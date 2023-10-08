package com.example.shop

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.shop.adapter.CartListAdapter
import com.example.shop.adapter.CheckoutAdapter
import com.example.shop.databinding.ActivityCkeckoutBinding
import com.example.shop.firebase.FirestoreClass
import com.example.shop.model.Address
import com.example.shop.model.Cart
import com.example.shop.model.Order
import com.example.shop.utils.Constant

class CkeckoutActivity : BaseActivity() {
    private var binding: ActivityCkeckoutBinding? = null
    private lateinit var mCartItemList :ArrayList<Cart>
    private var mAddressDetails: Address?= null
    private var mSubTotal = 0.0
    private var mtotal = 0.0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCkeckoutBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        setupActionBar()
        getCartList()
        if (intent.hasExtra(Constant.EXTRA_ADDRESS_DETAILS)){
            mAddressDetails = intent.getParcelableExtra(Constant.EXTRA_ADDRESS_DETAILS)
        }
        if (mAddressDetails!=null){
            binding?.tvCheckoutAddressType?.text = mAddressDetails?.type
            binding?.tvCheckoutFullName?.text = mAddressDetails?.name
            binding?.tvCheckoutAddress?.text= mAddressDetails?.address
            binding?.tvCheckoutMobileNumber?.text = mAddressDetails?.mobileNumber
        }
        binding?.btnPlaceOrder?.setOnClickListener {
           placeAnOrder()
        }

    }
    fun setupActionBar(){
        setSupportActionBar(binding?.toolbarCheckoutActivity)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.title = ""
        supportActionBar!!.setHomeAsUpIndicator(R.drawable.back_white_24)
        binding?.toolbarCheckoutActivity?.setNavigationOnClickListener { onBackPressed() }
    }
    fun getCartList(){
        FirestoreClass().getCartistToCheckout(this)
    }

    fun successCartItemList(cartList:ArrayList<Cart>){
        mCartItemList= cartList
        if (mCartItemList.size>0){
            binding?.rvCartListItems?.visibility = View.VISIBLE
            binding?.rvCartListItems?.layoutManager = LinearLayoutManager(this)
            binding?.rvCartListItems?.setHasFixedSize(true)
            val adapter = CheckoutAdapter(this,mCartItemList)
            binding?.rvCartListItems?.adapter = adapter
            for (item in mCartItemList){
                val price =item.price.toDouble()
                val quantity = item.cart_quantity.toInt()
                mSubTotal += (price*quantity)
            }
            mtotal = mSubTotal+10
            binding?.tvCheckoutSubTotal?.text = mSubTotal.toString()
            binding?.tvCheckoutTotalAmount?.text = mtotal.toString()

        }
    }
    fun placeAnOrder(){
        showProgressDialog()
        if (mAddressDetails!=null) {
            val order = Order(
                FirestoreClass().getCurrentID(),
                mCartItemList,
                mAddressDetails!!,
                "My Order ${System.currentTimeMillis()}",
                mCartItemList[0].image,
                mSubTotal.toString(),
                "10.0",
                mtotal.toString(),
            )
            FirestoreClass().placeAnorder(this,order)
        }

    }

    fun orderPlaceSuccess(){
        hideProgressDialog()
        Toast.makeText(this,"Your order placed successfully", Toast.LENGTH_SHORT).show()
        startActivity(Intent(this,MainActivity::class.java))
        finish()
    }


}