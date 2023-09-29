package com.example.shop

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.text.TextUtils
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.example.shop.databinding.ActivityProfileBinding
import com.example.shop.firebase.FirestoreClass
import com.example.shop.model.Users
import com.example.shop.utils.Constant
import com.example.shop.utils.Constant.PICK_IMAGE_REQUEST_CODE

class ProfileActivity : BaseActivity() {
    private var binding:ActivityProfileBinding?= null
    private var mUser: Users?= null
    private var mSelectImageUri : Uri?= null
    private lateinit var mImageUri :String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        actionBar()
        if (intent.hasExtra(Constant.EXTRA_USER_DETAILS)){
             mUser = intent.getParcelableExtra(Constant.EXTRA_USER_DETAILS)!!
        }
        getDataUer(mUser!!)
       // FirestoreClass().getUserDetail(this)
        binding?.profileImage?.setOnClickListener {
            getImage()
        }
        binding?.btSaveProfile?.setOnClickListener {
            if (validateUserProfile()) {
                showProgressDialog()
                if (mSelectImageUri != null) {
                    FirestoreClass().uploadImageToCloudStorge(this, mSelectImageUri)
                } else {
                    updateProfile()
                }
            }

        }

    }
    fun validateUserProfile():Boolean{
        return when{
            TextUtils.isEmpty(binding?.etMobieProfile?.text)->{
                showErrorSnackBar("Please enter mobie number",false)
                false
            }

            else -> {
                true
            }
        }
    }
    private fun actionBar(){
        setSupportActionBar(binding?.toolbarProfile)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setHomeAsUpIndicator(R.drawable.back_white_24)
        supportActionBar!!.title="Profile"
        binding?.toolbarProfile?.setNavigationOnClickListener { onBackPressed() }
    }
    fun getDataUer(user:Users){
        if (user.profileComplete==0){
            binding?.etFirstnameProfile?.isEnabled = false
            binding?.etFirstnameProfile?.setText(user.firstName)

            binding?.etLastnameProfile?.isEnabled = false
            binding?.etLastnameProfile?.setText(user.lastName)

            binding?.etmailProfile?.isEnabled = false
            binding?.etmailProfile?.setText(user.email)
        }
        else{
            binding?.etFirstnameProfile?.isEnabled = false
            binding?.etFirstnameProfile?.setText(user.firstName)

            binding?.etLastnameProfile?.isEnabled = false
            binding?.etLastnameProfile?.setText(user.lastName)

            binding?.etmailProfile?.isEnabled = false
            binding?.etmailProfile?.setText(user.email)

            binding?.etMobieProfile?.isEnabled = false
            binding?.etMobieProfile?.setText(user.mobie.toString())
            Glide.
            with(this)
                .load(user.image)
                .override(200,300)
                .centerCrop()
                .into(binding?.profileImage);

        }
    }
    fun getImage(){
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
            == PackageManager.PERMISSION_GRANTED) {
            showImageChoosen()
        }else{
            ActivityCompat.requestPermissions(this,
                arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                Constant.READ_STORE_PERMISSION_CODE
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (grantResults.isNotEmpty()&& grantResults[0] == PackageManager.PERMISSION_GRANTED){
          showImageChoosen()
        }
        else{
            Toast.makeText(this,
                "You just dennied the permission for storage.You can aslo allow it from setting" ,
                Toast.LENGTH_SHORT).show()
        }
    }
    private fun showImageChoosen(){
        val gallery =Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(gallery, PICK_IMAGE_REQUEST_CODE)
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK &&
            requestCode == PICK_IMAGE_REQUEST_CODE && data!!.data != null
        ) {
            mSelectImageUri = data!!.data
            binding?.profileImage?.setImageURI(Uri.parse(mSelectImageUri!!.toString()))
        }
    }

//    val startForResult = registerForActivityResult(
//        ActivityResultContracts.StartActivityForResult()
//    ) { result: ActivityResult ->
//        if (result.resultCode == Activity.RESULT_OK) {
//            mSelectImageUri = data.data
//            binding?.profileImage?.setImageURI(Uri.parse(mSelectImageUri!!.toString()))
//
//        }
//    }

    fun updateProfile(){
        val hashMap = HashMap<String,Any>()
        hashMap[Constant.COMPLETE_PROFILE]=1
        val mobieNumber = binding?.etMobieProfile?.text.toString()
        if (mobieNumber!!.isNotEmpty()){
            hashMap[Constant.MOBIE] = mobieNumber!!.toLong()
        }
        val gender = if (binding?.rbMale?.isChecked==true){
            Constant.MALE
        }
        else{
            Constant.FERMALE
        }
        hashMap[Constant.GENDER]= gender
        hashMap[Constant.IMAGE]= mImageUri
        binding?.btSaveProfile?.text = "Submit"
        FirestoreClass().updateUser(this,hashMap)
    }

    fun updateUserSuccess() {
        startActivity(Intent(this,MainActivity::class.java))
    }

    fun imageUploadSuccess(image: String) {
        mImageUri = image
        updateProfile()
        hideProgressDialog()

    }
}