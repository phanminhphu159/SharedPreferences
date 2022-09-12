package com.example.sharedpreferencestesting.preferences

import android.content.Context
import android.content.SharedPreferences
import android.content.SharedPreferences.Editor
import com.example.sharedpreferencestesting.model.Cart
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type

class MyPreferences(context: Context) {

    private var sharedPreferences: SharedPreferences? = null
    private var editor: Editor? = null
    private val gson: Gson = Gson()

    init {
        sharedPreferences =
            context.getSharedPreferences(Config.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE)
        if (sharedPreferences != null) {
            editor = sharedPreferences?.edit()
        }
        editor?.apply()
    }

    companion object {
        private var myPreferences: MyPreferences? = null
        fun getPreferences(context: Context): MyPreferences {
            if (myPreferences == null)
                myPreferences = MyPreferences(context)
            return myPreferences as MyPreferences
        }
    }

    fun getListCart(): MutableList<Cart> {
        val jsonText: String? =
            sharedPreferences?.getString(Config.listCart, null)
        val listType: Type = object : TypeToken<MutableList<Cart>?>() {}.type
        return gson.fromJson(jsonText, listType)
    }

    fun setListCart(listCart: MutableList<Cart>) {
        val jsonText: String = gson.toJson(listCart)
        editor?.putString(Config.listCart, jsonText)
        editor?.apply()
    }

    fun addCart(cart: Cart) {
        val temp: MutableList<Cart> = this.getListCart()
        temp.add(cart)
        setListCart(temp)
    }

    fun removeCart(cart: Cart) {
        val temp: MutableList<Cart> = this.getListCart()
        temp.remove(cart)
        setListCart(temp)
    }

    fun editCart(position: Int, cart: Cart) {
        val temp: MutableList<Cart> = this.getListCart()
        temp[position] = cart
        setListCart(temp)
    }
}


