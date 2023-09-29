package com.example.shop

import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Toast
import com.example.shop.databinding.ActivityLoginBinding
import com.example.shop.firebase.FirestoreClass
import com.example.shop.model.Users
import com.example.shop.utils.Constant
import com.firebase.ui.auth.data.model.User
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : BaseActivity(),View.OnClickListener{
    private var binding : ActivityLoginBinding?= null
    private var auth = FirebaseAuth.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        binding?.tvForgotpassword?.setOnClickListener(this)
        binding?.tvregisterLogin?.setOnClickListener{
            startActivity(Intent(this, RegisterActivity::class.java))
        }
        binding?.btlogin?.setOnClickListener { login() }

    }
    private fun validateForm():Boolean{
        return when{
            TextUtils.isEmpty(binding?.etmailLogin?.text?.toString())->{
                showErrorSnackBar("Pleas enter email", true)
                false
            }
            TextUtils.isEmpty(binding?.etPasswordLogin?.text?.toString())->{
                showErrorSnackBar("Pleas enter password", true)
                false
            }

            else -> {true}
        }
    }
    private fun login(){
        if (validateForm()){
            showProgressDialog()
            val email = binding!!.etmailLogin!!.text!!.toString()!!.trim()
            val password = binding!!.etPasswordLogin!!.text!!.toString()!!.trim()
            auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    FirestoreClass().getUserDetail(this)

                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "signInWithEmail:failure", task.exception)
                    Toast.makeText(
                        baseContext,
                        "Authentication failed.",
                        Toast.LENGTH_SHORT,
                    ).show()

                }

                }
            }
    }
    fun userLoggedInSuccess(user: Users) {
        hideProgressDialog()
      //  if (user.profileComplete == 0) {
            val intent = Intent(this,ProfileActivity::class.java)
            intent.putExtra(Constant.EXTRA_USER_DETAILS,user)
            startActivity(intent)
      //  }
//        else {
//            startActivity(Intent(this, MainActivity::class.java))
//        }
    }

    override fun onClick(view: View?) {
        when(view!!.id){
            R.id.tv_forgotpassword ->
                startActivity(Intent(this,ForgotPasswordActivity::class.java))
            }
        }
    }

