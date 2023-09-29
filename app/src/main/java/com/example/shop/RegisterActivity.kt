package com.example.shop

import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Toast
import com.example.shop.databinding.ActivityMainBinding
import com.example.shop.databinding.ActivityRegisterBinding
import com.example.shop.firebase.FirestoreClass
import com.example.shop.model.Users
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class RegisterActivity :BaseActivity() {
    private var binding:ActivityRegisterBinding?= null
    private var auth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        actionBar()

        binding?.btregister?.setOnClickListener{
            registerUser()
        }
        binding?.tvloginRegister?.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java ))
        }
    }
    private fun actionBar(){
        setSupportActionBar(binding?.toolbarregister)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title ="CREATE AN ACCOUNT"
        supportActionBar?.setHomeAsUpIndicator(R.drawable.back_24)
        binding?.toolbarregister?.setNavigationOnClickListener { onBackPressed() }
    }
     fun validateForm(): Boolean {
          return when {

            TextUtils.isEmpty(binding?.etfirstname?.text?.toString()) -> {
                showErrorSnackBar("Please enter First Name.", true)
                false
            }
            TextUtils.isEmpty(binding?.etlastname?.text.toString()) -> {
                showErrorSnackBar("Please enter Last Name.", true)
                false
            }
            TextUtils.isEmpty(binding?.etmailRegister?.text.toString()) -> {
                showErrorSnackBar("Please enter Email.", true)
                false

            }
            TextUtils.isEmpty(binding?.etpasswordRegister?.text.toString()) -> {
                showErrorSnackBar("Please enter Password.", true)
                false
            }
            TextUtils.isEmpty(binding?.etcofirmpasswordRegister?.text.toString()) -> {
                showErrorSnackBar("Please enter Confirm Password.", true)
                false
            }
            binding?.checkbox?.isChecked == false -> {
                showErrorSnackBar("Please agree to the terms and condition", true)
                false

            }
              else -> {true}
          }

     }


     fun registerUser(){
         val email = binding?.etmailRegister!!.text.toString().trim()
         val password = binding?.etpasswordRegister!!.text.toString().trim()
       if (validateForm()){
           auth.createUserWithEmailAndPassword(email, password)
               .addOnCompleteListener(this) { task ->
                   if (task.isSuccessful) {
                       val firebaseUser: FirebaseUser = task.result!!.user!!
                       val user = Users(firebaseUser.uid,
                           binding?.etfirstname?.text.toString(),
                           binding?.etlastname?.text.toString(),
                           binding?.etmailRegister?.text.toString(),
                       )
                       FirestoreClass().registerUser(this,user)

                   } else {
                       // If sign in fails, display a message to the user
                       Toast.makeText(
                           baseContext,
                           "Authentication failed.",
                           Toast.LENGTH_SHORT,
                       ).show()
                   }
               }
       }
    }
    fun userRegisterSuccess(){
        Toast.makeText(this, "You are registered successfully", Toast.LENGTH_SHORT).show()
        startActivity(Intent(this, LoginActivity::class.java ))
    }
}