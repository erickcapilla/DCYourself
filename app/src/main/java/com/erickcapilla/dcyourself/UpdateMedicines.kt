package com.erickcapilla.dcyourself

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.erickcapilla.dcyourself.adapter.MedicineAdapter
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth

class UpdateMedicines : AppCompatActivity() {
    private lateinit var adapter: MedicineAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_medicines)

        val bundle = intent.extras
        val position = bundle?.getInt("position")
        val name = bundle?.getString("name")
        val dose = bundle?.getString("dose")
        val days = bundle?.getString("days")
        val time = bundle?.getString("time")
        val quantity = bundle?.getString("quantity")

        val editName = findViewById<EditText>(R.id.editName)
        val editDose = findViewById<EditText>(R.id.editDose)
        val editDays = findViewById<EditText>(R.id.editFrequency)
        val editTime = findViewById<EditText>(R.id.editTime)
        val editTotal = findViewById<EditText>(R.id.editTotal)

        editName.setText(name.toString())
        editDose.setText(dose.toString())
        editDays.setText(days.toString())
        editTime.setText(time.toString())
        editTotal.setText(quantity.toString())

        val deleteButton = findViewById<Button>(R.id.buttonDelete)
        deleteButton.setOnClickListener {
            AlertDialog.Builder(this@UpdateMedicines)
                .setMessage("¿Seguro que desea borrar el medicamento: $name?")
                .setCancelable(false)
                .setPositiveButton("Si") { dialog, whichButton ->
                    val change = Intent(this, Medicines::class.java)
                    change.putExtra("position", position)
                    change.putExtra("delete", true)
                    startActivity(change)
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
}