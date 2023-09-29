package com.example.shop

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.shop.firebase.FirestoreClass

class SpalshActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_spalsh)
        android.os.Handler().postDelayed({
            val  currentUserId = FirestoreClass().getCurrentID()
            if (currentUserId.isNotEmpty()){
                startActivity(Intent(this, MainActivity::class.java))
            }
            else{
                startActivity(Intent(this, LoginActivity::class.java))
            }

        },2500)
    }
}