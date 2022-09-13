package com.example.sharedpreferencestesting

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.sharedpreferencestesting.adapter.CartListAdapter
import com.example.sharedpreferencestesting.databinding.ActivityMainBinding
import com.example.sharedpreferencestesting.databinding.AddCartLayoutBinding
import com.example.sharedpreferencestesting.model.Cart
import com.example.sharedpreferencestesting.preferences.MyPreferences

class MainActivity : AppCompatActivity(), CartListAdapter.OnItemClickedListener {
    private var activityMainBinding: ActivityMainBinding? = null
    private var addCartLayoutBinding: AddCartLayoutBinding? = null
    private var cartListAdapter: CartListAdapter? = null
    private var myPreferences: MyPreferences? = null
    private var dialogBuilder: AlertDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setUpView()
        setUpData()
        setOnCLickListener()
        setUpRecyclerView()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 0x9345) {
            if (resultCode == RESULT_OK) {
                if (data == null) {
                    return
                }
                val tempCart: Cart = data.extras?.get("cart") as Cart
                val tempCartPosition: Int = data.extras?.get("position") as Int
                myPreferences?.editCart(tempCartPosition, tempCart)
                cartListAdapter?.setCartList(myPreferences?.getListCart())
            }
        }
    }

    override fun itemClick(cart: Cart, position: Int) {
        val intent = Intent(this@MainActivity, ShowItemsListActivity::class.java)
        intent.putExtra("cart", cart)
        intent.putExtra("position", position)
        startActivityForResult(intent, 0x9345)
    }

    override fun editItem(cart: Cart, position: Int) {
        showAddCartDialog(true, cart, position)
    }

    override fun removeItem(cart: Cart) {
        myPreferences?.removeCart(cart)
        cartListAdapter?.setCartList(myPreferences?.getListCart())
    }

    private fun setUpView() {
        supportActionBar?.hide()
        dialogBuilder = AlertDialog.Builder(this).create()
        activityMainBinding = ActivityMainBinding.inflate(layoutInflater)
        addCartLayoutBinding = AddCartLayoutBinding.inflate(layoutInflater)
        setContentView(activityMainBinding?.root)
    }

    private fun setUpData() {
        myPreferences = MyPreferences.getPreferences(this)
    }

    private fun setOnCLickListener() {
        activityMainBinding?.ivAddNewCart?.setOnClickListener {
            showAddCartDialog(
                false,
                null,
                null
            )
        }
    }

    private fun setUpRecyclerView() {
        activityMainBinding?.rvItemList?.layoutManager = LinearLayoutManager(this)
        cartListAdapter = CartListAdapter(this)
        cartListAdapter?.setCartList(myPreferences?.getListCart())
        activityMainBinding?.rvItemList?.adapter = cartListAdapter
    }

    private fun showAddCartDialog(isForEdit: Boolean, cart: Cart?, position: Int?) {
        // set Dialog View
        if (isForEdit) {
            addCartLayoutBinding?.tvCreate?.text = "Update"
            addCartLayoutBinding?.etEnterCart?.setText(cart?.cartName)
        } else {
            addCartLayoutBinding?.tvCreate?.text = "Create"
            addCartLayoutBinding?.etEnterCart?.setText(cart?.cartName)
        }
        // Onclick Listener
        addCartLayoutBinding?.tvCreate?.setOnClickListener(View.OnClickListener {
            val name = addCartLayoutBinding?.etEnterCart?.text.toString()
            // if no enter name Cart
            if (TextUtils.isEmpty(name)) {
                Toast.makeText(this@MainActivity, "Enter Cart name", Toast.LENGTH_SHORT).show()
                return@OnClickListener
            }
            if (isForEdit) {
                if (position != null) {
                    if (cart != null) {
                        cart.cartName = name
                        myPreferences?.editCart(position, cart)
                    }
                }
                cartListAdapter?.setCartList(myPreferences?.getListCart())
            } else {
                myPreferences?.addCart(Cart(0x123, name, null))
                cartListAdapter?.setCartList(myPreferences?.getListCart())
            }
            dialogBuilder?.dismiss()
        })
        addCartLayoutBinding?.tvCancel?.setOnClickListener { dialogBuilder?.dismiss() }
        // dialogBuilder set view
        dialogBuilder?.setView(addCartLayoutBinding?.root)
        dialogBuilder?.show()
    }
}