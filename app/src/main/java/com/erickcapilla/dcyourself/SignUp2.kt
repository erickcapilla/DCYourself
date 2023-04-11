package com.erickcapilla.dcyourself

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class SignUp2 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up2)

        val goBack = findViewById<Button>(R.id.go_back)
        goBack.setOnClickListener{
            finish()
        }
    }
}