package com.example.shop.firebase

import android.app.Activity
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.shop.*
import com.example.shop.adapter.DashBoardAdapter
import com.example.shop.fragment.DashboardFragment
import com.example.shop.fragment.OrderFragment
import com.example.shop.fragment.ProductFragment
import com.example.shop.model.*
import com.example.shop.utils.Constant
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlin.contracts.contract

class FirestoreClass {
    private var mFireStore = FirebaseFirestore.getInstance()
    fun registerUser(activity: RegisterActivity, users: Users) {
        mFireStore.collection("users")
            .document(users.id)
            .set(users, SetOptions.merge())
            .addOnSuccessListener {
                activity.userRegisterSuccess()
            }
            .addOnFailureListener { e ->
                Log.e(activity.javaClass.simpleName, "Error while registering the user", e)
            }
    }

    fun getCurrentID(): String {
        // An Instance of currentUser using FirebaseAuth
        val currentUser = FirebaseAuth.getInstance().currentUser

        // A variable to assign the currentUserId if it is not null or else it will be blank.
        var currentUserID = ""
        if (currentUser != null) {
            currentUserID = currentUser.uid
        }

        return currentUserID
    }

    fun getUserDetail(activity: Activity) {
        mFireStore.collection("users")
            .document(getCurrentID())
            .get()
            .addOnSuccessListener { document ->
                val user = document.toObject(Users::class.java)
                when (activity) {
                    is LoginActivity -> {
                        if (user !== null) {
                            activity.userLoggedInSuccess(user)
                        }
                    }
//                    is ProfileActivity ->{
//                        if (user!= null)
//                            activity.getDataUer(user)
//                    }
                    is SettingActivity -> {
                        if (user != null) {
                            activity.getUserSuccess(user)
                        }
                    }
                }
            }
    }

    fun updateUser(activity: ProfileActivity, hashMap: HashMap<String, Any>) {
        mFireStore.collection("users")
            .document(getCurrentID())
            .update(hashMap)
            .addOnSuccessListener {
                activity.updateUserSuccess()
            }
            .addOnFailureListener { e ->
                Log.e(activity.javaClass.simpleName, "Error while updating the user details", e)
            }

    }

    fun uploadImageToCloudStorge(activity: Activity, imageUri: Uri?) {
        val sRef: StorageReference = FirebaseStorage.getInstance().reference.child(
            Constant.USER_PROFILE_IMAGE + System.currentTimeMillis() +
                    "." + Constant.getFileExtension(activity, imageUri!!)
        )
        sRef.putFile(imageUri).addOnSuccessListener { taskSnapshot ->
            Log.e("Firebase image url", taskSnapshot.metadata!!.reference!!.downloadUrl.toString())
            taskSnapshot.metadata!!.reference!!.downloadUrl
        }
            .addOnSuccessListener { uri ->
                Log.e("Downlaod image Url", uri.toString())
                when (activity) {
                    is ProfileActivity -> {
                        activity.imageUploadSuccess(imageUri!!.toString())
                    }
                    is AddProductActivity -> {
                        activity.imageUploadSuccess(imageUri!!.toString())
                    }

                }

            }
            .addOnFailureListener {
                when (activity) {
                    is ProfileActivity ->
                        activity.hideProgressDialog()
                }
            }
    }

    fun addProduct(activity: AddProductActivity, product: Product) {
        mFireStore.collection(Constant.PRODUCTS)
            .document()
            .set(product, SetOptions.merge())
            .addOnSuccessListener { document ->
                activity.productUploadSuccess()
            }
            .addOnFailureListener { e ->
                Log.e(activity.javaClass.simpleName, "Error while uploading the product", e)
            }
    }

    fun getAllProduct(fragment: DashboardFragment) {
        mFireStore.collection(Constant.PRODUCTS)
            .get()
            .addOnSuccessListener { document ->
                Log.e(" All products List", document.documents.toString())
                val listProduct = ArrayList<Product>()
                for (i in document) {
                    val product = i.toObject(Product::class.java)
                    product.product_id = i.id
                    listProduct.add(product)
                }
                fragment.display(listProduct)

            }
            .addOnFailureListener { e ->
                Log.e(fragment.javaClass.simpleName, "error while get list Product", e)
            }
    }

    fun getListProduct(fragment: ProductFragment) {
        mFireStore.collection(Constant.PRODUCTS)
            .whereEqualTo(Constant.USER_ID, getCurrentID())
            .get()
            .addOnSuccessListener { document ->
                val list = ArrayList<Product>()
                Log.e("Product list", document.documents.toString())
                for (i in document) {
                    val product = i.toObject(Product::class.java)
                    product.product_id = i.id
                    list.add(product)
                    fragment.display(list)
                }
            }
            .addOnFailureListener { e ->
                Log.e(fragment.javaClass.simpleName, "error while get list Product", e)
            }
    }

    fun deleteProduct(fragment: ProductFragment, productId: String) {
        mFireStore.collection(Constant.PRODUCTS)
            .document(productId)
            .delete()
            .addOnSuccessListener {
                fragment.getListProduct()
            }
            .addOnFailureListener { e ->
                Log.e(fragment.javaClass.simpleName, "error while delete", e)
            }
    }
    fun updateProduct(activity: AddProductActivity,product:Product, productId: String){
        mFireStore.collection(Constant.PRODUCTS)
            .document(productId)
            .set(product, SetOptions.merge())
            .addOnSuccessListener {
                activity.updateProductSuccess()
            }
            .addOnFailureListener { e ->
                Log.e(activity.javaClass.simpleName, "error while delete", e)
            }
    }

    fun getProductDetail(activity: ProductDetailActivity, productId: String) {
        mFireStore.collection(Constant.PRODUCTS)
            .document(productId)
            .get()
            .addOnSuccessListener { document ->
                val product: Product? = document.toObject(Product::class.java)
                if (product != null) {
                    activity.productDetailsSuccess(product!!)
                }
            }
            .addOnFailureListener { e ->
                Log.e(activity.javaClass.simpleName, "error", e)
            }
    }

    fun addToCart(activity: ProductDetailActivity, cart: Cart) {
        mFireStore.collection(Constant.CART_ITEMS)
            .document()
            .set(cart, SetOptions.merge())
            .addOnSuccessListener {
                activity.addToCartSuccess()
            }
            .addOnFailureListener { e ->
                Log.e(activity.javaClass.simpleName, "error while add cart!!", e)
            }
    }
    fun addToCart2(activity: CartActivity, cart: Cart) {
        mFireStore.collection("cart_to_checkout")
            .document()
            .set(cart, SetOptions.merge())
            .addOnSuccessListener {
                activity.addToCartSuccess()
            }
            .addOnFailureListener { e ->
                Log.e(activity.javaClass.simpleName, "error while add cart!!", e)
            }
    }
    fun getCartistToCheckout(activity: CkeckoutActivity) {
        mFireStore.collection("cart_to_checkout")
            .get()
            .addOnSuccessListener { document ->
                Log.e("list cart ", document.documents.toString())
                val list: ArrayList<Cart> = ArrayList()
                for (i in document.documents) {
                    val cart = i.toObject(Cart::class.java)!!
                    cart.id = i.id
                    list.add(cart)
                }
                activity.successCartItemList(list)
            }
            .addOnFailureListener { e ->
                Log.e(activity.javaClass.simpleName, "error while get cart list", e)
            }
    }

    fun getCartist1(activity: Activity) {
        mFireStore.collection(Constant.CART_ITEMS)
            .get()
            .addOnSuccessListener { document ->
                Log.e("list cart ", document.documents.toString())
                val list: ArrayList<Cart> = ArrayList()
                for (i in document.documents) {
                    val cart = i.toObject(Cart::class.java)!!
                    cart.id = i.id
                    list.add(cart)
                }
                when(activity){
                    is CartActivity->{
                        activity.getCartList(list)
                    }
                    is CkeckoutActivity ->{
                        activity.successCartItemList(list)
                    }
                }

            }
            .addOnFailureListener { e ->
                Log.e(activity.javaClass.simpleName, "error while get cart list", e)
            }
    }
    fun getCartist(activity: CartActivity) {
        mFireStore.collection(Constant.CART_ITEMS)
            .get()
            .addOnSuccessListener { document ->
                Log.e("list cart mua ", document.documents.toString())
                val list: ArrayList<Cart> = ArrayList()
                for (i in document.documents) {
                    val cart = i.toObject(Cart::class.java)!!
                    cart.id = i.id
                    list.add(cart)
                }
                activity.getCartList(list)
            }
            .addOnFailureListener { e ->
                Log.e(activity.javaClass.simpleName, "error while get cart list", e)
            }
    }

    fun deleteCart(activity: CartActivity, cartId: String) {
        mFireStore.collection(Constant.CART_ITEMS)
            .document(cartId)
            .delete()
            .addOnSuccessListener {
                activity.getCartListSuccess()
                Toast.makeText(activity, "Delete success", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { e ->
                Log.e(activity.javaClass.simpleName, "lol", e)
            }
    }

    fun updateCart(activity: CartActivity, hashMap: HashMap<String, Any>, cartId: String) {
        mFireStore.collection(Constant.CART_ITEMS)
            .document(cartId)
            .update(hashMap)
            .addOnSuccessListener {
                activity.getCartListSuccess()
            }
            .addOnFailureListener { e ->
                Log.e(activity.javaClass.simpleName, "lol", e)
            }
    }

    fun addAddress(activity: AddAddressActivity, address: Address) {
        mFireStore.collection(Constant.ADDRESSES)
            .document()
            .set(address, SetOptions.merge())
            .addOnSuccessListener {
                activity.saveAddressSuccess()
            }
            .addOnFailureListener { e ->
                Log.e(activity.javaClass.simpleName, "lol", e)
            }
    }
    fun updateAddress(activity: AddAddressActivity,address:Address,id:String){
        mFireStore.collection(Constant.ADDRESSES)
            .document(id)
            .set(address, SetOptions.merge())
            .addOnSuccessListener {
                activity.addAddressSuccess()
            }
            .addOnFailureListener { e->
                Log.e(activity.javaClass.simpleName,"error update the address",e)
            }
    }
    fun deleteAddress(activity: AddressActivity,id:String){
        mFireStore.collection(Constant.ADDRESSES)
            .document(id)
            .delete()
            .addOnSuccessListener {
                activity.getListAddress()
            }
    }

    fun getListAddress(activity: AddressActivity) {
        mFireStore.collection(Constant.ADDRESSES)
            .whereEqualTo(Constant.USER_ID, getCurrentID())
            .get()
            .addOnSuccessListener { document ->
                Log.e("listAddress", document.documents.toString())
                val listAddress = ArrayList<Address>()
                for (i in document.documents) {
                    val address = i.toObject(Address::class.java)!!
                    address.id = i.id
                    listAddress.add(address)
                }
                activity.getListAddressSuccess(listAddress)
            }
            .addOnFailureListener { e ->
                Log.e(activity.javaClass.simpleName, "error while get the address", e)
            }
    }
    fun searchProduct(fragment: Fragment,query:String ){
        mFireStore.collection(Constant.PRODUCTS)
            .whereEqualTo("title",query.toLowerCase())
            .get()
            .addOnCompleteListener {document->
                val list = ArrayList<Product>()
                Log.e("searchlist", document.result.toString())
                for (doc in document.result){
                    val i = doc.toObject(Product::class.java)!!
                 //   val product = Product(i.user_id,i.product_id,i.title,i.description)
                    list.add(i)
                }
                when(fragment){
                    is DashboardFragment ->{
                        fragment.display(list)
                    }
                    is OrderFragment ->{
                        fragment.display(list)
                    }
                }


              //  fragment.search()
            }
    }
    fun getUserName(fragment: DashboardFragment){
        mFireStore.collection("users").document(getCurrentID()).get()
            .addOnSuccessListener { document->
                val user = document.toObject(Users::class.java)!!
                fragment.getName(user)
            }
            .addOnFailureListener { e ->
                Log.e(fragment.javaClass.simpleName, "error while get the address", e)
            }
    }
    fun addProductFavorite(dashboardFragment: DashboardFragment,product: Product,productId:String){
        mFireStore.collection("ProductFavorite").document(productId).set(product, SetOptions.merge())
            .addOnSuccessListener {
                dashboardFragment.addProductFavoriteSuccess()
            }
    }
    fun getProductFavorite(favoriteFragment: OrderFragment){
        mFireStore.collection("ProductFavorite")

            .get()
            .addOnSuccessListener { document->
                val list = ArrayList<Product>()
                for (i in document.documents){
                    val product = i.toObject<Product>(Product::class.java)!!
                    list.add(product)
                }
                favoriteFragment.display(list)

            }
            .addOnFailureListener { e->
                Log.e(favoriteFragment.javaClass.simpleName, "error while get the address", e)
            }
    }
    fun deleteFavoriteProduct(fragment: OrderFragment,productId: String){
        mFireStore.collection("ProductFavorite")
            .document(productId)
            .delete()
            .addOnSuccessListener {
                fragment.getProductFavorite()
            }
            .addOnFailureListener { e->
                Log.e(fragment.javaClass.simpleName, "error while delete favorite product", e)
            }
    }
    fun placeAnorder(activity: CkeckoutActivity,order: Order){
        mFireStore.collection(Constant.ORDERS)
            .document()
            .set(order, SetOptions.merge())
            .addOnSuccessListener {
                activity.orderPlaceSuccess()
            }
            .addOnFailureListener { e->
                Log.e(activity.javaClass.simpleName,"error",e)
            }

    }
}

