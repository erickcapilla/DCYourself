package com.erickcapilla.dcyourself


import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.erickcapilla.dcyourself.util.DatePickerFragment
import com.erickcapilla.dcyourself.util.UIUtils
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


class BirthDate : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_birth_date)

        val calendar = findViewById<EditText>(R.id.calendarView)
        val nextButton = findViewById<Button>(R.id.next)
        val goBack = findViewById<Button>(R.id.go_back)

        val uiModel = UIUtils()

        calendar.setOnClickListener{ showDatePickerDialog() }

        nextButton.setOnClickListener {
            if(!uiModel.isEditEmpty(listOf(calendar))) {
                val change = Intent(this, WeightHeight::class.java)
                startActivity(change)
            } else {
                uiModel.showToast(applicationContext, "Elige una fecha")
            }
        }

        goBack.setOnClickListener { finish() }
    }

    private fun showDatePickerDialog() {
        val datePicker = DatePickerFragment { day, month, year -> onDateSelected(day, month, year) }
        datePicker.show(supportFragmentManager, "datePicker")
    }

    private fun onDateSelected(day: Int, month: Int, year: Int) {
        if(year > 2020) {
            val uiModel = UIUtils()
            uiModel.showToast(applicationContext, "Elige un año menor o igual a 2020")
        } else {
            val calendar = findViewById<EditText>(R.id.calendarView)
            val dayString = if (day < 10) "0$day" else "$day"
            val monthString = if (month < 10) "0" + (month+1) else "" + (month+1)
            val date = "$dayString/$monthString/$year"
            calendar.setText(date)
            saveData(date)
        }
    }

    private fun saveData(data: String) {
        val db = Firebase.firestore
        val user = Firebase.auth.currentUser
        var email = ""
        user?.let {
            for (profile in it.providerData) {
                email = profile.email.toString()
            }
        }

        db.collection("info").document(email)
            .update("date", data)
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        AlertDialog.Builder(this@BirthDate)
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