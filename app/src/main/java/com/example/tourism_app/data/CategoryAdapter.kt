package com.example.tourism_app.data

import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.tourism_app.R
import com.google.firebase.storage.FirebaseStorage
import java.io.File

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

        //holder.image.setImageResource(currentItem.imageId!!)
        holder.name.text = currentItem.name
        var imageName = currentItem.name
        if(imageName != null) {
            imageName = "category_" +imageName.lowercase()
            val storageRef = FirebaseStorage.getInstance().reference.child("Category/$imageName.jpg")
            val localfile = File.createTempFile("temp2Image", "jpg")
            storageRef.getFile(localfile).addOnSuccessListener {
                val bitmap = BitmapFactory.decodeFile(localfile.absolutePath)
                holder.image.setImageBitmap(bitmap)
            }
        }
    }

    override fun getItemCount(): Int {
        return categoryList.size
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val name : TextView = itemView.findViewById(R.id.tvCategoryCard)
        val image : ImageView = itemView.findViewById(R.id.ivCategory)
    }
}