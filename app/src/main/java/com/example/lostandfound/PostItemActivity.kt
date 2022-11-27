package com.example.lostandfound

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.Toast
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.contract.ActivityResultContracts
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import java.net.URI
import java.util.concurrent.ThreadLocalRandom.current

class PostItemActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase
    private lateinit var storage: FirebaseStorage
    private lateinit var uri5: Uri
    private lateinit var downloadurl:String




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post_item)

        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()
        storage = Firebase.storage

        val db = Firebase.firestore
        val currentUser = auth.currentUser
        val currentUserUid = currentUser?.uid.toString()
        val progressBar = findViewById<ProgressBar>(R.id.Post_Item_Progerss_Bar)
        val storageref = storage.reference
        val selectImagesBtn = findViewById<Button>(R.id.Post_Item_Select_Image_Button)
        val postFoundBtn = findViewById<Button>(R.id.Post_Found_Button)
        val postLostBtn = findViewById<Button>(R.id.Post_Lost_Button)
        var count =0
        var currentUserName = ""
        var currentUserPhone = ""

        val gallery = registerForActivityResult(ActivityResultContracts.GetContent()) {uri : Uri? ->
            if (uri != null) {
                uri5 = uri
            }
        }


        progressBar.visibility = View.VISIBLE

        database.reference.child("users").child(currentUserUid).get().addOnSuccessListener {
            currentUserName  = it.child("name").value.toString()
            currentUserPhone = it.child("phone").value.toString()
            findViewById<EditText>(R.id.Post_Item_Name).setText(currentUserName)
            findViewById<EditText>(R.id.Post_Item_Phone_No).setText(currentUserPhone)
            progressBar.visibility = View.GONE
        }

        selectImagesBtn.setOnClickListener{
            gallery.launch("image/*")
                findViewById<ImageView>(R.id.image1).visibility = View.VISIBLE
                findViewById<ImageView>(R.id.image1).setImageURI(uri5)
        }

        postFoundBtn.setOnClickListener {
            progressBar.visibility = View.VISIBLE

            val status: String = "Found"

            storageref.child(System.currentTimeMillis().toString()).putFile(uri5)
                    .addOnSuccessListener { task ->
                        task.metadata!!.reference!!.downloadUrl.addOnSuccessListener {
                            val d = it.toString()
                            downloadurl = d
                        }
                    }
            val detailsOfPost = hashMapOf(
                "name" to currentUserName,
                "phone" to currentUserPhone,
                "status" to status,
                "uid" to currentUserUid,
                "downloadurl" to downloadurl
            )

            db.collection("ItemsList").add(detailsOfPost)
                .addOnSuccessListener {
                    progressBar.visibility = View.GONE
                    Toast.makeText(this, "Item Posted", Toast.LENGTH_LONG).show()
                }
                .addOnFailureListener{
                    progressBar.visibility = View.GONE
                    Toast.makeText(this, "Something Went Wrong", Toast.LENGTH_LONG).show()
                }
        }

        postLostBtn.setOnClickListener {
            progressBar.visibility = View.VISIBLE

            val status: String = "Lost"

            storageref.child(System.currentTimeMillis().toString()).putFile(uri5)
                .addOnSuccessListener { task ->
                    task.metadata!!.reference!!.downloadUrl.addOnSuccessListener {
                        val d = it.toString()
                        downloadurl = d
                    }
                }
            val detailsOfPost = hashMapOf(
                "name" to currentUserName,
                "phone" to currentUserPhone,
                "status" to status,
                "uid" to currentUserUid,
                "downloadurl" to downloadurl
            )

            db.collection("ItemsList").add(detailsOfPost)
                .addOnSuccessListener {
                    progressBar.visibility = View.GONE
                    Toast.makeText(this, "Item Posted", Toast.LENGTH_LONG).show()
                }
                .addOnFailureListener{
                    progressBar.visibility = View.GONE
                    Toast.makeText(this, "Something Went Wrong", Toast.LENGTH_LONG).show()
                }
        }


//        selectImagesBtn.setOnClickListener{
//            if(count ==0) {
//                count++
//                gallery.launch("image/*")
//                uri5 = uri
//                findViewById<ImageView>(R.id.image1).visibility = View.VISIBLE
//                findViewById<ImageView>(R.id.image1).setImageURI(uri5)
//            }
//            else if(count ==1) {
//                count++
//                gallery.launch("image/*")
//                uri1= uri
//                findViewById<ImageView>(R.id.image2).visibility = View.VISIBLE
//                findViewById<ImageView>(R.id.image2).setImageURI(uri1)
//            }
//            else if(count ==2) {
//                count++
//                gallery.launch("image/*")
//                uri2 = uri
//                findViewById<ImageView>(R.id.image3).visibility = View.VISIBLE
//                findViewById<ImageView>(R.id.image3).setImageURI(uri2)
//            }
//            else if(count ==3) {
//                count++
//                gallery.launch("image/*")
//                uri3 = uri
//                findViewById<ImageView>(R.id.image4).visibility = View.VISIBLE
//                findViewById<ImageView>(R.id.image4).setImageURI(uri3)
//            }
//            else if(count ==4) {
//                count++
//                gallery.launch("image/*")
//                uri4 = uri
//                findViewById<ImageView>(R.id.image5).visibility = View.VISIBLE
//                findViewById<ImageView>(R.id.image5).setImageURI(uri4)
//            }
//            else
//            {
//                Toast.makeText(this, "Max 5 Images can be Uploaded", Toast.LENGTH_LONG).show()
//            }
//            val intent = Intent(this, PostItemActivity::class.java)
//            startActivity(intent)
//        }
//
//
//        postFoundBtn.setOnClickListener{
//            progressBar.visibility = View.VISIBLE
//
//            val status: String = "Found"
//            if(count == 1)
//            {
//                storageref.child(System.currentTimeMillis().toString()).putFile(uri5)
//                    .addOnSuccessListener { task ->
//                        task.metadata!!.reference!!.downloadUrl.addOnSuccessListener {
//                            val d = it.toString()
//                            downloadurl.add(d)
//                        }
//
//                    }
//            }
//            if(count == 2)
//            {
//                storageref.child(System.currentTimeMillis().toString()).putFile(uri5)
//                    .addOnSuccessListener { task ->
//                        task.metadata!!.reference!!.downloadUrl.addOnSuccessListener {
//                            val d = it.toString()
//                            downloadurl.add(d)
//                        }
//
//                    }
//                storageref.child(System.currentTimeMillis().toString()).putFile(uri1)
//                    .addOnSuccessListener { task ->
//                        task.metadata!!.reference!!.downloadUrl.addOnSuccessListener {
//                            val d = it.toString()
//                            downloadurl.add(d)
//                        }
//
//                    }
//            }
//            if(count == 3)
//            {
//                storageref.child(System.currentTimeMillis().toString()).putFile(uri5)
//                    .addOnSuccessListener { task ->
//                        task.metadata!!.reference!!.downloadUrl.addOnSuccessListener {
//                            val d = it.toString()
//                            downloadurl.add(d)
//                        }
//
//                    }
//                storageref.child(System.currentTimeMillis().toString()).putFile(uri1)
//                    .addOnSuccessListener { task ->
//                        task.metadata!!.reference!!.downloadUrl.addOnSuccessListener {
//                            val d = it.toString()
//                            downloadurl.add(d)
//                        }
//
//                    }
//                storageref.child(System.currentTimeMillis().toString()).putFile(uri2)
//                    .addOnSuccessListener { task ->
//                        task.metadata!!.reference!!.downloadUrl.addOnSuccessListener {
//                            val d = it.toString()
//                            downloadurl.add(d)
//                        }
//
//                    }
//            }
//            if(count == 4)
//            {
//                storageref.child(System.currentTimeMillis().toString()).putFile(uri5)
//                    .addOnSuccessListener { task ->
//                        task.metadata!!.reference!!.downloadUrl.addOnSuccessListener {
//                            val d = it.toString()
//                            downloadurl.add(d)
//                        }
//
//                    }
//                storageref.child(System.currentTimeMillis().toString()).putFile(uri1)
//                    .addOnSuccessListener { task ->
//                        task.metadata!!.reference!!.downloadUrl.addOnSuccessListener {
//                            val d = it.toString()
//                            downloadurl.add(d)
//                        }
//
//                    }
//                storageref.child(System.currentTimeMillis().toString()).putFile(uri2)
//                    .addOnSuccessListener { task ->
//                        task.metadata!!.reference!!.downloadUrl.addOnSuccessListener {
//                            val d = it.toString()
//                            downloadurl.add(d)
//                        }
//
//                    }
//                storageref.child(System.currentTimeMillis().toString()).putFile(uri3)
//                    .addOnSuccessListener { task ->
//                        task.metadata!!.reference!!.downloadUrl.addOnSuccessListener {
//                            val d = it.toString()
//                            downloadurl.add(d)
//                        }
//
//                    }
//            }
//            if(count == 5)
//            {
//                storageref.child(System.currentTimeMillis().toString()).putFile(uri5)
//                    .addOnSuccessListener { task ->
//                        task.metadata!!.reference!!.downloadUrl.addOnSuccessListener {
//                            val d = it.toString()
//                            downloadurl.add(d)
//                        }
//
//                    }
//                storageref.child(System.currentTimeMillis().toString()).putFile(uri1)
//                    .addOnSuccessListener { task ->
//                        task.metadata!!.reference!!.downloadUrl.addOnSuccessListener {
//                            val d = it.toString()
//                            downloadurl.add(d)
//                        }
//
//                    }
//                storageref.child(System.currentTimeMillis().toString()).putFile(uri2)
//                    .addOnSuccessListener { task ->
//                        task.metadata!!.reference!!.downloadUrl.addOnSuccessListener {
//                            val d = it.toString()
//                            downloadurl.add(d)
//                        }
//
//                    }
//                storageref.child(System.currentTimeMillis().toString()).putFile(uri3)
//                    .addOnSuccessListener { task ->
//                        task.metadata!!.reference!!.downloadUrl.addOnSuccessListener {
//                            val d = it.toString()
//                            downloadurl.add(d)
//                        }
//
//                    }
//                storageref.child(System.currentTimeMillis().toString()).putFile(uri4)
//                    .addOnSuccessListener { task ->
//                        task.metadata!!.reference!!.downloadUrl.addOnSuccessListener {
//                            val d = it.toString()
//                            downloadurl.add(d)
//                        }
//
//                    }
//            }
//
//            val detailsOfPost = hashMapOf(
//                "name" to currentUserName,
//                "phone" to currentUserPhone,
//                "status" to status,
//                "uid" to currentUserUid,
//                "downloadurl" to downloadurl
//            )
//
//            db.collection("ItemsList").add(detailsOfPost)
//                .addOnSuccessListener {
//                    progressBar.visibility = View.GONE
//                    Toast.makeText(this, "Item Posted", Toast.LENGTH_LONG).show()
//                }
//                .addOnFailureListener{
//                    progressBar.visibility = View.GONE
//                    Toast.makeText(this, "Something Went Wrong", Toast.LENGTH_LONG).show()
//                }
//
//        }
//
//
//        postLostBtn.setOnClickListener{
//            progressBar.visibility = View.VISIBLE
//            val status = "Lost"
//
//            if(count == 1)
//            {
//                storageref.child(System.currentTimeMillis().toString()).putFile(uri5)
//                    .addOnSuccessListener { task ->
//                        task.metadata!!.reference!!.downloadUrl.addOnSuccessListener {
//                            val d = it.toString()
//                            downloadurl.add(d)
//                        }
//
//                    }
//            }
//            if(count == 2)
//            {
//                storageref.child(System.currentTimeMillis().toString()).putFile(uri5)
//                    .addOnSuccessListener { task ->
//                        task.metadata!!.reference!!.downloadUrl.addOnSuccessListener {
//                            val d = it.toString()
//                            downloadurl.add(d)
//                        }
//
//                    }
//                storageref.child(System.currentTimeMillis().toString()).putFile(uri1)
//                    .addOnSuccessListener { task ->
//                        task.metadata!!.reference!!.downloadUrl.addOnSuccessListener {
//                            val d = it.toString()
//                            downloadurl.add(d)
//                        }
//
//                    }
//            }
//            if(count == 3)
//            {
//                storageref.child(System.currentTimeMillis().toString()).putFile(uri5)
//                    .addOnSuccessListener { task ->
//                        task.metadata!!.reference!!.downloadUrl.addOnSuccessListener {
//                            val d = it.toString()
//                            downloadurl.add(d)
//                        }
//
//                    }
//                storageref.child(System.currentTimeMillis().toString()).putFile(uri1)
//                    .addOnSuccessListener { task ->
//                        task.metadata!!.reference!!.downloadUrl.addOnSuccessListener {
//                            val d = it.toString()
//                            downloadurl.add(d)
//                        }
//
//                    }
//                storageref.child(System.currentTimeMillis().toString()).putFile(uri2)
//                    .addOnSuccessListener { task ->
//                        task.metadata!!.reference!!.downloadUrl.addOnSuccessListener {
//                            val d = it.toString()
//                            downloadurl.add(d)
//                        }
//
//                    }
//            }
//            if(count == 4)
//            {
//                storageref.child(System.currentTimeMillis().toString()).putFile(uri5)
//                    .addOnSuccessListener { task ->
//                        task.metadata!!.reference!!.downloadUrl.addOnSuccessListener {
//                            val d = it.toString()
//                            downloadurl.add(d)
//                        }
//
//                    }
//                storageref.child(System.currentTimeMillis().toString()).putFile(uri1)
//                    .addOnSuccessListener { task ->
//                        task.metadata!!.reference!!.downloadUrl.addOnSuccessListener {
//                            val d = it.toString()
//                            downloadurl.add(d)
//                        }
//
//                    }
//                storageref.child(System.currentTimeMillis().toString()).putFile(uri2)
//                    .addOnSuccessListener { task ->
//                        task.metadata!!.reference!!.downloadUrl.addOnSuccessListener {
//                            val d = it.toString()
//                            downloadurl.add(d)
//                        }
//
//                    }
//                storageref.child(System.currentTimeMillis().toString()).putFile(uri3)
//                    .addOnSuccessListener { task ->
//                        task.metadata!!.reference!!.downloadUrl.addOnSuccessListener {
//                            val d = it.toString()
//                            downloadurl.add(d)
//                        }
//
//                    }
//            }
//            if(count == 5)
//            {
//                storageref.child(System.currentTimeMillis().toString()).putFile(uri5)
//                    .addOnSuccessListener { task ->
//                        task.metadata!!.reference!!.downloadUrl.addOnSuccessListener {
//                            val d = it.toString()
//                            downloadurl.add(d)
//                        }
//
//                    }
//                storageref.child(System.currentTimeMillis().toString()).putFile(uri1)
//                    .addOnSuccessListener { task ->
//                        task.metadata!!.reference!!.downloadUrl.addOnSuccessListener {
//                            val d = it.toString()
//                            downloadurl.add(d)
//                        }
//
//                    }
//                storageref.child(System.currentTimeMillis().toString()).putFile(uri2)
//                    .addOnSuccessListener { task ->
//                        task.metadata!!.reference!!.downloadUrl.addOnSuccessListener {
//                            val d = it.toString()
//                            downloadurl.add(d)
//                        }
//
//                    }
//                storageref.child(System.currentTimeMillis().toString()).putFile(uri3)
//                    .addOnSuccessListener { task ->
//                        task.metadata!!.reference!!.downloadUrl.addOnSuccessListener {
//                            val d = it.toString()
//                            downloadurl.add(d)
//                        }
//
//                    }
//                storageref.child(System.currentTimeMillis().toString()).putFile(uri4)
//                    .addOnSuccessListener { task ->
//                        task.metadata!!.reference!!.downloadUrl.addOnSuccessListener {
//                            val d = it.toString()
//                            downloadurl.add(d)
//                        }
//
//                    }
//            }
//
//            val detailsOfPost = hashMapOf(
//                "name" to currentUserName,
//                "phone" to currentUserPhone,
//                "status" to status,
//                "uid" to currentUserUid,
//                "downloadurl" to downloadurl
//            )
//
//            db.collection("ItemsList").add(detailsOfPost)
//                .addOnSuccessListener {
//                    progressBar.visibility = View.GONE
//                    Toast.makeText(this, "Item Posted", Toast.LENGTH_LONG).show()
//                }
//                .addOnFailureListener{
//                    progressBar.visibility = View.GONE
//                    Toast.makeText(this, "Something Went Wrong", Toast.LENGTH_LONG).show()
//                }
//        }
    }
}