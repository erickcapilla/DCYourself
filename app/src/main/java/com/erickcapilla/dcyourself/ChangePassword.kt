package com.erickcapilla.dcyourself

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.InputType
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import com.erickcapilla.dcyourself.util.UIUtils
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
        val progressBar = findViewById<ProgressBar>(R.id.progressBar)
        val progressTitle = findViewById<TextView>(R.id.progressTitle)
        val textForgot = findViewById<TextView>(R.id.forgotPassword)
        val errorPassword = findViewById<TextView>(R.id.errorPassword)
        val errorConfirmPassword = findViewById<TextView>(R.id.errorConfirmPassword)
        val goBack = findViewById<Button>(R.id.go_back)

        val uiModel = UIUtils()

        val upDateButton = findViewById<Button>(R.id.update)
        upDateButton.setOnClickListener {
            if(uiModel.isEditEmpty(listOf(editPassword, editNewPassword, editConfirmPassword))) {
                uiModel.showToast(applicationContext, "Ingresa todos los datos que se solicitan")
                return@setOnClickListener
            }

            if(!uiModel.isPasswordValid(editNewPassword.text.toString())) {
                errorPassword.visibility = View.VISIBLE
                return@setOnClickListener
            }

            if(editNewPassword.text.toString() != editConfirmPassword.text.toString()) {
                errorConfirmPassword.visibility = View.VISIBLE
                return@setOnClickListener
            }

            progressTitle.visibility = View.VISIBLE
            progressBar.visibility = View.VISIBLE
            upDateButton.isEnabled = false
            upDateButton.setBackgroundResource(R.drawable.backgroun_button_unenable)
            goBack.isEnabled = false
            goBack.setBackgroundResource(R.drawable.backgroun_button_unenable)

            val email = user?.email
            val credential = EmailAuthProvider.getCredential(email.toString(), editPassword.text.toString())

            user!!.reauthenticate(credential)
                .addOnCompleteListener(this) {
                    if(it.isSuccessful) {
                        user.updatePassword(editNewPassword.text.toString())
                            .addOnCompleteListener { task ->
                                if(task.isSuccessful) {
                                    uiModel.showToast(applicationContext, "Contraseña actualizada")
                                    editPassword.setText("")
                                    editNewPassword.setText("")
                                    editConfirmPassword.setText("")
                                    progressBar.visibility = View.GONE
                                    progressTitle.visibility = View.GONE
                                    upDateButton.isEnabled = true
                                    upDateButton.setBackgroundResource(R.drawable.background_button_primary)
                                    goBack.isEnabled = true
                                    goBack.setBackgroundResource(R.drawable.background_button_secondary)
                                }
                            }
                    } else {
                        uiModel.showToast(applicationContext, "Contraseña incorrecta. Vuelve a intentarlo.")
                        progressBar.visibility = View.GONE
                        progressTitle.visibility = View.GONE
                        upDateButton.isEnabled = true
                        upDateButton.setBackgroundResource(R.drawable.background_button_primary)
                        goBack.isEnabled = true
                        goBack.setBackgroundResource(R.drawable.background_button_secondary)
                    }
                }
        }

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

        val eyeButton2 = findViewById<ImageButton>(R.id.eyeButton2)
        var visibility2 = false

        eyeButton2.setOnClickListener {
            if(visibility2) {
                visibility2 = false
                editNewPassword.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
                eyeButton2.setImageResource(R.drawable.baseline_visibility_24)
            } else {
                visibility2 = true
                editNewPassword.inputType = InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
                eyeButton2.setImageResource(R.drawable.baseline_visibility_off_24)
            }
        }

        val eyeButton3 = findViewById<ImageButton>(R.id.eyeButton3)
        var visibility3 = false

        eyeButton3.setOnClickListener {
            if(visibility3) {
                visibility3 = false
                editConfirmPassword.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
                eyeButton3.setImageResource(R.drawable.baseline_visibility_24)
            } else {
                visibility3 = true
                editConfirmPassword.inputType = InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
                eyeButton3.setImageResource(R.drawable.baseline_visibility_off_24)
            }
        }

        textForgot.setOnClickListener {
            val change = Intent(this, RecoverPassword::class.java)
            startActivity(change)
        }

        goBack.setOnClickListener{
            finish()
        }
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