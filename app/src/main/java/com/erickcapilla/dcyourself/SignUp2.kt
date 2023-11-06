package com.erickcapilla.dcyourself

import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import com.erickcapilla.dcyourself.util.Utils
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


class SignUp2 : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private val db = Firebase.firestore


    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up2)

        auth = Firebase.auth
        val bundle = intent.extras
        val name = bundle?.getString("name")
        val lastName = bundle?.getString("lastName")
        val lastName2 = bundle?.getString("lastName2")

        val editEmail = findViewById<EditText>(R.id.editEmail)
        val editPassword = findViewById<EditText>(R.id.editPassword)
        val editConfirmPassword = findViewById<EditText>(R.id.editConfirmPassword)
        val accept = findViewById<CheckBox>(R.id.accept)
        val progressBar = findViewById<ProgressBar>(R.id.progressBar)
        val progressTitle = findViewById<TextView>(R.id.progressTitle)
        val errorEmail = findViewById<TextView>(R.id.errorEmail)
        val errorPassword = findViewById<TextView>(R.id.errorPassword)
        val errorConfirmPassword = findViewById<TextView>(R.id.errorConfirmPassword)
        val goBack = findViewById<Button>(R.id.go_back)

        val uiModel = Utils()

        val eyeButton = findViewById<ImageButton>(R.id.eyeButton)
        var visibility = false

        editEmail.setOnClickListener {
            errorEmail.visibility = View.GONE
        }

        editPassword.setOnClickListener {
            errorPassword.visibility = View.GONE
        }

        editConfirmPassword.setOnClickListener {
            errorConfirmPassword.visibility = View.GONE
        }

        eyeButton.setOnClickListener {
            if(visibility) {
                visibility = false
                editPassword.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
                editConfirmPassword.typeface = ResourcesCompat.getFont(this, R.font.droid_sans)
                editPassword.setSelection(editPassword.text.length)
                eyeButton.setImageResource(R.drawable.baseline_visibility_24)
            } else {
                visibility = true
                editPassword.inputType = InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
                editConfirmPassword.typeface = ResourcesCompat.getFont(this, R.font.droid_sans)
                editPassword.setSelection(editPassword.text.length)
                eyeButton.setImageResource(R.drawable.baseline_visibility_off_24)
            }
        }

        val eyeButton2 = findViewById<ImageButton>(R.id.eyeButton2)
        var visibility2 = false

        eyeButton2.setOnClickListener {
            if(visibility2) {
                visibility2 = false
                editConfirmPassword.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
                editConfirmPassword.typeface = ResourcesCompat.getFont(this, R.font.droid_sans)
                editConfirmPassword.setSelection(editConfirmPassword.text.length)
                eyeButton2.setImageResource(R.drawable.baseline_visibility_24)
            } else {
                visibility2 = true
                editConfirmPassword.inputType = InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
                editConfirmPassword.typeface = ResourcesCompat.getFont(this, R.font.droid_sans)
                editConfirmPassword.setSelection(editConfirmPassword.text.length)
                eyeButton2.setImageResource(R.drawable.baseline_visibility_off_24)
            }
        }

        val eyeButton3 = findViewById<ImageButton>(R.id.eyeButton3)
        eyeButton3.setOnClickListener {
            val change = Intent(this, Terms::class.java)
            startActivity(change)
        }

        val signUp = findViewById<Button>(R.id.signUp)
        signUp.setOnClickListener {
            val email: String
            val password: String
            val confirmPassword: String

            if(uiModel.isEditEmpty(listOf(editEmail, editPassword, editConfirmPassword))) {
                uiModel.showToast(applicationContext, "Ingresa todos los datos que se solicitan")
                return@setOnClickListener
            } else {
                email = editEmail.text.toString()
                password = editPassword.text.toString()
                confirmPassword = editConfirmPassword.text.toString()
            }

            if(!uiModel.isEmailValid(email)) {
                errorEmail.visibility = View.VISIBLE
                return@setOnClickListener
            }

            if(!uiModel.isPasswordValid(password)) {
                errorPassword.visibility = View.VISIBLE
                return@setOnClickListener
            }

            if(password != confirmPassword) {
                errorConfirmPassword.visibility = View.VISIBLE
                return@setOnClickListener
            }

            if(!accept.isChecked) {
                uiModel.showToast(applicationContext, "Debes aceptar los términos y condiciones")
                return@setOnClickListener
            }

            progressTitle.visibility = View.VISIBLE
            progressBar.visibility = View.VISIBLE
            signUp.isEnabled = false
            signUp.setBackgroundResource(R.drawable.background_button_disable)
            goBack.isEnabled = false
            goBack.setBackgroundResource(R.drawable.background_button_disable)

            auth.createUserWithEmailAndPassword(editEmail.text.toString().trim(),
                editPassword.text.toString()).addOnCompleteListener(this) { task ->
                if(task.isSuccessful) {
                    val user = Firebase.auth.currentUser!!
                    val credential = EmailAuthProvider
                        .getCredential(email, password)
                    user.reauthenticate(credential)
                        .addOnCompleteListener {
                            db.collection("user").document(email)
                                .set(hashMapOf(
                                    "name" to name,
                                    "lastName" to lastName,
                                    "lastName2" to lastName2
                                ))
                            db.collection("info").document(email)
                                .set(hashMapOf(
                                    "gender" to "",
                                    "date" to "",
                                    "weight" to "",
                                    "height" to "",
                                    "family" to "",
                                    "exercise" to ""
                                ))
                            db.collection("diagnosis").document(email)
                                .set(hashMapOf(
                                    "diabetic" to "",
                                    "type" to ""
                                ))
                        }

                    editEmail.setText("")
                    editPassword.setText("")
                    editConfirmPassword.setText("")
                    progressBar.visibility = View.GONE
                    progressTitle.visibility = View.GONE
                    accept.isChecked = false
                    uiModel.showToast(applicationContext, "Usuario registrado")
                    val change = Intent(this, Home::class.java)
                    startActivity(change)
                } else {
                    signUp.isEnabled = true
                    progressBar.visibility = View.GONE
                    progressTitle.visibility = View.GONE
                    signUp.setBackgroundResource(R.drawable.background_button_primary)
                    goBack.isEnabled = true
                    goBack.setBackgroundResource(R.drawable.background_button_secondary)
                    val e = task.exception as? FirebaseAuthException
                    Log.v(TAG, e?.errorCode.toString())

                    when (e?.errorCode.toString()) {
                        "ERROR_EMAIL_ALREADY_IN_USE" -> {
                            uiModel.showToast(applicationContext, "El usuario ya existe")
                        }
                        else -> {
                            // Otro error
                            uiModel.showToast(applicationContext, "Ocurrió un error | Revisa tu conexión")
                        }
                    }
                    uiModel.showToast(applicationContext, "Ya hay un usuario con este correo o Revisa tu conexión")
                }
            }
        }

        goBack.setOnClickListener { finish() }
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        AlertDialog.Builder(this@SignUp2)
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