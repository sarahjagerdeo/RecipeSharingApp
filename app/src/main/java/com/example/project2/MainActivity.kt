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
import com.google.firebase.auth.FirebaseUser

class MainActivity : AppCompatActivity() {

    private lateinit var username: EditText
    private lateinit var password: EditText
    private lateinit var loginButton: Button
    private lateinit var signUpBtn: Button
    private lateinit var firebaseAuth: FirebaseAuth

    var userBool = false
    var passBool = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        username = findViewById(R.id.username)
        password = findViewById(R.id.password)
        loginButton = findViewById(R.id.loginButton)
        signUpBtn = findViewById(R.id.signUpBtn)
        firebaseAuth = FirebaseAuth.getInstance()

        username.addTextChangedListener(userTextWatcher)
        password.addTextChangedListener(passTextWatcher)

        signUpBtn.setOnClickListener {
            val inputtedUserName = username.text.toString().trim()
            val inputtedPassword = password.text.toString().trim()

            firebaseAuth.createUserWithEmailAndPassword(inputtedUserName, inputtedPassword)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val user = firebaseAuth.currentUser
                        Toast.makeText(this, "Created user: ${user?.email}", Toast.LENGTH_LONG).show()
                    } else {
                        Toast.makeText(this, "Failed to create account: ${task.exception?.message}", Toast.LENGTH_LONG).show()
                    }
                }
        }

        loginButton.setOnClickListener {
            val inputtedUserName = username.text.toString().trim()
            val inputtedPassword = password.text.toString().trim()

            firebaseAuth.signInWithEmailAndPassword(inputtedUserName, inputtedPassword)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val user = firebaseAuth.currentUser
                        Toast.makeText(this, "Welcome ${user?.email}!", Toast.LENGTH_LONG).show()

                        val screen2Intent = Intent(this@MainActivity, MainActivity2::class.java)
                        screen2Intent.putExtra("username", inputtedUserName)
                        startActivity(screen2Intent)
                    } else {
                        Toast.makeText(this, "Login failed: ${task.exception?.message}", Toast.LENGTH_LONG).show()
                    }
                }
        }
    }

    private val userTextWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        override fun afterTextChanged(s: Editable?) {
            userBool = true
            if (passBool) {
                loginButton.isEnabled = true
            }
        }
    }

    private val passTextWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        override fun afterTextChanged(s: Editable?) {
            passBool = true
            if (userBool) {
                loginButton.isEnabled = true
            }
        }
    }
}
