package com.erickcapilla.dcyourself

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.erickcapilla.dcyourself.model.UIModel
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

        val uiModel = UIModel()

        val login = findViewById<Button>(R.id.logIn)
        login.setOnClickListener {
            val email: String
            val password: String

            if(uiModel.isEditEmpty(listOf(editEmail, editPassword))) {
                uiModel.showToast(applicationContext, "Ingresa todos los datos que se solicitan")
                return@setOnClickListener
            } else {
                email = editEmail.text.toString()
                password = editPassword.text.toString()
            }

            if(!uiModel.isEmailValid(email)) {
                uiModel.showToast(applicationContext, "Email no valido")
                return@setOnClickListener
            }

            auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this) {
                if(it.isSuccessful) {
                    val change = Intent(this, Home::class.java)
                    startActivity(change)
                } else {
                    uiModel.showToast(applicationContext, "Se ha producido un error. Vuelve a intentarlo")
                }
            }
        }

        val textForgot = findViewById<TextView>(R.id.textForgot)
        textForgot.setOnClickListener {
            val change = Intent(this, RecoverPassword::class.java)
            startActivity(change)
        }

        val goBack = findViewById<Button>(R.id.go_back)
        goBack.setOnClickListener{
            val change = Intent(this, MainActivity::class.java)
            startActivity(change)
        }
    }
    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        AlertDialog.Builder(this@Login)
            .setMessage("¿Salir de la aplicación?")
            .setCancelable(false)
            .setPositiveButton("Si") { dialog, whichButton ->
                finishAffinity() //Sale de la aplicación.
            }
            .setNegativeButton("Cancelar") { dialog, whichButton ->

            }
            .show()
    }
}