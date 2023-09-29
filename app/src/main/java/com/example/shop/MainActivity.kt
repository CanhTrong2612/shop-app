package com.example.shop

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.shop.databinding.ActivityMainBinding
import com.example.shop.databinding.FragmentProductBinding
import com.example.shop.fragment.DashboardFragment
import com.example.shop.fragment.FavoriteFragment
import com.example.shop.fragment.OrderFragment
import com.example.shop.fragment.ProductFragment

class MainActivity : AppCompatActivity() {
    private var binding: ActivityMainBinding? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        replaceFragment(DashboardFragment())
        binding?.bottomNavigationView?.setOnItemSelectedListener {
            when(it.itemId){
                R.id.navigation_dashboard->{
                    replaceFragment(DashboardFragment())
                     true
                }
                R.id.navigation_products->{
                    replaceFragment(ProductFragment())
                     true
                }

                R.id.navigation_notifications->{
                    replaceFragment(OrderFragment())
                     true
                }

                else -> {false}
            }
    }

    }
    fun replaceFragment(fragment:Fragment){
        val fragmentManager =supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frame_layout,fragment)
        fragmentTransaction.commit()
    }
}