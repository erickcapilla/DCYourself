package com.erickcapilla.dcyourself

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.erickcapilla.dcyourself.model.UIModel


class SignUp : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        val uiModel = UIModel()

        val editName = findViewById<EditText>(R.id.editName)
        val editLastName = findViewById<EditText>(R.id.editLastName)
        val editLastName2 = findViewById<EditText>(R.id.editLastName2)

        val next = findViewById<Button>(R.id.next)

        next.setOnClickListener {
            if(!uiModel.isEditEmpty(listOf(editName, editLastName, editLastName2))) {
                val change = Intent(this, SignUp2::class.java)
                change.putExtra("name", editName.text.toString())
                change.putExtra("lastName", editLastName.text.toString())
                change.putExtra("lastName2", editLastName2.text.toString())
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
        AlertDialog.Builder(this@SignUp)
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