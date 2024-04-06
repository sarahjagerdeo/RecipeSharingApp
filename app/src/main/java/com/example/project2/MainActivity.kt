package com.example.project2

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import java.net.PasswordAuthentication

class MainActivity : AppCompatActivity() {

    private lateinit var username: EditText
    private lateinit var password: EditText
    private lateinit var loginButton: Button
    private lateinit var signUpBtn:Button
    private lateinit var firebaseAuth: FirebaseAuth

    var userBool = false
    var passBool = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        username = findViewById(R.id.username)
        password = findViewById(R.id.password)
        loginButton = findViewById(R.id.loginButton)
        signUpBtn=findViewById(R.id.signUpBtn)
        firebaseAuth= FirebaseAuth.getInstance()
        username.addTextChangedListener(userTextWatcher)
        password.addTextChangedListener(passTextWatcher)

        signUpBtn.setOnClickListener {
            val inputtedUserName:String=username.text.toString().trim()
            val inputtedPassword:String=password.text.toString().trim()
            firebaseAuth.createUserWithEmailAndPassword(inputtedUserName,inputtedPassword)
                .addOnCompleteListener {
                    if(it.isSuccessful){
                        val user = firebaseAuth.currentUser
                        Toast.makeText(this, "Created user: ${user!!.email}",Toast.LENGTH_LONG).show()
                    } else {
                        val exception=it.exception
                        Toast.makeText(this, "Failed: $exception",Toast.LENGTH_LONG).show()
                    }
                }
        }

        loginButton.setOnClickListener{
            Toast.makeText(this@MainActivity, "Login button clicked", Toast.LENGTH_LONG).show()
            val screen2Intent = Intent(this@MainActivity, MainActivity2::class.java)
            val nameBug = username.text
            screen2Intent.putExtra("username", "$nameBug")
            Log.d("MainActivity", "$nameBug")
            startActivity(screen2Intent)
        }

    }
    private val userTextWatcher: TextWatcher = object: TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            Log.d("MainActivity","inside of before $s")
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            Log.d("MainActivity","inside of on text changed $count and start $start")
        }

        override fun afterTextChanged(s: Editable?) {
            userBool = true
            if (passBool == true){
                loginButton.isEnabled = true
            }
            Log.d("MainActivity","inside of afterTextChanged $s")
        }
    }
    private val passTextWatcher: TextWatcher = object: TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            Log.d("MainActivity","inside of before $s")
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            Log.d("MainActivity","inside of on text changed $count and start $start")
        }

        override fun afterTextChanged(s: Editable?) {
            passBool = true
            if (userBool == true){
                loginButton.isEnabled = true
            }
            Log.d("MainActivity","inside of afterTextChanged $s")
        }
    }
}