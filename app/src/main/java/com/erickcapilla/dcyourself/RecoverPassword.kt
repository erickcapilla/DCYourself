package com.erickcapilla.dcyourself

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import com.erickcapilla.dcyourself.util.UIUtils
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class RecoverPassword : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recover_password)


        auth = Firebase.auth
        val uiModel = UIUtils()

        val editEmail = findViewById<EditText>(R.id.editEmail)
        val progressBar = findViewById<ProgressBar>(R.id.progressBar)
        val progressTitle = findViewById<TextView>(R.id.progressTitle)
        val goBack = findViewById<Button>(R.id.go_back)

        val sendEmail = findViewById<Button>(R.id.next)
        sendEmail.setOnClickListener {
            if(uiModel.isEditEmpty(listOf(editEmail))) {
                uiModel.showToast(applicationContext, "Ingresa tu correo")
                return@setOnClickListener
            }

            val email = editEmail.text.toString()

            if(!uiModel.isEmailValid(email)) {
                uiModel.showToast(applicationContext, "Email no valido")
                return@setOnClickListener
            }

            progressTitle.visibility = View.VISIBLE
            progressBar.visibility = View.VISIBLE
            sendEmail.isEnabled = false
            sendEmail.setBackgroundResource(R.drawable.backgroun_button_unenable)
            goBack.isEnabled = false
            goBack.setBackgroundResource(R.drawable.backgroun_button_unenable)

            auth.sendPasswordResetEmail(email)
                .addOnCompleteListener {
                    if(it.isSuccessful) {
                        uiModel.showToast(this, "Email enviado. ¡Revisa tu correo!")
                        editEmail.setText("")
                        progressBar.visibility = View.GONE
                        progressTitle.visibility = View.GONE
                        sendEmail.isEnabled = true
                        sendEmail.setBackgroundResource(R.drawable.background_button_primary)
                        goBack.isEnabled = true
                        goBack.setBackgroundResource(R.drawable.background_button_secondary)
                    } else {
                        uiModel.showToast(this, "Hubo un problema")
                        editEmail.setText("")
                        progressBar.visibility = View.GONE
                        progressTitle.visibility = View.GONE
                        sendEmail.isEnabled = true
                        sendEmail.setBackgroundResource(R.drawable.background_button_primary)
                        goBack.isEnabled = true
                        goBack.setBackgroundResource(R.drawable.background_button_secondary)
                    }
                }
        }

        goBack.setOnClickListener{
            finish()
        }
    }

    private fun goMain() {
        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(intent)
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        AlertDialog.Builder(this@RecoverPassword)
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