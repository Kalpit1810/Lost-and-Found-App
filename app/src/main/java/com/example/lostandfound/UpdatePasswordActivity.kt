package com.example.lostandfound

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase

class UpdatePasswordActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase

    @SuppressLint("CutPasteId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_password)

        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()

        val currentUser = auth.currentUser
        val userEmail = currentUser?.email.toString()
        val updatePassBtn = findViewById<Button>(R.id.UpdatePassButton)
        val updatePassPrgressbar = findViewById<ProgressBar>(R.id.UpdatePassProgressbar)

        updatePassBtn.setOnClickListener {

            updatePassPrgressbar.visibility = View.VISIBLE

            val currentPassword = findViewById<EditText>(R.id.UpdatePassCurrentPassword).text.toString()
            val newPassword = findViewById<EditText>(R.id.UpdatePassPassword).text.toString()
            val confirmPassword = findViewById<EditText>(R.id.UpdatePassConfirmPassword).text.toString()

            if (currentPassword.isEmpty() || newPassword.isEmpty() || confirmPassword.isEmpty()) {
                if (currentPassword.isEmpty()) {
                    Toast.makeText(this, "Enter Current Password", Toast.LENGTH_LONG).show()
                    findViewById<EditText>(R.id.UpdatePassCurrentPassword).error = "Enter Current Password"
                }
                if (newPassword.isEmpty()) {
                    Toast.makeText(this, "Enter New Password", Toast.LENGTH_LONG).show()
                    findViewById<EditText>(R.id.UpdatePassPassword).error = "Enter New Password"
                }
                if (currentPassword.isEmpty()) {
                    Toast.makeText(this, "Confirm Password", Toast.LENGTH_LONG).show()
                    findViewById<EditText>(R.id.UpdatePassConfirmPassword).error ="Confirm Password"
                }
                updatePassPrgressbar.visibility = View.GONE
            }
            else if (newPassword.length < 6) {
                updatePassPrgressbar.visibility = View.GONE
                findViewById<EditText>(R.id.UpdatePassPassword).error = "Password less than 6 characters"
                Toast.makeText(this, "Password too small", Toast.LENGTH_SHORT).show()
            }
            else if (confirmPassword != newPassword) {
                updatePassPrgressbar.visibility = View.GONE
                findViewById<EditText>(R.id.UpdatePassConfirmPassword).error = "Password not matched, try again"
                Toast.makeText(this, "$confirmPassword\n$newPassword", Toast.LENGTH_SHORT).show()
            }
            else
            {
                val credential = EmailAuthProvider
                    .getCredential(userEmail, currentPassword)
                currentUser?.reauthenticate(credential)?.addOnCompleteListener {task ->
                    if(task.isSuccessful) {
                        currentUser!!.updatePassword(newPassword).addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                updatePassPrgressbar.visibility = View.GONE
                                Toast.makeText(this, "Password Updated successfully", Toast.LENGTH_LONG).show()
                            } else {
                                updatePassPrgressbar.visibility = View.GONE
                                Toast.makeText(this, "Something went wrong\nTry again", Toast.LENGTH_LONG).show()
                            }
                        }
                    }
                    else{
                        updatePassPrgressbar.visibility = View.GONE
                        findViewById<EditText>(R.id.UpdatePassCurrentPassword).error = "Enter correct password"
                        Toast.makeText(this,"Authenthication Failed", Toast.LENGTH_LONG).show()
                    }
                }

            }

        }
    }
}

