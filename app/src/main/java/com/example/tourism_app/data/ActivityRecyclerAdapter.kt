package com.example.tourism_app.data

import android.annotation.SuppressLint
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.tourism_app.R
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import java.io.File

class ActivityRecyclerAdapter(
    private val activityList: ArrayList<Activity>,
    private val listener: ActivityRecyclerEvent
) : RecyclerView.Adapter<ActivityRecyclerAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(
            R.layout.activity_card, parent,false)
        return MyViewHolder(itemView)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = activityList[position]

        holder.name.text = currentItem.name
        holder.category.text = currentItem.category
        holder.location.text = "Paris ${currentItem.getArrondissement()}"
        holder.visitCount.text = "(51)"

        var imageName = currentItem.name
        if(imageName != null) {
            imageName = imageName.replace(" ", "")
            val storageRef = FirebaseStorage.getInstance().reference.child("LieuImage/$imageName.jpg")
            val localfile = File.createTempFile("tempImage", "jpg")
            storageRef.getFile(localfile).addOnSuccessListener {
                val bitmap = BitmapFactory.decodeFile(localfile.absolutePath)
                holder.picture.setImageBitmap(bitmap)
            }
        }
        holder.like_button.setOnClickListener{
            //we need the pseudo to save it at the right place
            //if already saved then delete it otherwise create new element in bdd
            val database = FirebaseDatabase.getInstance().reference
            val saved_lieu_ref = database.child("Saved_lieu")
            val newUserRef = saved_lieu_ref.push()
            newUserRef.child("name").setValue("")
            newUserRef.child("visited").setValue(0)
        }
    }

    override fun getItemCount(): Int {
        return activityList.size
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        val name : TextView = itemView.findViewById(R.id.tvName)
        val location : TextView = itemView.findViewById(R.id.tvLocation)
        val visitCount : TextView = itemView.findViewById(R.id.tvVisitCount)
        val category : TextView = itemView.findViewById(R.id.tvCategory)
        private val cardClickable : ConstraintLayout = itemView.findViewById(R.id.constraintLayout)
        val picture : ImageView = itemView.findViewById(R.id.ivPicture)
        val like_button : Button = itemView.findViewById(R.id.ibLike)

        init {
            cardClickable.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            val position = adapterPosition
            if (position != RecyclerView.NO_POSITION) {
                listener.onItemClick(position)
            }
        }
    }

    interface ActivityRecyclerEvent {
        fun onItemClick(position: Int)
    }
}