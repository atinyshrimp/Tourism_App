package com.example.tourism_app.data.details.transports

import android.content.ContentValues
import android.graphics.BitmapFactory
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.tourism_app.R
import com.google.firebase.storage.FirebaseStorage
import java.io.File

class SubwayAdapter(
    private val subwayList: ArrayList<String>
): RecyclerView.Adapter<SubwayAdapter.MyViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(
            R.layout.subway_card, parent, false
        )
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val subway = subwayList[position]
        val subData = subway.split(":")
        val subNb = subData[0]
        val subStation = subData[1]
        val imageView = holder.image
        val imageName = "metro$subNb"
        val storageRef = FirebaseStorage.getInstance().reference.child("Transport/Subway/$imageName.png")
        val localFile = File.createTempFile("temp3Image", "png")
        storageRef.getFile(localFile).addOnSuccessListener {
            val bitmap = BitmapFactory.decodeFile(localFile.absolutePath)
            imageView.setImageBitmap(bitmap)
        }.addOnFailureListener { exception ->
            Log.e(ContentValues.TAG, "Error downloading rer image :", exception)
        }

        holder.station.text = subStation
    }

    override fun getItemCount(): Int {
        return subwayList.size
    }

    class MyViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val image: ImageView = itemView.findViewById(R.id.ivSubway)
        val station: TextView = itemView.findViewById(R.id.tvStation)
    }
}