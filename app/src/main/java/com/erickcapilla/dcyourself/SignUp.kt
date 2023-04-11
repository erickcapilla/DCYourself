package com.erickcapilla.dcyourself

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class SignUp : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        val next = findViewById<Button>(R.id.next)

        next.setOnClickListener {
            val change = Intent(this, SignUp2::class.java)
            startActivity(change)
        }

        val goBack =findViewById<Button>(R.id.go_back)
        goBack.setOnClickListener{
            finish()
        }
    }
}