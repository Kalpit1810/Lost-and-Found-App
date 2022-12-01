package com.example.lostandfound

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.*
import com.google.firebase.storage.FirebaseStorage

class MyPostActivity : AppCompatActivity() {
    private lateinit var itemRecyclerView: RecyclerView
    private lateinit var itemArrayList: ArrayList<ItemsList>
    private lateinit var myAdapter: MyPostAdapter
    private lateinit var db: FirebaseFirestore
    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_post)

        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()

        val currentUser = auth.currentUser
        val currentUserUid = currentUser?.uid.toString()

        itemRecyclerView = findViewById(R.id.My_post_list)
        itemRecyclerView.layoutManager = LinearLayoutManager(this)
        itemRecyclerView.setHasFixedSize(true)

        itemArrayList = arrayListOf<ItemsList>()

        myAdapter= MyPostAdapter(itemArrayList)
        itemRecyclerView.adapter = myAdapter

        EventChangeListener(currentUserUid)


    }

    private fun EventChangeListener(s:String){
        db = FirebaseFirestore.getInstance()
        db.collection("ItemsList").whereEqualTo("uid", s)
            .addSnapshotListener(object: EventListener<QuerySnapshot> {
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