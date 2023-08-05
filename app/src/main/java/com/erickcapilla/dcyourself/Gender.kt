package com.erickcapilla.dcyourself

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import androidx.appcompat.app.AlertDialog
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class Gender : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gender)

        val manButton = findViewById<ImageButton>(R.id.iBMan)
        val womanButton = findViewById<ImageButton>(R.id.iBWoman)
        val nextButton = findViewById<Button>(R.id.next)

        nextButton.isEnabled = false
        nextButton.setBackgroundResource(R.drawable.backgroun_button_unenable)

        var gender = ""

        manButton.setOnClickListener {
            manButton.isEnabled = false
            womanButton.isEnabled = true
            nextButton.isEnabled = true
            nextButton.setBackgroundResource(R.drawable.background_button_primary)
            manButton.setBackgroundResource(R.drawable.backgroun_button_unenable)
            womanButton.setBackgroundResource(R.drawable.background_button_info)
            gender = "Hombre"
        }

        womanButton.setOnClickListener {
            womanButton.isEnabled = false
            manButton.isEnabled = true
            nextButton.isEnabled = true
            nextButton.setBackgroundResource(R.drawable.background_button_primary)
            womanButton.setBackgroundResource(R.drawable.backgroun_button_unenable)
            manButton.setBackgroundResource(R.drawable.background_button_info)
            gender = "Mujer"
        }

        nextButton.setOnClickListener {
            saveData(gender)
            val change = Intent(this, BirthDate::class.java)
            startActivity(change)
        }
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
            .update("gender", data)
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        AlertDialog.Builder(this@Gender)
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