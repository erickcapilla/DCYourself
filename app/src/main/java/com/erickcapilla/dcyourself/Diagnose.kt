package com.erickcapilla.dcyourself

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import com.erickcapilla.dcyourself.model.UIModel

class Diagnose : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_diagnose)

        val uiModel = UIModel()
        val editGlucose = findViewById<EditText>(R.id.editGlucose)
        val editHemoglobin = findViewById<EditText>(R.id.editHemoglobin)
        val editInsulin= findViewById<EditText>(R.id.editInsulin)

        val next = findViewById<Button>(R.id.next)
        next.setOnClickListener {
            if(!uiModel.isEditEmpty(listOf(editGlucose, editHemoglobin, editInsulin))) {
                val change = Intent(this, Negative::class.java)
                startActivity(change)
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
        AlertDialog.Builder(this@Diagnose)
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