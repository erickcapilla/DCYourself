package com.erickcapilla.dcyourself

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.erickcapilla.dcyourself.adapter.MedicineAdapter
import com.erickcapilla.dcyourself.util.DatePickerFragment
import com.erickcapilla.dcyourself.util.UIUtils
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class UpdateMedicines : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var adapter: MedicineAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_medicines)

        auth = Firebase.auth
        val db = Firebase.firestore
        val user = Firebase.auth.currentUser
        var email = ""
        user?.let {
            for (profile in it.providerData) {
                email = profile.email.toString()
            }
        }

        val docRefUser = db.collection("med").document(email)
        val medicinesData = docRefUser.collection("meds")

        val uiModel = UIUtils()

        val bundle = intent.extras
        val id = bundle?.getString("id").toString()
        val name = bundle?.getString("name")
        val dose = bundle?.getString("dose")
        val days = bundle?.getString("days")
        val time = bundle?.getString("time")
        val quantity = bundle?.getString("quantity")
        val date = bundle?.getString("date")

        val editName = findViewById<EditText>(R.id.editName)
        val editDose = findViewById<EditText>(R.id.editDose)
        val editFrequency = findViewById<Spinner>(R.id.editFrequency)
        val editDays = findViewById<EditText>(R.id.editDays)
        val editEnd = findViewById<EditText>(R.id.editEnd)
        val editTotal = findViewById<EditText>(R.id.editTotal)

        val listFrequency = arrayOf("6", "8", "12", "24")
        val frequencyAdapter = ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, listFrequency)
        editFrequency.adapter = frequencyAdapter
        editFrequency.prompt = "Cada cuantas horas"

        editEnd.setOnClickListener{ showDatePickerDialog(editEnd) }

        var exercisePosition = 0
        if(time == "6") exercisePosition = 0
        else if(time == "8") exercisePosition = 1
        else if(time == "12") exercisePosition = 2
        else if(time == "24") exercisePosition = 3
        editFrequency.setSelection(exercisePosition)
        editName.setText(name.toString())
        editDose.setText(dose.toString())
        editDays.setText(days.toString())
        editTotal.setText(quantity.toString())

        val buttonUpdate = findViewById<Button>(R.id.buttonUpdate)
        buttonUpdate.setOnClickListener {
            if(uiModel.isEditEmpty(listOf(editName, editDose, editDays, editEnd, editTotal))) {
                uiModel.showToast(applicationContext, "Llena todos los campos que se solicitan")
                return@setOnClickListener
            }

            val medData = hashMapOf(
                "name" to editName.text.toString(),
                "total" to editTotal.text.toString(),
                "dose" to editDose.text.toString(),
                "frequency" to editFrequency.selectedItem.toString(),
                "dateTwo" to editEnd.text.toString()
            )
            medicinesData.document(id).update(medData as Map<String, Any>)
                .addOnSuccessListener {
                    uiModel.showToast(applicationContext, "Medicamento actualizado")
                    val change = Intent(this, Medicines::class.java)
                    startActivity(change)
                    change.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                    overridePendingTransition(0,0)
                }
        }

        val deleteButton = findViewById<Button>(R.id.buttonDelete)
        deleteButton.setOnClickListener {
            AlertDialog.Builder(this@UpdateMedicines)
                .setMessage("¿Seguro que desea borrar el medicamento: $name?")
                .setCancelable(false)
                .setPositiveButton("Si") { dialog, whichButton ->
                    medicinesData.document(id).delete()
                        .addOnSuccessListener {
                            uiModel.showToast(applicationContext, "Medicamento $name eliminado")
                            val change = Intent(this, Medicines::class.java)
                            startActivity(change)
                            change.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                            overridePendingTransition(0,0)
                        }
                }
                .setNegativeButton("Cancelar") { dialog, whichButton ->

                }
                .show()
        }

        val bottomMenu = findViewById<BottomNavigationView>(R.id.bottomNavigation)
        bottomMenu.selectedItemId = R.id.bottom_med

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