package com.example.tourism_app.data.details.transports

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.tourism_app.R

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
        val resourceId = imageView.resources.getIdentifier(imageName, "drawable", imageView.context.packageName)

        if (resourceId != 0) {
            imageView.setImageResource(resourceId)
        } else {
            // Log an error if the resource is not found
            Log.e("SubwayAdapter", "Resource not found for subway: $subway")
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