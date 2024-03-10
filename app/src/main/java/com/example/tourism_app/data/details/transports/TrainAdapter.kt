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

class TrainAdapter(
    private val trainList: ArrayList<String>
): RecyclerView.Adapter<TrainAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(
            R.layout.subway_card, parent, false
        )
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val train = trainList[position]
        val trainData = train.split(":")
        val trainChar = trainData[0]
        val trainStop = trainData[1]

        val imageView = holder.image
        val imageName = "train${trainChar.lowercase()}"
        val storageRef = FirebaseStorage.getInstance().reference.child("Transport/Train/$imageName.png")
        val localFile = File.createTempFile("temp3Image", "png")
        storageRef.getFile(localFile).addOnSuccessListener {
            val bitmap = BitmapFactory.decodeFile(localFile.absolutePath)
            imageView.setImageBitmap(bitmap)
        }.addOnFailureListener { exception ->
            Log.e(ContentValues.TAG, "Error downloading bus image :", exception)
        }

        holder.stop.text = trainStop
    }

    override fun getItemCount(): Int {
        return trainList.size
    }

    class MyViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val image: ImageView = itemView.findViewById(R.id.ivSubway)
        val stop: TextView = itemView.findViewById(R.id.tvStation)
    }
}