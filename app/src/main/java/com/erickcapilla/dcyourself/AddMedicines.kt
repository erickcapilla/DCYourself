package com.erickcapilla.dcyourself

import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import com.erickcapilla.dcyourself.util.DatePickerFragment
import com.erickcapilla.dcyourself.util.UIUtils
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class AddMedicines : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_medicines)

        val editName = findViewById<EditText>(R.id.editName)
        val editDose = findViewById<EditText>(R.id.editDose)
        val editFrequency = findViewById<Spinner>(R.id.editFrequency)
        val editStart = findViewById<EditText>(R.id.editStart)
        val editEnd = findViewById<EditText>(R.id.editEnd)
        val editTotal = findViewById<EditText>(R.id.editTotal)
        val buttonAdd = findViewById<Button>(R.id.buttonAdd)
        val bottomMenu = findViewById<BottomNavigationView>(R.id.bottomNavigation)
        bottomMenu.selectedItemId = R.id.bottom_med

        var name: String
        var total: String
        var dose: String
        var frequency: String
        var dateOne: String
        var dateTwo: String

        val listFrequency = arrayOf("6", "8", "12", "24")
        val frequencyAdapter = ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, listFrequency)
        editFrequency.adapter = frequencyAdapter
        editFrequency.prompt = "Cada cuantas horas"

        val uiModels = UIUtils()

        editStart.setOnClickListener{ showDatePickerDialog(editStart) }
        editEnd.setOnClickListener{ showDatePickerDialog(editEnd) }

        auth = Firebase.auth
        val db = Firebase.firestore
        val user = Firebase.auth.currentUser
        var email = ""
        user?.let {
            for (profile in it.providerData) {
                email = profile.email.toString()
            }
        }

        buttonAdd.setOnClickListener {
            if(uiModels.isEditEmpty(listOf(editName, editDose, editStart, editEnd, editTotal))) {
                uiModels.showToast(applicationContext, "Llena todos los campos que se solicitan")
                return@setOnClickListener
            }

            val docRefUser = db.collection("med").document(email)

            docRefUser.get().addOnSuccessListener { document ->
                /*if(doc != null) {
                    /*val name = doc.data?.get("name") as Map<*>
                    val total = doc.data?.get("total") as Map<*,*>
                    val dose = doc.data?.get("dose") as Map<*,*>
                    val frequency = doc.data?.get("frequency") as Map<*,*>
                    val dateOne = doc.data?.get("dateOne") as Map<*,*>
                    val dateTwo = doc.data?.get("dateTwo") as Map<*,*>*/
                    val nameArray = doc.toObject(ArrayList::class.java)
                    for (doc in querySnapshot) {
                        val name = ArrayList<String>()
                        name.addAll(nameArray as ArrayList<String>)
                    }

                    /*name.toList().toMutableList().add(editName.text.toString())
                    total.toList().toMutableList().add(editTotal.text.toString())
                    dose.toList().toMutableList().add(editDose.text.toString())
                    frequency.toList().toMutableList().add(editFrequency.selectedItem.toString())
                    dateOne.toList().toMutableList().add(editStart.text.toString())
                    dateTwo.toList().toMutableList().add(editEnd.text.toString())

                    docRefUser.set().addOnSuccessListener {

                    }*/
                }*/
                db.collection("med").document(email)
                    .update("name", editName.text.toString())
                db.collection("med").document(email)
                    .update("total", editTotal.text.toString())
                db.collection("med").document(email)
                    .update("dose", editDose.text.toString())
                db.collection("med").document(email)
                    .update("frequency", editFrequency.selectedItem.toString())
                db.collection("med").document(email)
                    .update("dateOne", editStart.text.toString())
                db.collection("med").document(email)
                    .update("dateTwo", editEnd.text.toString())
            }

        }

        bottomMenu.setOnItemSelectedListener {
            when(it.itemId) {
                R.id.bottom_med -> {
                    val change = Intent(this, Medicines::class.java)
                    startActivity(change)
                    change.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                    overridePendingTransition(0,0)
                    finish()
                    true
                }
                R.id.bottom_data -> {
                    val change = Intent(this, Graph::class.java)
                    startActivity(change)
                    change.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                    overridePendingTransition(0,0)
                    finish()
                    true
                }
                R.id.bottom_diagnose -> {
                    val change = Intent(this, Diagnose::class.java)
                    startActivity(change)
                    change.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                    overridePendingTransition(0,0)
                    true
                }
                R.id.bottom_recommend -> {
                    val change = Intent(this, Recommendations::class.java)
                    startActivity(change)
                    change.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                    overridePendingTransition(0,0)
                    finish()
                    true
                }
                R.id.bottom_home -> {
                    val change = Intent(this, Home::class.java)
                    startActivity(change)
                    change.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                    overridePendingTransition(0,0)
                    finish()
                    true
                }
                else -> false
            }
        }
    }

    private fun showDatePickerDialog(edit: EditText) {
        val datePicker = DatePickerFragment { day, month, year -> onDateSelected(day, month, year, edit) }
        datePicker.show(supportFragmentManager, "datePicker")
    }

    private fun onDateSelected(day: Int, month: Int, year: Int, edit: EditText) {
        val dayString = if (day < 10) "0$day" else "$day"
        val monthString = if (month < 10) "0" + (month+1) else "" + (month+1)
        val date = "$dayString/$monthString/$year"
        edit.setText(date)
    }
}