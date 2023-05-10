package com.erickcapilla.dcyourself

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.content.Intent
import androidx.appcompat.app.AlertDialog
import com.erickcapilla.dcyourself.provider.services.firebase.FBAuth


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.Theme_DCYour)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val firebaseAuth = FBAuth()
        if (firebaseAuth.isUserLogged()) {
            val change = Intent(this, Home::class.java)
            startActivity(change)
        } else {
            setup()
        }

    }

    private fun setup() {
        val signup = findViewById<Button>(R.id.signUp)
        val login = findViewById<Button>(R.id.logIn)

        signup.setOnClickListener {
            val change = Intent(this, SignUp::class.java)
            startActivity(change)
        }

        login.setOnClickListener {
            val change = Intent(this, Login::class.java)
            startActivity(change)
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        AlertDialog.Builder(this@MainActivity)
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