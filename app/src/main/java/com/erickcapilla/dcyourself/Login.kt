package com.erickcapilla.dcyourself

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class Login : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        auth = Firebase.auth

        val editEmail = findViewById<EditText>(R.id.editEmail)
        val editPassword = findViewById<EditText>(R.id.editPassword)


        val login = findViewById<Button>(R.id.logIn)
        login.setOnClickListener {
            if(!editEmpty(editEmail) || !editEmpty(editPassword)) {
                auth.signInWithEmailAndPassword(editEmail.text.toString(),
                    editPassword.text.toString()).addOnCompleteListener(this) {
                    if(it.isSuccessful) {
                        val change = Intent(this, Home::class.java)
                        startActivity(change)
                    } else {
                        Toast.makeText(applicationContext, "Se ha producido un error, Vuelve a intentarlo",
                            Toast.LENGTH_SHORT).show()
                    }
                }
            } else {
                Toast.makeText(applicationContext, "Ingresa todos los datos que se solicitan", Toast.LENGTH_SHORT).show()
            }
        }

        val textForgot = findViewById<TextView>(R.id.textForgot)
        textForgot.setOnClickListener {
            val change = Intent(this, RecoverPassword::class.java)
            startActivity(change)
        }

        val goBack = findViewById<Button>(R.id.go_back)
        goBack.setOnClickListener{
            finish()
        }
    }

    private fun editEmpty(edit: EditText): Boolean {
        return edit.text.toString().trim().isEmpty()
    }
}