package com.example.tourism_app.data.details.transports

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.tourism_app.R

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
        val resourceId = imageView.resources.getIdentifier(imageName, "drawable", imageView.context.packageName)

        if (resourceId != 0) {
            imageView.setImageResource(resourceId)
        } else {
            // Log an error if the resource is not found
            Log.e("RERAdapter", "Resource not found for RER: $rer")
        }
    }

    override fun getItemCount(): Int {
        return rerList.size
    }
}