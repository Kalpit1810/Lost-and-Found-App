package com.example.lostandfound

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class LandingPageActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_landing_page)

        val updatePasswordBtn = findViewById<Button>(R.id.Update_pass_btn)
        val myPostsBtn = findViewById<Button>(R.id.my_posts_btn)
        val checkItemsBtn = findViewById<Button>(R.id.check_item_btn)
        val postItemBtn = findViewById<Button>(R.id.post_item_btn)

        updatePasswordBtn.setOnClickListener{
            val intent = Intent(this, UpdatePasswordActivity::class.java)
            startActivity(intent)
        }

        myPostsBtn.setOnClickListener{
            val intent = Intent(this, UpdatePasswordActivity::class.java)
            startActivity(intent)
        }

        checkItemsBtn.setOnClickListener{
            val intent = Intent(this, CheckItemActivity::class.java)
            startActivity(intent)
        }

        postItemBtn.setOnClickListener{
            val intent = Intent(this, PostItemActivity::class.java)
            startActivity(intent)
        }
    }
}