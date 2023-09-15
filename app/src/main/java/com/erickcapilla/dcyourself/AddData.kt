package com.erickcapilla.dcyourself

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import com.erickcapilla.dcyourself.util.Utils
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class AddData : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_data)

        auth = Firebase.auth
        val db = Firebase.firestore
        val user = Firebase.auth.currentUser
        var email = ""
        user?.let {
            for (profile in it.providerData) {
                email = profile.email.toString()
            }
        }

        val uiModel = Utils()
        val editGlucose = findViewById<EditText>(R.id.editGlucose)
        val editHemoglobin = findViewById<EditText>(R.id.editHemoglobin)
        val editInsulin= findViewById<EditText>(R.id.editInsulin)

        val next = findViewById<Button>(R.id.next)
        next.setOnClickListener {
            if(!uiModel.isEditEmpty(listOf(editGlucose, editHemoglobin, editInsulin))) {
                val glucose = editGlucose.text.toString().toFloat()
                val hemoglobin = editHemoglobin.text.toString().toFloat()
                val insulin = editInsulin.text.toString().toFloat()

                val docRefUser = db.collection("data").document(email)
                val medicinesData = docRefUser.collection("userData")

                val useData = hashMapOf(
                    "glucose" to glucose,
                    "hemoglobin" to hemoglobin,
                    "insulin" to insulin
                )

                medicinesData.add(useData).addOnSuccessListener { doc ->
                    uiModel.showToast(applicationContext, "¡Datos agregados!")
                    val change = Intent(this, Graph::class.java)
                    startActivity(change)
                }
            } else {
                uiModel.showToast(applicationContext, "Ingresa todos los datos que se solicitan")
            }

        }

        val goBack = findViewById<Button>(R.id.go_back)
        goBack.setOnClickListener{
            finish()
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        AlertDialog.Builder(this@AddData)
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