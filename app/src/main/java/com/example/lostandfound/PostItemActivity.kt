package com.example.lostandfound

import android.content.Intent
import android.net.Uri
import android.os.Build.VERSION_CODES.N
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract
import android.provider.MediaStore
import android.provider.MediaStore.Images.Media
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
import com.google.firebase.storage.ktx.storageMetadata
import org.checkerframework.checker.units.qual.m
import java.net.URI
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.ThreadLocalRandom.current
import java.util.jar.Attributes.Name


class PostItemActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase
    private lateinit var storage: FirebaseStorage
    private lateinit var uri5: Uri
    private lateinit var downloadurl: String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post_item)

        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()
        storage = Firebase.storage

        val db = Firebase.firestore
        val currentUser = auth.currentUser
        val currentUserUid = currentUser?.uid.toString()
        var currentUserEmail = ""
        val progressBar = findViewById<ProgressBar>(R.id.Post_Item_Progerss_Bar)
        val storageref = storage.reference
        val selectImagesBtn = findViewById<Button>(R.id.Post_Item_Select_Image_Button)
        val postFoundBtn = findViewById<Button>(R.id.Post_Found_Button)
        val postLostBtn = findViewById<Button>(R.id.Post_Lost_Button)
        var count = 0
        var currentUserName = ""
        var currentUserPhone = ""

        progressBar.visibility = View.VISIBLE

        database.reference.child("users").child(currentUserUid).get().addOnSuccessListener {
            currentUserName = it.child("name").value.toString()
            currentUserPhone = it.child("phone").value.toString()
            currentUserEmail = it.child("email").value.toString()
            findViewById<EditText>(R.id.Post_Item_Name).setText(currentUserName)
            findViewById<EditText>(R.id.Post_Item_Phone_No).setText(currentUserPhone)
            progressBar.visibility = View.GONE
        }

        selectImagesBtn.setOnClickListener(View.OnClickListener {
            val imageSelectIntent =
                Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(imageSelectIntent, 3)
        })

        val message = findViewById<EditText>(R.id.Post_Item_Message).text.toString()

        postFoundBtn.setOnClickListener {
            if(findViewById<EditText>(R.id.Post_Item_Phone_No).text.toString().isEmpty() || findViewById<EditText>(R.id.Post_Item_Name).text.toString().isEmpty() || findViewById<EditText>(R.id.Post_Item_Location).text.toString().isEmpty() || findViewById<EditText>(R.id.Post_Item_Message).text.toString().isEmpty())
            {
                Toast.makeText(this,"Please Fill all Areas", Toast.LENGTH_LONG).show()
            }
            else{
                progressBar.visibility = View.VISIBLE
                val message = findViewById<EditText>(R.id.Post_Item_Message).text.toString()
                uploadImage(currentUserName,currentUserPhone,"Found",currentUserUid,currentUserEmail,message,uri5)
            }

        }

        postLostBtn.setOnClickListener {
            if(findViewById<EditText>(R.id.Post_Item_Phone_No).text.toString().isEmpty() || findViewById<EditText>(R.id.Post_Item_Name).text.toString().isEmpty() || findViewById<EditText>(R.id.Post_Item_Location).text.toString().isEmpty() || findViewById<EditText>(R.id.Post_Item_Message).text.toString().isEmpty())
            {
                Toast.makeText(this,"Please Fill all Areas", Toast.LENGTH_LONG).show()
            }
            else
            {
                progressBar.visibility = View.VISIBLE
                val message = findViewById<EditText>(R.id.Post_Item_Message).text.toString()
                uploadImage(currentUserName, currentUserPhone, "Lost", currentUserUid,currentUserEmail,message,uri5)
            }
        }

    }


        override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
            super.onActivityResult(requestCode, resultCode, data)
            if (resultCode == RESULT_OK && data != null) {
                uri5 = data.data!!
                findViewById<ImageView>(R.id.image1).visibility = View.VISIBLE
                findViewById<ImageView>(R.id.image1).setImageURI(uri5)
            }
        }

        private fun uploadImage(cu:String,ph:String,s:String, em:String,ui:String,m:String,ur: Uri) {
            val formatter = SimpleDateFormat("yyyy_MM_DD_HH_mm_ss", Locale.getDefault())
            val now = Date()
            val filename = formatter.format(now)
            val storageReference = FirebaseStorage.getInstance().getReference("Images/$filename")
            storageReference.putFile(uri5)

            val detailsOfPost = hashMapOf(
                "uploadedBy" to cu,
                "phone" to ph,
                "status" to s,
                "uid" to ui,
                "Email" to em,
                "message" to m,
                "fileName" to filename,
            )

            Firebase.firestore.collection("ItemsList").add(detailsOfPost)
                .addOnSuccessListener {
                    Toast.makeText(this, "Item Posted", Toast.LENGTH_LONG).show()
                }
                .addOnFailureListener{
                    Toast.makeText(this, "Something Went Wrong", Toast.LENGTH_LONG).show()
                }

            findViewById<ProgressBar>(R.id.Post_Item_Progerss_Bar).visibility = View.GONE

        }

}