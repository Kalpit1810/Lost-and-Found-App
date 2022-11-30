package com.example.lostandfound

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.view.menu.MenuView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.storage.FirebaseStorage
import java.util.jar.Attributes.Name

class LostItemsAdapter(private val lostItemList:ArrayList<ItemsList>): RecyclerView.Adapter<LostItemsAdapter.myViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): myViewHolder {

        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.check_post_layout,parent,false)
        return myViewHolder(itemView)

    }

    override fun onBindViewHolder(holder: myViewHolder, position: Int) {
        val currentitem = lostItemList[position]
        val imageReference = FirebaseStorage.getInstance().getReference().child("images/${currentitem.filename}")

        holder.Name.text =currentitem.uploadedBy
        holder.Phone.text = currentitem.phone
        holder.message.text = currentitem.message

        Glide.with(holder.image).load(imageReference).into(holder.image)

    }

    override fun getItemCount(): Int {
        return lostItemList.size
    }

    class myViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){

        val Name = itemView.findViewById<TextView>(R.id.tvname)
        val Phone = itemView.findViewById<TextView>(R.id.tvphone)
        val message = itemView.findViewById<TextView>(R.id.tvmessage)
        val image = itemView.findViewById<ImageView>(R.id.tvImage)
    }


}