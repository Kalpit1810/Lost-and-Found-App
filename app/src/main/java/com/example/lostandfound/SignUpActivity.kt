package com.example.lostandfound

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase

class SignUpActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase
    private val emailPattern="[a-zA-Z0-9._-]+@iitp.ac.in"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()

        val signUpName: EditText = findViewById(R.id.SignUpName)
        val signUpRollNo: EditText = findViewById(R.id.SignUpRoll_No)
        val signUpEmail: EditText = findViewById(R.id.SignUpEmail_ID)
        val signUpPhone: EditText = findViewById(R.id.SignUpPhone_No)
        val signUpPassword: EditText = findViewById(R.id.SignUpPassword)
        val signUpCPassword: EditText = findViewById(R.id.SignUpConfirmPassword)
        val signUpPasswordLayout: TextInputLayout = findViewById(R.id.SignUpPasswordLayout)
        val signUpCPasswordLayout: TextInputLayout = findViewById(R.id.SignUpConfirmPasswordLayout)
        val signUpBtn: Button = findViewById(R.id.SignUpButton)
        val signUpProgressbar: ProgressBar = findViewById(R.id.SignUpProgressbar)
        val signUpText: TextView = findViewById(R.id.SignInText)

        signUpText.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        signUpBtn.setOnClickListener {

            val name = signUpName.text.toString()
            val roll_no = signUpRollNo.text.toString()
            val email = signUpEmail.text.toString()
            val phone = signUpPhone.text.toString()
            val password = signUpPassword.text.toString()
            val cPassword = signUpCPassword.text.toString()

            signUpProgressbar.visibility = View.VISIBLE
            signUpPasswordLayout.isPasswordVisibilityToggleEnabled = false
            signUpCPasswordLayout.isPasswordVisibilityToggleEnabled = false

            if(name.isEmpty() || roll_no.isEmpty() || email.isEmpty() || phone.isEmpty() || password.isEmpty() || cPassword.isEmpty()){
                if(name.isEmpty()){
                    signUpName.error = "Enter your Name"
                }
                if(roll_no.isEmpty()){
                    signUpPhone.error = "Enter your Roll number"
                }
                if(email.isEmpty()) {
                    signUpEmail.error = "Enter your Email address"
                }
                if(phone.isEmpty()){
                    signUpPhone.error = "Enter your phone number"
                }
                if(password.isEmpty()) {
                    signUpPassword.error = "Enter your password"
                }
                if (cPassword.isEmpty()) {
                    signUpCPassword.error = "Confirm your password"
                }
                Toast.makeText(this, "Enter valid details", Toast.LENGTH_SHORT).show()
                signUpProgressbar.visibility = View.GONE
            }else if(!email.matches(emailPattern.toRegex())){
                signUpProgressbar.visibility = View.GONE
                signUpEmail.error= "Enter valid email address"
                Toast.makeText(  this,  "Enter valid email address", Toast.LENGTH_SHORT).show()
            }else if(phone.length != 10){
                signUpProgressbar.visibility = View.GONE
                signUpPhone.error = "Enter valid phone number"
                Toast.makeText( this, "Enter valid phone number", Toast.LENGTH_SHORT).show()
            }else if(password.length <6){
                signUpPasswordLayout.isPasswordVisibilityToggleEnabled = false
                signUpProgressbar.visibility = View.GONE
                signUpPassword.error = "Enter password more than 6 characters"
                Toast.makeText(this, "Enter password more than 6 characters", Toast.LENGTH_SHORT).show()
            }else if(password != cPassword){
                signUpCPasswordLayout.isPasswordVisibilityToggleEnabled = false
                signUpProgressbar.visibility = View.GONE
                signUpCPassword.error = "Password not matched, try again"
                Toast.makeText(this, "Password not matched, try again", Toast.LENGTH_SHORT).show()
            }else{
                auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener{
                    if(it.isSuccessful){
                        val databaseRef = database.reference.child(  "users").child(auth.currentUser!!.uid)
                        val users: Users = Users (name,roll_no, email, password, phone, auth.currentUser!!.uid)
                        databaseRef.setValue(users).addOnCompleteListener {
                            if(it.isSuccessful){

                                Toast.makeText(this,"An verification Email is sent",Toast.LENGTH_LONG).show()
                                Firebase.auth.currentUser?.sendEmailVerification()?.addOnSuccessListener {
                                    val intent = Intent(  this, MainActivity::class.java)
                                    startActivity(intent)
                                }

                            }else{
                                Toast.makeText( this,"Something went wrong, try again1", Toast.LENGTH_SHORT).show()
                            }
                        }
                }else{
                    Toast.makeText(  this, "Something went wrong, try again", Toast.LENGTH_SHORT).show()
                }
                }
                signUpProgressbar.visibility = View.GONE
            }
        }
    }
}