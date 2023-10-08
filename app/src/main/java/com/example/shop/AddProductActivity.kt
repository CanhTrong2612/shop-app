package com.example.shop

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.shop.databinding.ActivityAddProductBinding
import com.example.shop.firebase.FirestoreClass
import com.example.shop.model.Product
import com.example.shop.utils.Constant
import com.squareup.picasso.Picasso

class AddProductActivity : BaseActivity() {
    private var binding: ActivityAddProductBinding? = null
    private  var mProduct: Product?= null
    private var mProductImage: String? = null
    private var mSelectImageUri: Uri? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddProductBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        actionBar()
        binding?.ivCameraProduct?.setOnClickListener {
            getImage()
        }
        binding?.btSubmitProduct?.setOnClickListener {
            showProgressDialog()

            if (mSelectImageUri!=null){
                FirestoreClass().uploadImageToCloudStorge(this,mSelectImageUri)
            }
            else {
                addProduct()
            }
        }
        if (intent.hasExtra(Constant.EXTRA_EDIT_PRODUCT)){
            mProduct = intent.getParcelableExtra(Constant.EXTRA_EDIT_PRODUCT)!!
        }
        if (mProduct!= null && mProduct!!.product_id.isNotEmpty()){
            binding?.productTitle?.setText(mProduct!!.title)
            binding?.productPrice?.setText(mProduct!!.price)
            binding?.productDescription?.setText(mProduct!!.description)
            binding?.productQuantity?.setText(mProduct!!.quantity)
            Picasso.get()
                .load(mProduct!!.image)
                .fit()
                .into(binding?.ivProduct)
            binding?.tvLableAddProduct?.text = "Edit Product"
        }


    }


    private fun actionBar() {
        setSupportActionBar(binding?.toolbarProduct)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = ""
        supportActionBar?.setHomeAsUpIndicator(R.drawable.baseline_arrow_right_24)
        binding?.toolbarProduct?.setNavigationOnClickListener { onBackPressed() }
    }

//    val getAction = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
//        mSelectImageUri = it?.data!!.data
//        binding?.ivProduct?.setImageURI(Uri.parse(mSelectImageUri!!.toString()))
//
//    }

    fun getImage() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
            == PackageManager.PERMISSION_GRANTED
        ) {
            showImageChoosen()
        } else {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                Constant.READ_STORE_PERMISSION_CODE
            )
        }
    }

    private fun showImageChoosen() {
        val gallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(gallery, Constant.PICK_IMAGE_REQUEST_CODE)
        //getAction.launch(gallery)

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            showImageChoosen()
        } else {
            Toast.makeText(
                this,
                "You just dennied the permission for storage.You can aslo allow it from setting",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    fun addProduct() {
        val username =
            this.getSharedPreferences(Constant.MYSHOPPAL_PREFERENCES, Context.MODE_PRIVATE)
                .getString(Constant.LOGGED_IN_USERNAME, "")!!
        val product = Product(
            FirestoreClass().getCurrentID(),
            username,
            binding?.productTitle?.text?.toString(),
            binding?.productPrice?.text.toString(),
            binding?.productDescription?.text.toString(),
            binding?.productQuantity?.text.toString(),
            mProductImage
        )
        if (mProduct!=null && mProduct!!.product_id.isNotEmpty()){
            FirestoreClass().updateProduct(this,product,mProduct!!.product_id)
        }
        else{
            FirestoreClass().addProduct(this, product)
        }

    }

    fun productUploadSuccess() {
        hideProgressDialog()
        finish()
    }

    fun imageUploadSuccess(imageUrl: String) {
        mProductImage = imageUrl
        addProduct()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK &&
            requestCode == Constant.PICK_IMAGE_REQUEST_CODE && data!!.data != null
        ) {
            mSelectImageUri = data!!.data
            binding?.ivProduct?.setImageURI(Uri.parse(mSelectImageUri!!.toString()))
        }
    }

    fun updateProductSuccess(){
        showErrorSnackBar("update product success",false)
      //  hideProgressDialog()
        finish()
    }


}



