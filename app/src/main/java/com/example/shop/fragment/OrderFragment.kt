package com.example.shop.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.recyclerview.widget.GridLayoutManager
import com.example.shop.R
import com.example.shop.adapter.DashBoardAdapter
import com.example.shop.adapter.PhotoAdapter
import com.example.shop.adapter.ProductFavoriteAdapter
import com.example.shop.databinding.FragmentOrderBinding
import com.example.shop.firebase.FirestoreClass
import com.example.shop.model.Photo
import com.example.shop.model.Product

class OrderFragment : Fragment() {
    private var binding : FragmentOrderBinding?= null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentOrderBinding.inflate(layoutInflater,container,false)
        displayViewPager()
        binding?.searchFavoriteProduct?.setOnClickListener { searchViewProductFavorite() }
        FirestoreClass().getProductFavorite(this)
        // Inflate the layout for this fragment
        return binding?.root
    }
    fun display(list:ArrayList<Product>){
        binding?.rvFaroriteProduct?.layoutManager = GridLayoutManager(context,2)
        binding?.rvFaroriteProduct?.setHasFixedSize(true)
        val adapter = ProductFavoriteAdapter(requireContext(),list,this)
        binding?.rvFaroriteProduct?.adapter = adapter
    }
    fun listPhoto():ArrayList<Photo>{
        val list= ArrayList<Photo>()
        list.add(Photo(R.drawable.ss))
        list.add(Photo(R.drawable.tainghe))
        list.add(Photo(R.drawable.ip1))
        list.add(Photo(R.drawable.lap2))
        return list
    }
    fun displayViewPager(){
        val photoAdapter = PhotoAdapter(requireContext(),listPhoto())
        binding?.viewPager?.adapter= photoAdapter
        binding?.circleIndication?.setViewPager(binding?.viewPager)

    }
    fun searchViewProductFavorite(){
        binding?.searchFavoriteProduct?.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(string: String?): Boolean {
                searchDataSuccess(string!!)
                return false
            }

            override fun onQueryTextChange(string: String?): Boolean {
                searchDataSuccess(string!!)
                return false
            }

        })

    }
    fun getProductFavorite(){
        FirestoreClass().getProductFavorite(this)
    }
    fun searchDataSuccess(queryData: String){
        FirestoreClass().searchProduct(this,queryData)
    }
    fun deleteProductFavorite(id:String,product: Product){
        FirestoreClass().deleteFavoriteProduct(this,id)
    }




}