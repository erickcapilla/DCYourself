package com.erickcapilla.dcyourself

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.InputType
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import com.erickcapilla.dcyourself.util.UIUtils
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class AccountInformation : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_account_information)

        val userEmail = findViewById<TextView>(R.id.email)
        val editEmail = findViewById<EditText>(R.id.editEmail)
        val editEmailConfirm = findViewById<EditText>(R.id.editEmailConfirm)
        val changePassword = findViewById<Button>(R.id.changePassword)
        val next = findViewById<Button>(R.id.next)
        val goBack = findViewById<Button>(R.id.goBack)

        val uiModel = UIUtils()
        var newEmail:String

        auth = Firebase.auth
        val db = Firebase.firestore
        val user = Firebase.auth.currentUser
        var email = ""
        user?.let {
            for (profile in it.providerData) {
                email = profile.email.toString()
            }
        }

        userEmail.text = email

        changePassword.setOnClickListener {
            val change = Intent(this, ChangePassword::class.java)
            startActivity(change)
        }

        next.setOnClickListener {
            if(uiModel.isEditEmpty(listOf(editEmail, editEmailConfirm))) {
                uiModel.showToast(applicationContext, "Ingresa todos los datos que se solicitan")
                return@setOnClickListener
            } else {
                newEmail = editEmail.text.toString()
            }

            if(!uiModel.isEmailValid(newEmail)) {
                uiModel.showToast(applicationContext, "Email no valido")
                return@setOnClickListener
            }

            if(newEmail != editEmailConfirm.text.toString()) {
                uiModel.showToast(applicationContext, "Las contraseñas deben coincidir")
                return@setOnClickListener
            }

            val editPassword = EditText(this)
            editPassword.hint = "Confirma con tu contraseña"
            editPassword.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
            AlertDialog.Builder(this)
                .setTitle("Cambiar correo electrónico")
                .setMessage("Ahora accederas a tu cuenta con este correo nuevo")
                .setView(editPassword)
                .setPositiveButton("Cambiar") { dialog, whichButton ->
                    if(uiModel.isEditEmpty(listOf(editPassword))) {
                        uiModel.showToast(applicationContext, "Ingresa todos los datos que se solicitan")
                        return@setPositiveButton
                    }
                    val pass = editPassword.text.toString()
                    val credential = EmailAuthProvider.getCredential(email, pass)
                    user!!.reauthenticate(credential)
                        .addOnCompleteListener(this) {
                            if (it.isSuccessful) {
                                user.updateEmail(newEmail)
                                    .addOnCompleteListener { task ->
                                        if (task.isSuccessful) {
                                            uiModel.showToast(applicationContext, "Correo electrónico actualizado")
                                        }
                                    }
                            } else {
                                uiModel.showToast(applicationContext, "Contraseña incorrecta")
                            }
                        }
                }
                .setNegativeButton("Cancelar") { dialog, whichButton -> }
                .create()
                .show()
        }

        goBack.setOnClickListener {
            finish()
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        AlertDialog.Builder(this@AccountInformation)
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