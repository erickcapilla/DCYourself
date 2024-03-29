package com.erickcapilla.dcyourself

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import com.erickcapilla.dcyourself.util.Utils
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class WeightHeight : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_weight_height)

        val editHeight = findViewById<EditText>(R.id.editHeight)
        val editWeight = findViewById<EditText>(R.id.editWeight)
        val nextButton = findViewById<Button>(R.id.next)
        val goBack = findViewById<Button>(R.id.go_back)

        val uiModel = Utils()

        nextButton.setOnClickListener {
            if(!uiModel.isEditEmpty(listOf(editHeight, editWeight))) {
                val height = editHeight.text.toString()
                val weight = editWeight.text.toString()
                saveData("height", height)
                saveData("weight", weight)
                val change = Intent(this, DiabetesFamily::class.java)
                startActivity(change)
            } else {
                uiModel.showToast(applicationContext, "Ingresa todos los datos que se solicitan")
            }
        }

        goBack.setOnClickListener { finish() }
    }

    private fun saveData(field: String, data: String) {
        val db = Firebase.firestore
        val user = Firebase.auth.currentUser
        var email = ""
        user?.let {
            for (profile in it.providerData) {
                email = profile.email.toString()
            }
        }

        db.collection("info").document(email)
            .update(field, data)
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        AlertDialog.Builder(this@WeightHeight)
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