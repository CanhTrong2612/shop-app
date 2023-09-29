package com.example.shop.fragment

import android.app.SearchManager
import android.content.Intent
import android.os.Bundle
import android.provider.ContactsContract.CommonDataKinds.Phone
import android.view.*
import android.widget.SearchView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.manager.Lifecycle
import com.example.shop.CartActivity
import com.example.shop.MainActivity
import com.example.shop.R
import com.example.shop.SettingActivity
import com.example.shop.adapter.DashBoardAdapter
import com.example.shop.adapter.PhotoAdapter
import com.example.shop.databinding.FragmentDashboardBinding
import com.example.shop.firebase.FirestoreClass
import com.example.shop.model.Photo
import com.example.shop.model.Product
import com.example.shop.model.Users


class DashboardFragment : Fragment(),View.OnClickListener {

    private var binding:FragmentDashboardBinding?= null
    private var mUser:Users?= null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentDashboardBinding.inflate(layoutInflater,container,false)
        binding?.ibCart?.setOnClickListener(this)
        binding?.ibSeting?.setOnClickListener(this)
        binding?.search?.setOnClickListener { search() }
        FirestoreClass().getUserName(this)
//        val menuHost:MenuHost = requireActivity()
//        menuHost.addMenuProvider(this, viewLifecycleOwner,androidx.lifecycle.Lifecycle.State.RESUMED )
      //  getAllProduct()
        displayViewPager()
        FirestoreClass().getAllProduct(this@DashboardFragment)
        return binding?.root
    }
//    fun setFragmentToolbar(){
//        ( requireActivity() as MainActivity).setSupportActionBar(binding?.toolbarr)
//        ( requireActivity() as MainActivity).supportActionBar!!.title =""
//    }

//    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
//        menuInflater.inflate(R.menu.menu_1,menu)
//    }

//    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
//        when(menuItem.itemId){
//            R.id.fragment_setting->{
//                startActivity(Intent(requireContext(),SettingActivity::class.java))
//            }
//            R.id.action_cart->{
//                startActivity(Intent(requireContext(),CartActivity::class.java))
//            }
//
//        }
//        return true
//    }
    fun display(list:ArrayList<Product>){
        binding?.rvDashboard?.layoutManager = GridLayoutManager(context,2)
        binding?.rvDashboard?.setHasFixedSize(true)
        val adapter = DashBoardAdapter(requireContext(),list,this)
        binding?.rvDashboard?.adapter = adapter
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
    fun getAllProduct(){
        FirestoreClass().getAllProduct(this)
    }

    override fun onClick(view: View?) {
        when(view!!.id){
            R.id.ib_seting->{
                startActivity(Intent(requireContext(),SettingActivity::class.java))
            }
            R.id.ib_cart->{
                startActivity(Intent(requireContext(),CartActivity::class.java))
            }
        }
    }
    fun search(){
        binding?.search?.setOnQueryTextListener(object: SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query : String?): Boolean {
                if (query != null) {
                    searchDataSuccess(query)
                }
                return false
            }
            override fun onQueryTextChange(searchText: String?): Boolean {
                if (searchText != null) {
                    searchDataSuccess(searchText)
                }
                return false
            }
        })
    }
    fun searchDataSuccess(queryData: String){
        FirestoreClass().searchProduct(this,queryData)
    }
    fun getName(user: Users){
        binding?.txtName?.setText(user?.firstName+""+mUser?.lastName)
    }
    fun addProductFavorite(id:String,product: Product){
        FirestoreClass().addProductFavorite(this,product!!,id)
    }
    fun addProductFavoriteSuccess(){
        Toast.makeText(requireContext(),"Added product to favorites",Toast.LENGTH_SHORT).show()
    }




}