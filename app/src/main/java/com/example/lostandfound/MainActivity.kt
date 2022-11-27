package com.example.lostandfound

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {
    private lateinit var auth : FirebaseAuth
    private val emailPattern= "[a-zA-Z0-9_-]+@iitp.ac.in"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        auth = FirebaseAuth.getInstance()

        val signInEmail: EditText = findViewById(R.id.SignInEmail_ID)
        val signInPassword: EditText = findViewById(R.id.SignInPassword)
        val signInPasswordLayout: TextInputLayout = findViewById(R.id.SignInPasswordLayout)
        val signInBtn : Button =   findViewById(R.id.SignInButton)
        val signInProgressbar : ProgressBar = findViewById(R.id.SignInProgressbar)

        auth = FirebaseAuth.getInstance()
        if(auth.currentUser==null){
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }

        val signUpText: TextView = findViewById(R.id.SignUpText)
        signUpText.setOnClickListener {
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }

        val forgetpasstext: TextView = findViewById(R.id.ForgetPassText)
        forgetpasstext.setOnClickListener {
            val intent = Intent(this, ForgetPasswordActivity::class.java)
            startActivity(intent)
        }

        signInBtn.setOnClickListener{
            signInProgressbar.visibility = View.VISIBLE
            signInPasswordLayout.isPasswordVisibilityToggleEnabled = true

            val email = signInEmail.text.toString()
            val password =  signInPassword.text.toString()

            if(email.isEmpty() || password.isEmpty()){
                if(email.isEmpty()){
                    signInEmail.error = "Enter your email address"
                }
                if(password.isEmpty()){
                    signInPassword.error = "Enter your password"
                    signInPasswordLayout.isPasswordVisibilityToggleEnabled= false
                }
                signInProgressbar.visibility = View.GONE
                Toast.makeText( this, "Enter valid details", Toast.LENGTH_SHORT).show()
            }else if(!email.matches(emailPattern.toRegex())){
                signInProgressbar.visibility = View.GONE
                signInEmail.error = "Enter valid email address"
                Toast.makeText( this,  "Enter valid email address", Toast.LENGTH_SHORT).show()
            }else if(password.length < 6) {
                signInPasswordLayout.isPasswordVisibilityToggleEnabled = false
                signInProgressbar.visibility = View.GONE
                signInPassword.error = "Enter password more than 6 characters"
                Toast.makeText(this, "Enter password more than 6 characters", Toast.LENGTH_SHORT).show()
            }else {
                auth.signInWithEmailAndPassword(email, password).addOnCompleteListener{
                if(it.isSuccessful){
                    if(Firebase.auth.currentUser?.isEmailVerified == true)
                    {
                        val intent = Intent( this, LandingPageActivity::class.java)
                        startActivity(intent)
                    }
                    else
                    {
                        Toast.makeText(this, "Email is not Verified\n Please verify you Email first", Toast.LENGTH_LONG).show()
                        val intent = Intent( this, MainActivity::class.java)
                        startActivity(intent)
                    }

                }else{
                    Toast.makeText(this, "Something went wrong, try again", Toast.LENGTH_SHORT).show()
                    signInProgressbar.visibility= View.GONE
                }
            }

            }
        }

    }
}