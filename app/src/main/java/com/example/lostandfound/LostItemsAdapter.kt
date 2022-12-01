package com.example.lostandfound

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.view.menu.MenuView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.GlideContext
import com.google.firebase.database.core.Context
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.tasks.await
import java.util.jar.Attributes.Name

class LostItemsAdapter(private val context:android.content.Context, private val lostItemList:ArrayList<ItemsList>): RecyclerView.Adapter<LostItemsAdapter.myViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): myViewHolder {

        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.check_post_layout,parent,false)
        return myViewHolder(itemView)

    }

    override fun onBindViewHolder(holder: myViewHolder, position: Int) {
        val currentitem = lostItemList[position]
        val imageReference = Firebase.storage.reference.child("images/${currentitem.filename}")
        val downloadurl = imageReference.downloadUrl
        holder.Name.text =currentitem.uploadedBy
        holder.Phone.text = currentitem.phone
        holder.message.text = currentitem.message
        if(currentitem.status=="Lost")
        {
            holder.button.setText("Found It")
        }
        else{
            holder.button.setText("Claim It")
        }

        Glide.with(holder.itemView).load(downloadurl).into(holder.image)

    }

    override fun getItemCount(): Int {
        return lostItemList.size
    }

    class myViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){

        val Name = itemView.findViewById<TextView>(R.id.tvname)
        val Phone = itemView.findViewById<TextView>(R.id.tvphone)
        val message = itemView.findViewById<TextView>(R.id.tvmessage)
        val image = itemView.findViewById<ImageView>(R.id.tvImage)
        val button = itemView.findViewById<Button>(R.id.Check_post_layout_button)
    }


}