package com.erickcapilla.dcyourself

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.content.Intent
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        Thread.sleep(1000)

        setTheme(R.style.Theme_DCYour)

        val user = Firebase.auth.currentUser
        if (user != null) {
            val change = Intent(this, Home::class.java)
            startActivity(change)
        }

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val signup = findViewById<Button>(R.id.signup)
        val login = findViewById<Button>(R.id.signUp)

        signup.setOnClickListener {
            val change = Intent(this, SignUp::class.java)
            startActivity(change)
        }

        login.setOnClickListener {
            val change = Intent(this, Login::class.java)
            startActivity(change)
        }
    }
}