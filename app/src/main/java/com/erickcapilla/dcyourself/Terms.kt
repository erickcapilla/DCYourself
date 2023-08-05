package com.erickcapilla.dcyourself

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class Terms : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_terms)

        val goBack = findViewById<Button>(R.id.next)

        goBack.setOnClickListener {
            finish()
        }
    }
}