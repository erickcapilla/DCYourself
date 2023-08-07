package com.erickcapilla.dcyourself

import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.erickcapilla.dcyourself.util.UIUtils
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class Login : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        auth = Firebase.auth

        val progressBar = findViewById<ProgressBar>(R.id.progressBar)
        val progressTitle = findViewById<TextView>(R.id.progressTitle)
        val editEmail = findViewById<EditText>(R.id.editEmail)
        val editPassword = findViewById<EditText>(R.id.editPassword)
        val errorEmail = findViewById<TextView>(R.id.errorEmail)

        val eyeButton = findViewById<ImageButton>(R.id.eyeButton)
        var visibility = false

        eyeButton.setOnClickListener {
            if(visibility) {
                visibility = false
                editPassword.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
                eyeButton.setImageResource(R.drawable.baseline_visibility_24)
            } else {
                visibility = true
                editPassword.inputType = InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
                eyeButton.setImageResource(R.drawable.baseline_visibility_off_24)
            }
        }

        val uiModel = UIUtils()

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
                errorEmail.visibility = View.VISIBLE
                return@setOnClickListener
            }

            progressTitle.visibility = View.VISIBLE
            progressBar.visibility = View.VISIBLE
            login.isEnabled = false
            login.setBackgroundResource(R.drawable.background_button_unenable)

            auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this) {
                if(it.isSuccessful) {
                    progressBar.visibility = View.GONE
                    progressTitle.visibility = View.GONE
                    val change = Intent(this, Home::class.java)
                    startActivity(change)
                } else {
                    login.isEnabled = true
                    progressBar.visibility = View.GONE
                    progressTitle.visibility = View.GONE
                    login.setBackgroundResource(R.style.ButtonPrimary)
                    uiModel.showToast(applicationContext, "El usuario no existe. Revisa tu conexión")
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