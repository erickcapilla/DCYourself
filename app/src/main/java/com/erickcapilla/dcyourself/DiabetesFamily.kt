package com.erickcapilla.dcyourself

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import androidx.appcompat.app.AlertDialog
import com.erickcapilla.dcyourself.model.UIModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class DiabetesFamily : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_diabetes_family)

        val noButton = findViewById<ImageButton>(R.id.noBtn)
        val yesButton = findViewById<ImageButton>(R.id.yesBtn)
        val editNumber = findViewById<EditText>(R.id.editNumber)
        val nextButton = findViewById<Button>(R.id.next)
        val logoutButton = findViewById<ImageButton>(R.id.logOut)
        val goBack = findViewById<Button>(R.id.go_back)
        val uiModel = UIModel()

        nextButton.isEnabled = false
        nextButton.setBackgroundResource(R.drawable.button_backgroun_unenable)
        editNumber.isEnabled = false
        editNumber.setBackgroundResource(R.drawable.button_backgroun_unenable)

        var family = ""
        var familyBool = false

        noButton.setOnClickListener {
            noButton.isEnabled = false
            yesButton.isEnabled = true
            nextButton.isEnabled = true
            nextButton.setBackgroundResource(R.drawable.button_background_primary)
            noButton.setBackgroundResource(R.drawable.button_backgroun_unenable)
            yesButton.setBackgroundResource(R.drawable.button_background_info)
            editNumber.isEnabled = false
            editNumber.setBackgroundResource(R.drawable.button_backgroun_unenable)
            family = "No"
            familyBool = false
        }

        yesButton.setOnClickListener {
            yesButton.isEnabled = false
            noButton.isEnabled = true
            nextButton.isEnabled = true
            nextButton.setBackgroundResource(R.drawable.button_background_primary)
            yesButton.setBackgroundResource(R.drawable.button_backgroun_unenable)
            noButton.setBackgroundResource(R.drawable.button_background_info)
            editNumber.isEnabled = true
            editNumber.setBackgroundResource(R.drawable.button_background_secondary)
            familyBool = true
        }

        nextButton.setOnClickListener {
            if(!familyBool) {
                saveData(family)
                val change = Intent(this, Exercise::class.java)
                startActivity(change)
                return@setOnClickListener
            }
            if(!uiModel.isEditEmpty(listOf(editNumber)) && familyBool) {
                family = editNumber.text.toString()
                saveData(family)
                val change = Intent(this, Exercise::class.java)
                startActivity(change)
            } else {
                uiModel.showToast(applicationContext, "Ingresa todos los datos que se solicitan")
            }
        }


        logoutButton.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            val change = Intent(this, MainActivity::class.java)
            startActivity(change)
        }
        goBack.setOnClickListener { finish() }
    }

    private fun saveData (data: String) {
        val db = Firebase.firestore
        val user = Firebase.auth.currentUser
        var email = ""
        user?.let {
            for (profile in it.providerData) {
                email = profile.email.toString()
            }
        }

        db.collection("info").document(email)
            .update("family", data)
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        AlertDialog.Builder(this@DiabetesFamily)
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