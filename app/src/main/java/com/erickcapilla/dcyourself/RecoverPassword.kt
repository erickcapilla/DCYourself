package com.erickcapilla.dcyourself

import android.content.ContentValues
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.ActionCodeSettings
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class RecoverPassword : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recover_password)


        auth = Firebase.auth

        val editEmail = findViewById<EditText>(R.id.editEmail)

        val sendEmail = findViewById<Button>(R.id.next)
        sendEmail.setOnClickListener {
            val email = editEmail.text.toString()
            if(!editEmpty(editEmail)) {
                /*
                val user = auth.currentUser!!

                val url = "http://dcyourself.firebaseapp.com/verify?uid=" + user.uid
                val actionCodeSettings = ActionCodeSettings.newBuilder()
                    .setUrl(url).setDynamicLinkDomain(url)
                    .setAndroidPackageName("com.erickcapilla.dcyourself", false, null)
                    .build()
                 */
                auth!!.sendPasswordResetEmail(email)
                    .addOnCompleteListener {
                        if(it.isSuccessful) {
                            Toast.makeText(this, "Email enviado. ¡Revisa tu correo!. Sesión finalizada", Toast.LENGTH_SHORT).show()

                            goMain()
                        } else {
                            Toast.makeText(this, "Hubo un problema", Toast.LENGTH_SHORT).show()
                        }
                    }
            } else {
                Toast.makeText(applicationContext, "Ingresa tu correo", Toast.LENGTH_SHORT).show()
            }
        }

        val goBack = findViewById<Button>(R.id.go_back)
        goBack.setOnClickListener{
            finish()
        }
    }

    private fun editEmpty(edit: EditText): Boolean {
        return edit.text.toString().trim().isEmpty()
    }

    private fun goMain() {
        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(intent)
    }
}