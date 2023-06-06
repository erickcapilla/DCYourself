package com.erickcapilla.dcyourself

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.InputType
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.cardview.widget.CardView
import com.erickcapilla.dcyourself.util.UIUtils
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class Profile : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        val userName = findViewById<TextView>(R.id.userName)
        val account = findViewById<CardView>(R.id.account)
        val personal = findViewById<CardView>(R.id.personal)
        val preferences = findViewById<CardView>(R.id.preferences)
        val outButton = findViewById<Button>(R.id.logOut)
        val delete = findViewById<Button>(R.id.deleteAccount)
        val goBack = findViewById<Button>(R.id.goBack)

        val uiUtil = UIUtils()

        auth = Firebase.auth
        val db = Firebase.firestore
        val user = Firebase.auth.currentUser
        var email = ""
        user?.let {
            for (profile in it.providerData) {
                email = profile.email.toString()
            }
        }

        val docRefUser = db.collection("user").document(email)

        docRefUser.get().addOnSuccessListener { document ->
            if(document != null) {
                val name = document.data?.get("name").toString()
                val lastName = document.data?.get("lastName").toString()
                val lastName2 = document.data?.get("lastName2").toString()

                userName.text = "$name $lastName $lastName2"
            }
        }

        account.setOnClickListener {
            val change = Intent(this, ChangePassword::class.java)
            startActivity(change)
        }

        personal.setOnClickListener {
            val change = Intent(this, PersonalInformation::class.java)
            startActivity(change)
        }

        preferences.setOnClickListener {}

        outButton.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            val change = Intent(this, MainActivity::class.java)
            startActivity(change)
        }

        delete.setOnClickListener {
            val editPassword = EditText(this)
            editPassword.hint = "Confirma con tu contraseña"
            editPassword.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
            AlertDialog.Builder(this)
                .setTitle("Borrar cuenta")
                .setMessage("Tus datos se perderan completamente")
                .setView(editPassword)
                .setPositiveButton("Eliminar") { dialog, whichButton ->
                    if(uiUtil.isEditEmpty(listOf(editPassword))) {
                        uiUtil.showToast(applicationContext, "Ingresa todos los datos que se solicitan")
                        return@setPositiveButton
                    }
                    val pass = editPassword.text.toString()
                    val credential = EmailAuthProvider.getCredential(email, pass)
                    user!!.reauthenticate(credential)
                        .addOnCompleteListener(this) {
                            if (it.isSuccessful) {
                                user.delete()
                                    .addOnCompleteListener { task ->
                                        if (task.isSuccessful) {
                                            uiUtil.showToast(applicationContext, "Cuenta eliminada")
                                            val change = Intent(this, MainActivity::class.java)
                                            startActivity(change)
                                        }
                                    }
                            } else {
                                uiUtil.showToast(applicationContext, "Contraseña incorrecta")
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
        AlertDialog.Builder(this@Profile)
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