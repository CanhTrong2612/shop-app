package com.example.shop

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import com.example.shop.databinding.ActivityAddAddressBinding
import com.example.shop.firebase.FirestoreClass
import com.example.shop.model.Address
import com.example.shop.utils.Constant

class AddAddressActivity :BaseActivity() {
    private var binding: ActivityAddAddressBinding? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddAddressBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        setupActionBar()
        binding?.btnSubmitAddress?.setOnClickListener { saveAddress() }
    }

    fun setupActionBar() {
        setSupportActionBar(binding?.toolbarAddEditAddressActivity)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.title = ""
        supportActionBar!!.setHomeAsUpIndicator(R.drawable.back_white_24)
        binding?.toolbarAddEditAddressActivity?.setNavigationOnClickListener { onBackPressed() }
    }

    fun validateForm(): Boolean {
        when {
            TextUtils.isEmpty(binding?.etFullName?.text.toString()) -> {
                showErrorSnackBar("Please enter full name", true)
            }
            TextUtils.isEmpty(binding?.etPhoneNumber?.text.toString()) -> {
                showErrorSnackBar("Please enter phone number", true)
            }
            TextUtils.isEmpty(binding?.etAddress?.text.toString()) -> {
                showErrorSnackBar("Please enter address", true)
            }
            TextUtils.isEmpty(binding?.etAdditionalNote?.text.toString()) -> {
                showErrorSnackBar("Please enter additional note", true)
            }

        }
        return true
    }

    fun saveAddress() {
        val fullName = binding?.etFullName?.text.toString().trim()
        val phoneNumber = binding?.etPhoneNumber?.text.toString().trim()
        val address = binding?.etAddress?.text.toString().trim()
        val additionalNote = binding?.etAdditionalNote?.text.toString().trim()
        val otherDetail = binding?.etOtherDetails?.text.toString().trim()
        if (validateForm()) {
            val addressType: Any = when {
                binding?.rbHome?.isChecked == true -> {
                    Constant.HOME
                }
                binding?.rbOffice?.isChecked == true -> {
                    Constant.OFFICE
                }
                else -> true
            }
            val address = Address(
                FirestoreClass().getCurrentID(),
                fullName,
                phoneNumber,
                address,
                additionalNote,
                addressType as String,
                otherDetail
            )

            FirestoreClass().addAddress(this, address)
        }


    }

    fun saveAddressSuccess() {
        Toast.makeText(this, "Add the address successfully", Toast.LENGTH_SHORT).show()
        startActivity(Intent(this, AddressActivity::class.java))
        setResult(RESULT_OK)
        finish()
    }
}