package com.caton.kotlindemo

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import org.w3c.dom.Text

class RVAdapter(var mData: MutableList<String>) : RecyclerView.Adapter<ItemViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        return ItemViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_rv, parent, false));
    }

    override fun getItemCount(): Int = mData.size

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.bind(mData.get(position))
    }

}

class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
    override fun onClick(v: View?) {
        Toast.makeText(itemView.context, tvName.text, Toast.LENGTH_SHORT).show()
    }

    lateinit var tvName: TextView

    init {
        tvName=itemView.findViewById<TextView>(R.id.tv_name)
        itemView.setOnClickListener(this)
    }

    fun bind(data: String): Unit {
        tvName.text = data
    }
}