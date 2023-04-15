package com.erickcapilla.dcyourself

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class Home : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        auth = Firebase.auth

        val logoutButton = findViewById<ImageButton>(R.id.logOut)
        logoutButton.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            onBackPressed()
        }

        val changePassword = findViewById<ImageButton>(R.id.changePassword)
        changePassword.setOnClickListener {
            val change = Intent(this, ChangePassword::class.java)
            startActivity(change)
        }
    }
}