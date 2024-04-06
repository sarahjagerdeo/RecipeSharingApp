package com.example.project2

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class MainActivity2 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)
        val recipeFeedButton: Button = findViewById(R.id.recipeFeedButton)
        recipeFeedButton.setOnClickListener {
            val intent = Intent(this, RecipeFeedActivity::class.java)
            startActivity(intent)
        }
    }
}
