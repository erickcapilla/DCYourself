package com.erickcapilla.dcyourself

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast

class SignUp : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        val editName = findViewById<EditText>(R.id.editName)
        val editLastName = findViewById<EditText>(R.id.editLastName)
        val editLastName2 = findViewById<EditText>(R.id.editLastName2)

        val next = findViewById<Button>(R.id.next)
        next.setOnClickListener {
            if(!editEmpty(editName) || !editEmpty(editLastName) || !editEmpty(editLastName2)) {
                val change = Intent(this, SignUp2::class.java)
                change.putExtra("name", editName.text.toString())
                change.putExtra("lastName", editLastName.text.toString())
                change.putExtra("lastName2", editLastName2.text.toString())
                startActivity(change)
            } else {
                Toast.makeText(applicationContext, "Ingresa todos los datos que se solicitan", Toast.LENGTH_SHORT).show()
            }

        }

        val goBack =findViewById<Button>(R.id.go_back)
        goBack.setOnClickListener{
            finish()
        }
    }

    private fun editEmpty(edit: EditText): Boolean {
        return edit.text.toString().trim().isEmpty()
    }
}