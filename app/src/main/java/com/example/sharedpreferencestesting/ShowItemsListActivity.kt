package com.example.sharedpreferencestesting

import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.sharedpreferencestesting.adapter.ItemsListAdapter
import com.example.sharedpreferencestesting.databinding.ActivityShowItemsListBinding
import com.example.sharedpreferencestesting.databinding.AddCartLayoutBinding
import com.example.sharedpreferencestesting.model.Cart
import com.example.sharedpreferencestesting.model.Items
import com.example.sharedpreferencestesting.preferences.MyPreferences

class ShowItemsListActivity : AppCompatActivity(), ItemsListAdapter.OnItemClickedListener {
    private lateinit var cart: Cart
    private var position: Int = 0
    private var activityShowItemsListBinding: ActivityShowItemsListBinding? = null
    private var addCartLayoutBinding: AddCartLayoutBinding? = null
    private var dialogBuilder: AlertDialog? = null
    private var itemsListAdapter: ItemsListAdapter? = null
    private var myPreferences: MyPreferences? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setUpView()
        setOnCLickListener()
        setUpData()
        setUpRecyclerView()
    }

    override fun itemClick(item: Items) {
        Toast.makeText(
            this@ShowItemsListActivity,
            "Don't Click Me",
            Toast.LENGTH_SHORT
        ).show()
    }

    override fun editItem(item: Items, position: Int) {
        showAddCartDialog(item, position)
    }

    override fun removeItem(item: Items, position: Int) {
        cart.listItem?.remove(item)
        updateCart()
    }

    private fun setUpView() {
        supportActionBar?.hide()
        dialogBuilder = AlertDialog.Builder(this).create()
        activityShowItemsListBinding = ActivityShowItemsListBinding.inflate(layoutInflater)
        addCartLayoutBinding = AddCartLayoutBinding.inflate(layoutInflater)
        setContentView(activityShowItemsListBinding?.root)
    }

    private fun setUpData() {
        val bundle = intent.extras ?: return
        cart = bundle["cart"] as Cart
        position = bundle["position"] as Int
        myPreferences = MyPreferences.getPreferences(this)
        if (cart.listItem == null) {
            cart.listItem = mutableListOf<Items>()
        }
    }

    private fun setOnCLickListener() {
        activityShowItemsListBinding?.ivSaveButton?.setOnClickListener {
            val name = activityShowItemsListBinding?.etAddNewItem?.text.toString()
            if (TextUtils.isEmpty(name)) {
                Toast.makeText(
                    this@ShowItemsListActivity,
                    "Enter Cart name",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                activityShowItemsListBinding?.etAddNewItem?.text = null
                cart.listItem?.add(Items(null, name, null))
                updateCart()
            }
        }
    }

    private fun setUpRecyclerView() {
        activityShowItemsListBinding?.rvItemList?.layoutManager = LinearLayoutManager(this)
        itemsListAdapter = ItemsListAdapter(this)
        cart.listItem?.let { itemsListAdapter?.setItemsList(it) }
        activityShowItemsListBinding?.rvItemList?.adapter = itemsListAdapter
    }

    private fun showAddCartDialog(item: Items, position: Int) {
        addCartLayoutBinding?.tvCreate?.text = "Update"
        addCartLayoutBinding?.etEnterCart?.setText(cart.cartName)
        addCartLayoutBinding?.etEnterCart?.setText(item.itemName)
        // Onclick Listener
        addCartLayoutBinding?.tvCreate?.setOnClickListener(View.OnClickListener {
            val name = addCartLayoutBinding?.etEnterCart?.text.toString()
            // if no enter name Cart
            if (TextUtils.isEmpty(name)) {
                Toast.makeText(
                    this@ShowItemsListActivity,
                    "Enter Cart name",
                    Toast.LENGTH_SHORT
                ).show()
                return@OnClickListener
            }
            item.itemName = name
            cart.listItem?.set(position, item)
            updateCart()
            cart.listItem?.let { i -> itemsListAdapter?.setItemsList(i) }
            dialogBuilder?.dismiss()
        })
        addCartLayoutBinding?.tvCancel?.setOnClickListener { dialogBuilder?.dismiss() }
        // dialogBuilder set view
        dialogBuilder?.setView(addCartLayoutBinding?.root)
        dialogBuilder?.show()
    }

    private fun updateCart() {
        myPreferences?.editCart(this.position, this.cart)
        cart.listItem?.let { i -> itemsListAdapter?.setItemsList(i) }
    }
}