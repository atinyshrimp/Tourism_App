package com.example.tourism_app.data.details.transports

import android.content.ContentValues.TAG
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

class BusAdapter(
    private val busList: ArrayList<String>
): RecyclerView.Adapter<BusAdapter.MyViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(
            R.layout.bus_card, parent, false
        )
        return MyViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return busList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val bus = busList[position]
        val imageView = holder.bus
        val imageName = "bus$bus"
        val storageRef = FirebaseStorage.getInstance().reference.child("Transport/Bus/$imageName.png")
        val localFile = File.createTempFile("temp3Image", "png")
        storageRef.getFile(localFile).addOnSuccessListener {
            val bitmap = BitmapFactory.decodeFile(localFile.absolutePath)
            imageView.setImageBitmap(bitmap)
        }.addOnFailureListener { exception ->
            Log.e(TAG, "Error downloading bus image :", exception)
        }
    }

    class MyViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val bus: ImageView = itemView.findViewById(R.id.ivBus)
    }
}