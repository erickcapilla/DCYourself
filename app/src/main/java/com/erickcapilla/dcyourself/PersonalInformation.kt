package com.erickcapilla.dcyourself

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import androidx.appcompat.app.AlertDialog
import com.erickcapilla.dcyourself.util.DatePickerFragment
import com.erickcapilla.dcyourself.util.Utils
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class PersonalInformation : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_personal_information)

        val userName = findViewById<EditText>(R.id.userName)
        val userLastName = findViewById<EditText>(R.id.userLastName)
        val userLastName2 = findViewById<EditText>(R.id.userLastName2)
        val calendar = findViewById<EditText>(R.id.calendar)
        val spinnerGender = findViewById<Spinner>(R.id.spinnerGender)
        val userWeight = findViewById<EditText>(R.id.weight)
        val userHeight = findViewById<EditText>(R.id.height)
        val userFamily = findViewById<EditText>(R.id.family)
        val userExercise = findViewById<Spinner>(R.id.exercise)
        val next = findViewById<Button>(R.id.next)
        val goBack = findViewById<Button>(R.id.goBack)

        val uiModels = Utils()

        val listGender = arrayOf("Mujer", "Hombre")
        val genderAdapter = ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, listGender)
        spinnerGender.adapter = genderAdapter

        val listExercise = arrayOf("Nunca", "1-2", "3-4", "5-7")
        val exerciseAdapter = ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, listExercise)
        userExercise.adapter = exerciseAdapter

        auth = Firebase.auth
        val db = Firebase.firestore
        val user = Firebase.auth.currentUser
        var email = ""
        user?.let {
            for (profile in it.providerData) {
                email = profile.email.toString()
            }
        }

        val docRefInfo = db.collection("info").document(email)
        val docRefUser = db.collection("user").document(email)

        docRefUser.get().addOnSuccessListener { doc ->
            if(doc != null) {
                val name = doc.data?.get("name").toString()
                userName.setText(name)
                val lastName = doc.data?.get("lastName").toString()
                userLastName.setText(lastName)
                val lastName2 = doc.data?.get("lastName2").toString()
                userLastName2.setText(lastName2)
            }
        }

        docRefInfo.get().addOnSuccessListener { doc ->
            val date = doc.data?.get("date").toString()
            calendar.setText(date)

            val exercise = doc.data?.get("exercise").toString()
            var exercisePosition = 0
            if(exercise == "Nunca") exercisePosition = 0
            else if(exercise == "1-2") exercisePosition = 1
            else if(exercise == "3-4") exercisePosition = 2
            else if(exercise == "5-7") exercisePosition = 3
            userExercise.setSelection(exercisePosition)

            var family = doc.data?.get("family").toString()
            if(family == "No") family = "0"
            userFamily.setText(family)

            val gender = doc.data?.get("gender").toString()
            var genderPosition = 0
            if(gender == "Mujer") genderPosition = 0 else genderPosition = 1
            spinnerGender.setSelection(genderPosition)

            val weight = doc.data?.get("weight").toString()
            userWeight.setText(weight)

            val height = doc.data?.get("height").toString()
            userHeight.setText(height)
        }

        calendar.setOnClickListener{ showDatePickerDialog() }

        next.setOnClickListener {
            if(uiModels.isEditEmpty(listOf(userName, userLastName, userLastName2, calendar,
                    userWeight, userHeight, userFamily))) {
                uiModels.showToast(applicationContext, "Llena todos los campos que se solicitan")
                return@setOnClickListener
            }

            val genderUpdate = spinnerGender.selectedItem.toString()
            val exerciseUpdate = userExercise.selectedItem.toString()

            updateDate("user", "name", userName.text.toString())
            updateDate("user", "lastName", userLastName.text.toString())
            updateDate("user", "lastName2", userLastName2.text.toString())

            updateDate("info", "family", userFamily.text.toString())
            updateDate("info", "date", calendar.text.toString())
            updateDate("info", "weight", userWeight.text.toString())
            updateDate("info", "height", userHeight.text.toString())
            updateDate("info", "gender", genderUpdate)
            updateDate("info", "exercise", exerciseUpdate)
            uiModels.showToast(applicationContext, "Información actualizada")
        }

        goBack.setOnClickListener {
            finish()
        }
    }

    private fun updateDate(collection: String, info: String, data: String) {
        val db = Firebase.firestore
        val user = Firebase.auth.currentUser
        var email = ""
        user?.let {
            for (profile in it.providerData) {
                email = profile.email.toString()
            }
        }
        db.collection(collection).document(email)
            .update(info, data)
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        AlertDialog.Builder(this@PersonalInformation)
            .setMessage("¿Salir de la aplicación?")
            .setCancelable(false)
            .setPositiveButton("Si") { dialog, whichButton ->
                finishAffinity() //Sale de la aplicación.
            }
            .setNegativeButton("Cancelar") { dialog, whichButton ->

            }
            .show()
    }

    private fun showDatePickerDialog() {
        val datePicker = DatePickerFragment { day, month, year -> onDateSelected(day, month, year) }
        datePicker.show(supportFragmentManager, "datePicker")
    }

    private fun onDateSelected(day: Int, month: Int, year: Int) {
            val calendar = findViewById<EditText>(R.id.calendar)
            val dayString = if (day < 10) "0$day" else "$day"
            val monthString = if (month < 10) "0" + (month+1) else "" + (month+1)
            val date = "$dayString/$monthString/$year"
            calendar.setText(date)
    }
}