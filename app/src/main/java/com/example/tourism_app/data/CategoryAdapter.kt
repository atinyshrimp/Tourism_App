package com.example.tourism_app.data

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.tourism_app.R

class CategoryAdapter (private val categoryList: ArrayList<Category>) : RecyclerView.Adapter<CategoryAdapter.MyViewHolder>(){
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(
            R.layout.category_card, parent,
            false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = categoryList[position]

        holder.image.setImageResource(currentItem.imageId!!)
        holder.name.text = currentItem.name
    }

    override fun getItemCount(): Int {
        return categoryList.size
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val name : TextView = itemView.findViewById(R.id.tvCategoryCard)
        val image : ImageView = itemView.findViewById(R.id.ivCategory)
    }
}