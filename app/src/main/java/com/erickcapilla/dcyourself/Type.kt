package com.erickcapilla.dcyourself

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AlertDialog

class Type : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_type)

        val next = findViewById<Button>(R.id.next)
        next.setOnClickListener {
            val change = Intent(this, Home::class.java)
            startActivity(change)
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        AlertDialog.Builder(this@Type)
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