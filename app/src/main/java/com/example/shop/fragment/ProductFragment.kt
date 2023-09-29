package com.example.shop.fragment

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.shop.AddProductActivity
import com.example.shop.MainActivity
import com.example.shop.R
import com.example.shop.adapter.ProductAdapter
import com.example.shop.databinding.FragmentProductBinding
import com.example.shop.firebase.FirestoreClass
import com.example.shop.model.Product
import com.example.shop.utils.Constant

class ProductFragment : Fragment(), MenuProvider {
    private var binding: FragmentProductBinding? = null
    private lateinit var mProduct:Product
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProductBinding.inflate(layoutInflater, container, false)
        setFragmentToolbar()
        getListProduct()
        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(
            this,
            viewLifecycleOwner,
            androidx.lifecycle.Lifecycle.State.RESUMED
        )

        return binding?.root
    }
    fun setFragmentToolbar(){
        ( requireActivity() as MainActivity).setSupportActionBar(binding?.toolbarr)
        ( requireActivity() as MainActivity).supportActionBar!!.title =""
    }

    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        menuInflater.inflate(R.menu.menu_2, menu)
    }

    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
        when (menuItem.itemId) {
            R.id.menu_Addprodoct -> {
                startActivity(Intent(requireContext(), AddProductActivity::class.java))
            }
        }
        return true
    }
    fun display(list:ArrayList<Product>){
        binding?.rvProduct?.layoutManager =LinearLayoutManager(context)
        binding?.rvProduct?.setHasFixedSize(true)
        val adapter = ProductAdapter(requireContext(),list,this)
        binding?.rvProduct?.adapter = adapter
    }
    override fun onResume() {
        super.onResume()
        getListProduct()
    }
    fun getListProduct(){
        FirestoreClass().getListProduct(this)
    }

    fun deleteProduct(productId: String) {
        dialog(productId)
    }

    fun dialog(productId: String){
        val alertDialog = AlertDialog.Builder(context)
        alertDialog.setIcon(android.R.drawable.ic_dialog_alert)
        alertDialog.setTitle("Alert Dialog")
        alertDialog.setMessage("Do you want to delete this product?")
        alertDialog.setNegativeButton("No"){dialog,which->
            dialog.dismiss()
        }
        alertDialog.setPositiveButton("Yes"){dialog,which->
            FirestoreClass().deleteProduct(this,productId)
        }
        alertDialog.create()
        alertDialog.show()
    }
}