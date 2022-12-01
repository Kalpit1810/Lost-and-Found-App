package com.example.lostandfound

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.auth.User
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase

class CheckItemActivity : AppCompatActivity() {
    private lateinit var itemRecyclerView: RecyclerView
    private lateinit var itemArrayList: ArrayList<ItemsList>
    private lateinit var myAdapter: LostItemsAdapter
    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_check_item)

        val progressBar = findViewById<ProgressBar>(R.id.check_item_progressbar)
        val lostItemsListBtn = findViewById<Button>(R.id.Check_Lost_Items_Button)
        val FoundItemsListBtn = findViewById<Button>(R.id.Check_Found_Items_Button)

        progressBar.visibility= View.VISIBLE

        itemRecyclerView = findViewById(R.id.lost_item_view)
        itemRecyclerView.layoutManager = LinearLayoutManager(this)
        itemRecyclerView.setHasFixedSize(true)

        itemArrayList = arrayListOf<ItemsList>()

        myAdapter= LostItemsAdapter(this,itemArrayList)
        itemRecyclerView.adapter = myAdapter

        EventChangeListener("Lost")

        lostItemsListBtn.setOnClickListener{
            itemArrayList.clear()
            findViewById<Button>(R.id.Check_post_layout_button).setText("Found It")
            EventChangeListener("Lost")
        }

        FoundItemsListBtn.setOnClickListener{
            itemArrayList.clear()
            findViewById<Button>(R.id.Check_post_layout_button).setText("Claim It")
            EventChangeListener("Found")
        }

        progressBar.visibility= View.GONE

    }
    private fun EventChangeListener(s:String){
        db = FirebaseFirestore.getInstance()
        db.collection("ItemsList").whereEqualTo("status", s)
            .addSnapshotListener(object: EventListener<QuerySnapshot>{
                override fun onEvent(value: QuerySnapshot?, error: FirebaseFirestoreException?) {
                    if(error!=null){
                        Log.e("Firestore Error", error.message.toString())
                        return
                    }

                    for(dc: DocumentChange in value?.documentChanges!!){
                        if(dc.type == DocumentChange.Type.ADDED){
                            itemArrayList.add(dc.document.toObject(ItemsList::class.java ))
                        }
                    }

                    myAdapter.notifyDataSetChanged()
                }

            })
    }
}