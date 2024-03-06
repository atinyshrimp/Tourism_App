package com.example.tourism_app.data.details.transports

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.tourism_app.R

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
        val resourceId = imageView.resources.getIdentifier(imageName, "drawable", imageView.context.packageName)

        if (resourceId != 0) {
            imageView.setImageResource(resourceId)
        } else {
            // Log an error if the resource is not found
            Log.e("TrainAdapter", "Resource not found for train: $train")
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