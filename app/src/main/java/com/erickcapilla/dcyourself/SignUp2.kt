package com.erickcapilla.dcyourself

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.view.View
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.erickcapilla.dcyourself.util.UIUtils
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
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
        val goBack = findViewById<Button>(R.id.go_back)

        val uiModel = UIUtils()

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
                editConfirmPassword.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
                eyeButton2.setImageResource(R.drawable.baseline_visibility_24)
            } else {
                visibility2 = true
                editConfirmPassword.inputType = InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
                eyeButton2.setImageResource(R.drawable.baseline_visibility_off_24)
            }
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
                uiModel.showToast(applicationContext, "Email no valido")
                return@setOnClickListener
            }

            if(password != confirmPassword) {
                uiModel.showToast(applicationContext, "Las contraseñas deben coincidir")
                return@setOnClickListener
            }

            if(!accept.isChecked) {
                uiModel.showToast(applicationContext, "Debes aceptar los términos y condiciones")
                return@setOnClickListener
            }

            progressTitle.visibility = View.VISIBLE
            progressBar.visibility = View.VISIBLE
            signUp.isEnabled = false
            signUp.setBackgroundResource(R.drawable.button_backgroun_unenable)
            goBack.isEnabled = false
            goBack.setBackgroundResource(R.drawable.button_backgroun_unenable)

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
                                    "exercise" to "",

                                ))
                        }
                    Firebase.auth.signOut()
                    editEmail.setText("")
                    editPassword.setText("")
                    editConfirmPassword.setText("")
                    progressBar.visibility = View.GONE
                    progressTitle.visibility = View.GONE
                    accept.isChecked = false
                    uiModel.showToast(applicationContext, "Usuario registrado")
                    val change = Intent(this, Login::class.java)
                    startActivity(change)
                } else {
                    signUp.isEnabled = true
                    progressBar.visibility = View.GONE
                    progressTitle.visibility = View.GONE
                    signUp.setBackgroundResource(R.drawable.button_background_primary)
                    goBack.isEnabled = true
                    goBack.setBackgroundResource(R.drawable.button_background_secondary)
                    uiModel.showToast(applicationContext, "Se ha producido un error. Vuelve a intentarlo")
                }
            }

            /*
                    /*val status = fBAuth.signUpUser(
                        name.toString(), lastName.toString(), lastName2.toString(), email, password
                    )*/
                    if(!uiModel.isEmailValid(email)) {
                        uiModel.showToast(applicationContext, "Email no valido")
                        return@setOnClickListener
                    }
                    auth.createUserWithEmailAndPassword(editEmail.text.toString(),
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
                                    }
                                Firebase.auth.signOut()
                                uiModel.showToast(applicationContext, "Usuario registrado")
                            } else {
                                uiModel.showToast(applicationContext, "Se ha producido un error. Vuelve a intentarlo")
                            }
                        }
                    /*if(status) {
                        uiModel.showToast(applicationContext, "Usuario registrado")
                    } else {
                        uiModel.showToast(applicationContext, "Se ha producido un error, Vuelve a intentarlo")
                    }*/
                 */
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