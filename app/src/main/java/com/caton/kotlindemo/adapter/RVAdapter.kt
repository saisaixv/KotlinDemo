package com.caton.kotlindemo.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.caton.kotlindemo.R
import com.caton.kotlindemo.dao.Item
import com.caton.kotlindemo.holder.ItemViewHolder

class RVAdapter(var mData: MutableList<Item>) : RecyclerView.Adapter<ItemViewHolder>() {

    lateinit var onItemClickListener: OnItemClickListener


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        return ItemViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_rv,
                parent,
                false
            )
        );
    }

    override fun getItemCount(): Int = mData.size

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.bind(mData.get(position).title)
        holder.itemView.setOnClickListener(View.OnClickListener {
            onItemClickListener?.onItemClick(it, position)
        })
    }


    interface OnItemClickListener {
        fun onItemClick(view: View, position: Int)
    }

}


