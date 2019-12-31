package com.caton.kotlindemo.holder;
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.caton.kotlindemo.R


class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    lateinit var tvName: TextView

    init {
        tvName=itemView.findViewById<TextView>(R.id.tv_name)
    }

    fun bind(data: String): Unit {
        tvName.text = data
    }
}
