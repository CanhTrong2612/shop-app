package com.example.shop

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.bumptech.glide.Glide
import com.example.shop.databinding.ActivityProductDetailBinding
import com.example.shop.firebase.FirestoreClass
import com.example.shop.model.Cart
import com.example.shop.model.Product
import com.example.shop.utils.Constant

class ProductDetailActivity : AppCompatActivity() {
    private var binding:ActivityProductDetailBinding?= null
    private var mProductId :String?= null
    private var productOwnerId :String ? = null
    private lateinit var mProductDetail :Product
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProductDetailBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        actionBar()
        getData()
        getProductDetail()
        if (FirestoreClass().getCurrentID() == productOwnerId){
            binding?.btGoToCart?.visibility = View.VISIBLE
            binding?.btAddToCart?.visibility = View.GONE
        }
        binding?.btAddToCart?.setOnClickListener {
            addToCart()
        }
        binding?.btGoToCart?.setOnClickListener {
            addToCart()
        }
    }
    private fun actionBar(){
        setSupportActionBar(binding?.toolbarProductDetailsActivity)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setHomeAsUpIndicator(R.drawable.back_white_24)
        supportActionBar!!.title=""
        binding?.toolbarProductDetailsActivity?.setNavigationOnClickListener { onBackPressed() }
    }
    private fun getData(){
        if (intent.hasExtra(Constant.PRODUCT_ID)){
             mProductId = intent.getStringExtra(Constant.PRODUCT_ID)
        }
        if (intent.hasExtra(Constant.EXTRA_PRODUCT_OWNER_ID)){
            productOwnerId = intent.getStringExtra(Constant.EXTRA_PRODUCT_OWNER_ID)
        }
    }
    fun productDetailsSuccess(product: Product){
        mProductDetail=product
        Glide
            .with(this)
            .load(product.image)
            .centerCrop()
            .into(binding?.ivProductDetailImage)

        binding?.tvProductDetailsTitle?.text = product.title
        binding?.tvProductDetailsPrice?.text = "$${product.price}"
        binding?.tvProductDetailsDescription?.text = product.description
        binding?.tvProductDetailsAvailableQuantity?.text = product.quantity

    }
    fun getProductDetail(){
        FirestoreClass().getProductDetail(this,mProductId!!)
    }
    fun addToCart(){
        val cart = Cart(
            FirestoreClass().getCurrentID(),
            mProductId!!,
            mProductDetail?.title!!,
            mProductDetail?.price!!,
            mProductDetail?.image!!,
        )
        if (cart!=null){
            FirestoreClass().addToCart(this,cart)
        }
    }
    fun addToCartSuccess(){
        startActivity(Intent(this,CartActivity::class.java))
    }
}