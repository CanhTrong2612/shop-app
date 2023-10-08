package com.example.shop

import android.app.DatePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.content.ContextCompat
import com.example.shop.databinding.ActivityOrderDetailBinding
import com.example.shop.model.Order
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

class OrderDetailActivity : AppCompatActivity() {
    private var binding:ActivityOrderDetailBinding?= null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityOrderDetailBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        actionBar()


    }
    fun actionBar(){
        setSupportActionBar(binding?.toolbarMyOrderDetailsActivity)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.title=""
        supportActionBar!!.setHomeAsUpIndicator(R.drawable.back_white_24)
        binding?.toolbarMyOrderDetailsActivity?.setNavigationOnClickListener { onBackPressed() }
    }
    private fun setupUI(orderDetails: Order) {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        val dateFormat = "dd MMM yyyy HH:mm"
        val formatter = SimpleDateFormat(dateFormat, Locale.US)
        calendar.timeInMillis = orderDetails.order_datetime
        val orderDateTime = formatter.format(calendar.time)
        binding?.tvOrderDetailsDate?.text = orderDateTime.toString()
        val diffInMilliSeconds: Long = System.currentTimeMillis() - orderDetails.order_datetime
        val diffInHours: Long = TimeUnit.MILLISECONDS.toHours(diffInMilliSeconds)
        if (diffInHours<1){
            binding?.tvOrderStatus?.text = resources.getString(R.string.order_status_pending)
            binding?.tvOrderStatus?.setTextColor(
                ContextCompat.getColor(
                    this,
                    R.color.pink
                )
            )
        }
        if (diffInHours<2){
                binding?.tvOrderStatus?.text  = resources.getString(R.string.order_status_in_process)
                binding?.tvOrderStatus?.setTextColor(
                    ContextCompat.getColor(
                        this,
                        R.color.pink
                    )
                )
            }
        else{
            binding?.tvOrderStatus?.text  = resources.getString(R.string.order_status_delivered)
            binding?.tvOrderStatus?.setTextColor(
                ContextCompat.getColor(
                    this,
                    R.color.pink
                )
            )
        }
        }
    }
