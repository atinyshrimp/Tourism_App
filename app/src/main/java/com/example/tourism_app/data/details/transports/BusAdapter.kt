package com.example.tourism_app.data.details.transports

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.tourism_app.R

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
        val resourceId = imageView.resources.getIdentifier(imageName, "drawable", imageView.context.packageName)

        if (resourceId != 0) {
            imageView.setImageResource(resourceId)
        } else {
            // Log an error if the resource is not found
            Log.e("BusAdapter", "Resource not found for bus: $bus")
        }
    }

    class MyViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val bus: ImageView = itemView.findViewById(R.id.ivBus)
    }
}