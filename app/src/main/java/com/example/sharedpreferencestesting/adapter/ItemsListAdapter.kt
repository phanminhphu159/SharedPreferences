package com.example.sharedpreferencestesting.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.sharedpreferencestesting.adapter.ItemsListAdapter.ItemViewHolder
import com.example.sharedpreferencestesting.databinding.ItemRvBinding
import com.example.sharedpreferencestesting.model.Items

class ItemsListAdapter(
    private val clickListener: OnItemClickedListener
) : RecyclerView.Adapter<ItemViewHolder>() {

    private lateinit var itemsList: MutableList<Items>

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        return ItemViewHolder(
            ItemRvBinding.inflate(LayoutInflater.from(parent.context), parent, false), clickListener
        )
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        itemsList[position].let { holder.onBindData(it, position) }
    }

    override fun getItemCount(): Int = itemsList.size

    fun setItemsList(itemsList: MutableList<Items>) {
        this.itemsList = itemsList
        notifyDataSetChanged()
    }

    inner class ItemViewHolder(
        private var itemRvBinding: ItemRvBinding,
        private val clickListener: OnItemClickedListener
    ) : RecyclerView.ViewHolder(itemRvBinding.root) {

        fun onBindData(item: Items, position: Int) {
            with(itemRvBinding) {
                tvCartName.text = item.itemName
                itemView.setOnClickListener { clickListener.itemClick(itemsList[position]) }
                ivRemoveCart.setOnClickListener {
                    clickListener.removeItem(
                        itemsList[position], position
                    )
                }
                ivEditCart.setOnClickListener {
                    clickListener.editItem(
                        itemsList[position], position
                    )
                }
            }
        }
    }

    interface OnItemClickedListener {
        fun itemClick(item: Items)
        fun editItem(item: Items, position: Int)
        fun removeItem(item: Items, position: Int)
    }
}