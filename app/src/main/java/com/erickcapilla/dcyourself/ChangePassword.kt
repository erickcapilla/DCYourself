package com.erickcapilla.dcyourself

import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.firebase.auth.EmailAuthCredential
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class ChangePassword : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_change_password)

        val user = Firebase.auth.currentUser
        auth = Firebase.auth

        val editPassword = findViewById<EditText>(R.id.editPassword)
        val editNewPassword = findViewById<EditText>(R.id.editNewPassword)
        val editConfirmPassword = findViewById<EditText>(R.id.editConfirmPassword)

        val upDateButton = findViewById<Button>(R.id.update)
        upDateButton.setOnClickListener {
            if(!editEmpty(editPassword) || !editEmpty(editNewPassword) || !editEmpty(editConfirmPassword)) {
                val email = user?.email
                Log.d(TAG, email.toString())
                val credential = EmailAuthProvider.getCredential(email.toString(), editPassword.text.toString())

                user!!.reauthenticate(credential)
                    .addOnCompleteListener(this) {
                        if(it.isSuccessful) {
                            /*
                            * Se modifica la contraseña del usuario
                            * @param password Nueva contraseña del usuario
                            * */
                            user!!.updatePassword(editNewPassword.text.toString())
                                .addOnCompleteListener { task ->
                                    if(task.isSuccessful) {
                                        editPassword.setText("");
                                        editNewPassword.setText("")
                                        editConfirmPassword.setText("")
                                        Toast.makeText(applicationContext, "Contraseña actualizada", Toast.LENGTH_SHORT).show()
                                    }
                                }
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

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        AlertDialog.Builder(this@ChangePassword)
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