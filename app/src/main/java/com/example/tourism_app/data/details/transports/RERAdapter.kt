package com.example.tourism_app.data.details.transports

import android.content.ContentValues
import android.graphics.BitmapFactory
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.tourism_app.R
import com.google.firebase.storage.FirebaseStorage
import java.io.File

class RERAdapter(
    private val rerList: ArrayList<String>
): RecyclerView.Adapter<RERAdapter.MyViewHolder>() {
    class MyViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val image: ImageView = itemView.findViewById(R.id.ivRER)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(
            R.layout.rer_card, parent, false
        )
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val rer = rerList[position]
        val imageView = holder.image
        val imageName = "rer${rer.lowercase()}"
        val storageRef = FirebaseStorage.getInstance().reference.child("Transport/RER/$imageName.png")
        val localFile = File.createTempFile("temp3Image", "png")
        storageRef.getFile(localFile).addOnSuccessListener {
            val bitmap = BitmapFactory.decodeFile(localFile.absolutePath)
            imageView.setImageBitmap(bitmap)
        }.addOnFailureListener { exception ->
            Log.e(ContentValues.TAG, "Error downloading rer image :", exception)
        }
    }

    override fun getItemCount(): Int {
        return rerList.size
    }
}