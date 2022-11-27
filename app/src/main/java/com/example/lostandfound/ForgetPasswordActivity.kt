package com.example.lostandfound

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase

class ForgetPasswordActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase
    private val emailPattern="[a-zA-Z0-9._-]+@iitp.ac.in"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forget_password)


        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()

        val forgetPassEmail: EditText = findViewById(R.id.ForgetPassEmail_ID)
        val updatePassbtn: Button = findViewById(R.id.ForgotPassSendLinkButton)
        val forgetPassProgressbar: ProgressBar = findViewById(R.id.ForgetPassProgressbar)

        updatePassbtn.setOnClickListener{

            val email = forgetPassEmail.text.toString()

            forgetPassProgressbar.visibility = View.VISIBLE

            if(email.isEmpty() ){
                forgetPassEmail.error = "Enter your email address"
                forgetPassProgressbar.visibility = View.GONE
                Toast.makeText( this, "Enter valid details", Toast.LENGTH_SHORT).show()
            }else if(!email.matches(emailPattern.toRegex())){
                forgetPassProgressbar.visibility = View.GONE
                forgetPassEmail.error= "Enter valid email address"
                Toast.makeText(  this,  "Enter valid email address", Toast.LENGTH_SHORT).show()
            }else {
                auth.sendPasswordResetEmail(email)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            forgetPassProgressbar.visibility = View.GONE
                            Toast.makeText(this, "Password reset link sent to Email", Toast.LENGTH_SHORT).show()
                            val intent = Intent( this, MainActivity::class.java)
                            startActivity(intent)
                        }
                        else{
                            forgetPassProgressbar.visibility = View.GONE
                            Toast.makeText(this, "Something went wrong.\nTry again.", Toast.LENGTH_SHORT).show()
                            val intent = Intent( this, MainActivity::class.java)
                            startActivity(intent)
                        }
                    }

            }



        }

    }
}

