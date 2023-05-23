package com.erickcapilla.dcyourself

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import com.erickcapilla.dcyourself.util.UIUtils
import com.erickcapilla.dcyourself.provider.services.firebase.FBAuth
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class Home : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        setup()
        val firebaseAuth = FBAuth()
        if (!firebaseAuth.isUserLogged()) {
            val change = Intent(this, MainActivity::class.java)
            startActivity(change)
        }
    }

    private fun setup() {
        auth = Firebase.auth
        val db = Firebase.firestore
        val user = Firebase.auth.currentUser
        var email = ""
        user?.let {
            for (profile in it.providerData) {
                email = profile.email.toString()
            }
        }

        val uiModel = UIUtils()
        val docRefInfo = db.collection("info").document(email)
        val docRefUser = db.collection("user").document(email)

        docRefUser.get().addOnSuccessListener { document ->
            if(document != null) {
                var name = document.data?.get("name").toString()
                val lastName = document.data?.get("lastName").toString()
                val textName = findViewById<TextView>(R.id.textName)
                val nameArray = name.split(" ").toTypedArray()
                name = nameArray[0]

                docRefInfo.get().addOnSuccessListener { doc ->
                    val gender = doc.data?.get("gender").toString()
                    if(gender == "Mujer") textName.text = "¡Bienvenida $name $lastName!" else textName.text = "¡Bienvenido $name $lastName!"
                }
            }
        }

        docRefInfo.get().addOnSuccessListener {document ->
            if(document != null) {
                val gender = document.data?.get("gender").toString()
                val date = document.data?.get("date").toString()
                val height = document.data?.get("height").toString()
                val weight = document.data?.get("weight").toString()
                val family = document.data?.get("family").toString()
                val exercise = document.data?.get("exercise").toString()

                if(gender == "") {
                    val change = Intent(this, Gender::class.java)
                    startActivity(change)
                    return@addOnSuccessListener
                }
                if(date == "") {
                    val change = Intent(this, BirthDate::class.java)
                    startActivity(change)
                    return@addOnSuccessListener
                }
                if(height == "" || weight == "") {
                    val change = Intent(this, WeightHeight::class.java)
                    startActivity(change)
                    return@addOnSuccessListener
                }
                if(family == "") {
                    val change = Intent(this, DiabetesFamily::class.java)
                    startActivity(change)
                    return@addOnSuccessListener
                }
                if(exercise == "") {
                    val change = Intent(this, Exercise::class.java)
                    startActivity(change)
                    return@addOnSuccessListener
                }
            }
        } .addOnFailureListener {
            uiModel.showToast(applicationContext, "Error en la bd")
        }

        val changePassword = findViewById<ImageButton>(R.id.changePassword)
        changePassword.setOnClickListener {
            val change = Intent(this, ChangePassword::class.java)
            startActivity(change)
        }

        val bottomMenu = findViewById<BottomNavigationView>(R.id.bottomNavigation)
        bottomMenu.selectedItemId = R.id.bottom_home

        bottomMenu.setOnItemSelectedListener {
            when(it.itemId) {
                R.id.bottom_home -> true
                R.id.bottom_data -> {
                    val change = Intent(this, Graph::class.java)
                    startActivity(change)
                    change.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                    overridePendingTransition(0,0)
                    finish()
                    true
                }
                R.id.bottom_diagnose -> {
                    val change = Intent(this, Diagnose::class.java)
                    startActivity(change)
                    change.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                    overridePendingTransition(0,0)
                    true
                }
                R.id.bottom_recommend -> {
                    val change = Intent(this, Recommendations::class.java)
                    startActivity(change)
                    change.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                    overridePendingTransition(0,0)
                    finish()
                    true
                }
                R.id.bottom_med -> {
                    val change = Intent(this, Medicines::class.java)
                    startActivity(change)
                    change.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                    overridePendingTransition(0,0)
                    finish()
                    true
                }
                else -> false
            }
        }

        val outButton = findViewById<ImageButton>(R.id.log_out)
        outButton.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            val change = Intent(this, MainActivity::class.java)
            startActivity(change)
        }

    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        AlertDialog.Builder(this@Home)
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