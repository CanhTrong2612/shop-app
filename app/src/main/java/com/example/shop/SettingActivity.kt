package com.example.shop

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.bumptech.glide.Glide
import com.example.shop.databinding.ActivitySettingBinding
import com.example.shop.firebase.FirestoreClass
import com.example.shop.model.Users
import com.firebase.ui.auth.data.model.User
import com.google.firebase.auth.FirebaseAuth

class SettingActivity : BaseActivity(), View.OnClickListener{
    private var binding:ActivitySettingBinding?= null
    private lateinit var mUser: Users
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        getUser()
        binding?.btnLogout?.setOnClickListener(this)
        binding?.edtEdit?.setOnClickListener(this)
    }
    fun getUser(){
        showProgressDialog()
        FirestoreClass().getUserDetail(this)
    }

    fun getUserSuccess(user: Users) {
        hideProgressDialog()
        binding?.tvName?.text= "${user.firstName} ${user.lastName}"
        binding?.tvGender?.text = user.gender
        binding?.tvEmail?.text = user.email
        binding?.tvMobileNumber?.text ="${user.mobie}"
        Glide
            .with(this)
            .load(user.image)
            .centerCrop()
            .into(binding?.profileImage)
    }

    override fun onClick(view: View?) {
        when(view!!.id){
            R.id.btn_logout->{
                FirebaseAuth.getInstance().signOut()
                startActivity(Intent(this,LoginActivity::class.java))
                finish()
            }
            R.id.edt_edit->{
                startActivity(Intent(this,ProfileActivity::class.java))
            }
        }
    }

}