package com.example.sharedpreferencestesting.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.sharedpreferencestesting.databinding.ItemRvBinding
import com.example.sharedpreferencestesting.model.Cart
import com.example.sharedpreferencestesting.adapter.CartListAdapter.MyViewHolder

class CartListAdapter(
    private val clickListener: OnItemClickedListener
) : RecyclerView.Adapter<MyViewHolder>() {

    private lateinit var cartList: MutableList<Cart>

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            ItemRvBinding.inflate(LayoutInflater.from(parent.context), parent, false), clickListener
        )
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        cartList[position].let { holder.onBindData(it, position) }
    }

    override fun getItemCount(): Int = cartList.size

    fun setCartList(cartList: MutableList<Cart>?) {
        if (cartList != null) {
            this.cartList = cartList
        }
        notifyDataSetChanged()
    }

    inner class MyViewHolder(
        private val itemBinding: ItemRvBinding,
        private val clickListener: OnItemClickedListener
    ) : RecyclerView.ViewHolder(itemBinding.root) {
        fun onBindData(item: Cart, position: Int) {
            with(itemBinding) {
                tvCartName.text = item.cartName
                itemView.setOnClickListener {
                    clickListener.itemClick(
                        cartList[position],
                        position)
                }
                ivEditCart.setOnClickListener {
                    clickListener.editItem(
                        cartList[position], position
                    )
                }
                ivRemoveCart.setOnClickListener {
                    clickListener.removeItem(
                        cartList[position]
                    )
                }
            }
        }
    }

    interface OnItemClickedListener {
        fun itemClick(cart: Cart, position: Int)
        fun editItem(cart: Cart, position: Int)
        fun removeItem(cart: Cart)
    }
}
