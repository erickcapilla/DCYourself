package com.erickcapilla.dcyourself

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AlertDialog
import com.erickcapilla.dcyourself.util.UIUtils
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class Exercise : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_exercise)

        val neverBtn = findViewById<Button>(R.id.neverBtn)
        val oneBtn = findViewById<Button>(R.id.oneBtn)
        val threeBtn = findViewById<Button>(R.id.threeBtn)
        val fiveBtn = findViewById<Button>(R.id.fiveBtn)
        val nextButton = findViewById<Button>(R.id.next)
        val goBack = findViewById<Button>(R.id.go_back)

        val uiModel = UIUtils()

        nextButton.isEnabled = false
        nextButton.setBackgroundResource(R.drawable.background_button_unenable)

        var exercise = ""

        neverBtn.setOnClickListener {
            neverBtn.isEnabled = false
            neverBtn.setBackgroundResource(R.drawable.background_button_unenable)

            oneBtn.isEnabled = true
            oneBtn.setBackgroundResource(R.drawable.background_button_secondary)

            threeBtn.isEnabled = true
            threeBtn.setBackgroundResource(R.drawable.background_button_secondary)

            fiveBtn.isEnabled = true
            fiveBtn.setBackgroundResource(R.drawable.background_button_info)

            nextButton.isEnabled = true
            nextButton.setBackgroundResource(R.style.ButtonPrimary)

            exercise = "Nunca"
        }

        oneBtn.setOnClickListener {
            oneBtn.isEnabled = false
            oneBtn.setBackgroundResource(R.drawable.background_button_unenable)

            neverBtn.isEnabled = true
            neverBtn.setBackgroundResource(R.drawable.background_button_info)

            threeBtn.isEnabled = true
            threeBtn.setBackgroundResource(R.drawable.background_button_secondary)

            fiveBtn.isEnabled = true
            fiveBtn.setBackgroundResource(R.drawable.background_button_info)

            nextButton.isEnabled = true
            nextButton.setBackgroundResource(R.style.ButtonPrimary)

            exercise = "1-2"
        }

        threeBtn.setOnClickListener {
            threeBtn.isEnabled = false
            threeBtn.setBackgroundResource(R.drawable.background_button_unenable)

            neverBtn.isEnabled = true
            neverBtn.setBackgroundResource(R.drawable.background_button_info)

            oneBtn.isEnabled = true
            oneBtn.setBackgroundResource(R.drawable.background_button_secondary)

            fiveBtn.isEnabled = true
            fiveBtn.setBackgroundResource(R.drawable.background_button_info)

            nextButton.isEnabled = true
            nextButton.setBackgroundResource(R.style.ButtonPrimary)

            exercise = "3-4"
        }

        fiveBtn.setOnClickListener {
            fiveBtn.isEnabled = false
            fiveBtn.setBackgroundResource(R.drawable.background_button_unenable)

            neverBtn.isEnabled = true
            neverBtn.setBackgroundResource(R.drawable.background_button_info)

            oneBtn.isEnabled = true
            oneBtn.setBackgroundResource(R.drawable.background_button_secondary)

            threeBtn.isEnabled = true
            threeBtn.setBackgroundResource(R.drawable.background_button_secondary)

            nextButton.isEnabled = true
            nextButton.setBackgroundResource(R.style.ButtonPrimary)

            exercise = "5-7"
        }

        nextButton.setOnClickListener {
            saveData(exercise)
            uiModel.showToast(applicationContext, "¡Gracias por proporcionar tu información!")
            val change = Intent(this, Home::class.java)
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
            .update("exercise", data)
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        AlertDialog.Builder(this@Exercise)
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