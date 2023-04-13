package com.erickcapilla.dcyourself

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.content.Intent

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        Thread.sleep(1500)

        setTheme(R.style.Theme_DCYour)

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