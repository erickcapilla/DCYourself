package com.erickcapilla.dcyourself

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class Login : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val login = findViewById<Button>(R.id.signUp)

        login.setOnClickListener {
            val change = Intent(this, SignUp::class.java)
            startActivity(change)
        }

        val goBack =findViewById<Button>(R.id.go_back)
        goBack.setOnClickListener{
            finish()
        }
    }
}